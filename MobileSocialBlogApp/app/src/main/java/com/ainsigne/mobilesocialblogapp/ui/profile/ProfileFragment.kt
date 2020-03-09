package com.ainsigne.mobilesocialblogapp.ui.profile

import android.graphics.Bitmap
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Build
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager

import com.ainsigne.mobilesocialblogapp.R
import com.ainsigne.mobilesocialblogapp.adapters.FeedAdapter
import com.ainsigne.mobilesocialblogapp.adapters.FriendsAdapter
import com.ainsigne.mobilesocialblogapp.base.BaseFragment
import com.ainsigne.mobilesocialblogapp.models.Posts
import com.ainsigne.mobilesocialblogapp.models.Users
import com.ainsigne.mobilesocialblogapp.ui.main.MainActivity
import com.ainsigne.mobilesocialblogapp.ui.main.PhotoRetrieval
import com.ainsigne.mobilesocialblogapp.utils.Config
import com.ainsigne.mobilesocialblogapp.utils.UINavigation
import com.bumptech.glide.Glide
import kotlinx.android.synthetic.main.fragment_profile.*
import org.jetbrains.anko.doAsync
import org.jetbrains.anko.uiThread


/**
 * A simple [Fragment] subclass.

 * Use the [Profile.newInstance] factory method to
 * create an instance of this fragment.
 *
 */
class ProfileFragment : BaseFragment(), ProfileView, PhotoRetrieval {



    var isPost = true

    var user : Users? = null

    var userName = ""

    var selectedPhoto = ""


    override fun currentUser(): Users? {
        if(userName.isNotEmpty())
            return userFrom(userName)
        Config.getUser()?.let {
            return userFrom(it)
        }

        return null
    }

    override fun upvotePost(post: Posts) {
        currentUser()?.id?.let { id ->
            presenter?.upvotePost(post,id)
        }
    }

    override fun downvotePost(post: Posts) {

        currentUser()?.id?.let { id ->
            presenter?.downvotePost(post,id)
        }
    }

    override fun navigateToPost(post: Posts) {

        val bundle = Bundle()
        bundle.putString("postId", post.id)
        main.addOnTopWithBundle(UINavigation.details, bundle)

    }

    override fun navigateToProfile(username: String) {
        userFrom(username)?.let {user ->
            val bundle = Bundle()
            bundle.putString("username", user.username)
            main.addOnTopWithBundle(UINavigation.profile, bundle)
        }
    }


    override fun userFrom(author: String): Users? {
        return presenter?.getUserFrom(author)
    }
    override fun retrievedAllUpdateView() {

        doAsync { uiThread {
            user = currentUser()
            user?.photoUrl?.let {photo ->
                it.context?.let {context ->
                    iv_profile_photo?.let {profile ->
                        Glide.with(context).load(photo).override(32,32).into(profile)
                    }
                }
            }
            tv_profile_firstname?.text = user?.firstname
            tv_profile_lastname?.text = user?.lastname
            tv_profile_birthday?.text = user?.birthday
            tv_profile_location?.text = user?.location
            et_profile_firstname?.setText(user?.firstname)
            et_profile_lastname?.setText(user?.lastname)
            et_profile_birthday?.setText(user?.birthday)
            et_profile_location?.setText(user?.location)
            presenter?.allPosts()?.filter { it.author == currentUser()?.username }?.let { posts ->
                val layoutManager = LinearLayoutManager(it.context)
                rv_profile?.layoutManager = layoutManager
                rv_profile?.adapter = FeedAdapter(posts, null, it)

            }
        } }
    }

    override fun addedUserUpdateView() {
        presenter?.retrieveAll()
    }

    override fun downloadedImageUpdateView(image: String) {

    }

    override fun uploadedImageUpdateView() {
        var user = currentUser()
        user?.firstname = et_profile_firstname?.text.toString()
        user?.lastname = et_profile_lastname?.text.toString()
        user?.location = et_profile_location?.text.toString()
        user?.birthday = et_profile_birthday?.text.toString()
        user?.photoUrl = selectedPhoto
        user?.let {
            presenter?.updateUser(it , selectedPhoto.isNotEmpty())
        }
    }

    override fun updatedPostUpdateView() {
        presenter?.retrieveAll()
    }

    /**
     * injector as ProfileInjector injects the presenter with the services and data needed for the
     * implementation
     **/
    var injector = ProfileInjectorImplementation()
    /**
     * presenter as ProfilePresenter injected automatically to call implementations
     **/
    var presenter: ProfilePresenter? = null


    private fun toggleFeed(){
        doAsync {
            uiThread { profile ->
                if(isPost){
                    presenter?.allPosts()?.filter { it.author == currentUser()?.username }?.let { posts ->
                        val layoutManager = LinearLayoutManager(profile.context)
                        rv_profile.layoutManager = layoutManager
                        rv_profile.adapter = FeedAdapter(posts, null, profile)

                    }
                }
                else
                {
                    presenter?.allUsers()?.filter {aUser ->
                        aUser.friendsId != null && aUser.friendsId!!.contains(currentUser()?.id)

                    }?.let { users ->
                        val layoutManager = LinearLayoutManager(profile.context)
                        rv_profile.layoutManager = layoutManager
                        rv_profile.adapter = FriendsAdapter(users, profile)
                    }
                }
            }
        }
    }

    private fun editProfile(){
        container_profile_et.visibility = View.VISIBLE
        container_profile_tv.visibility = View.INVISIBLE
    }
    private fun saveProfile(){
        container_profile_et.visibility = View.VISIBLE
        container_profile_tv.visibility = View.INVISIBLE
        var user = currentUser()
        user?.firstname = et_profile_firstname?.text.toString()
        user?.lastname = et_profile_lastname?.text.toString()
        user?.location = et_profile_location?.text.toString()
        user?.birthday = et_profile_birthday?.text.toString()
        user?.photoUrl = selectedPhoto
        if(selectedPhoto.isNotEmpty()){
            presenter?.uploadImage(selectedPhoto)
        }
        else{
            user?.let {
                presenter?.updateUser(it , selectedPhoto.isNotEmpty())
            }
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        injector.inject(this)
        if(this.context is MainActivity){
            main = this.context as MainActivity
            main.photoRetrieval = this
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_profile, container, false)


    }

    lateinit var main : MainActivity

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        presenter?.retrieveAll()
        arguments?.getString("username")?.let {
            userName = it
        }

        if(!userName.isNotEmpty()){

            iv_profile_photo.setOnClickListener {
                main.toggleBottomSheet()
            }
            btn_profile_edit.setOnClickListener { editProfile() }
            btn_profile_update.setOnClickListener { saveProfile() }
        }
        else {
            btn_profile_chat.visibility = View.VISIBLE
            btn_profile_chat.setOnClickListener {
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                    btn_profile_posts.setBackgroundColor(resources.getColor(R.color.colorPrimary, null))
                    btn_profile_friends.setBackgroundColor(resources.getColor(R.color.colorPrimary, null))
                    it.setBackgroundColor(resources.getColor(R.color.details_app_bg_color, null))
                }else{
                    btn_profile_posts.setBackgroundColor(resources.getColor(R.color.colorPrimary))
                    btn_profile_friends.setBackgroundColor(resources.getColor(R.color.colorPrimary))
                    it.setBackgroundColor(resources.getColor(R.color.details_app_bg_color))
                }
                userFrom(userName)?.let {user ->
                    val bundle = Bundle()
                    bundle.putString("chatId", user.id)
                    main.addOnTopWithBundle(UINavigation.session, bundle)
                }
            }
        }

        btn_profile_posts.setOnClickListener{
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                btn_profile_chat.setBackgroundColor(resources.getColor(R.color.colorPrimary, null))
                btn_profile_friends.setBackgroundColor(resources.getColor(R.color.colorPrimary, null))
                it.setBackgroundColor(resources.getColor(R.color.details_app_bg_color, null))
            }else{
                btn_profile_chat.setBackgroundColor(resources.getColor(R.color.colorPrimary))
                btn_profile_friends.setBackgroundColor(resources.getColor(R.color.colorPrimary))
                it.setBackgroundColor(resources.getColor(R.color.details_app_bg_color))
            }
            isPost = true
            toggleFeed()
        }
        btn_profile_friends.setOnClickListener {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                btn_profile_chat.setBackgroundColor(resources.getColor(R.color.colorPrimary, null))
                btn_profile_posts.setBackgroundColor(resources.getColor(R.color.colorPrimary, null))
                it.setBackgroundColor(resources.getColor(R.color.details_app_bg_color, null))
            }else{
                btn_profile_chat.setBackgroundColor(resources.getColor(R.color.colorPrimary))
                btn_profile_posts.setBackgroundColor(resources.getColor(R.color.colorPrimary))
                it.setBackgroundColor(resources.getColor(R.color.details_app_bg_color))
            }
            isPost = false
            toggleFeed()
        }
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            btn_profile_chat.setBackgroundColor(resources.getColor(R.color.colorPrimary, null))
            btn_profile_friends.setBackgroundColor(resources.getColor(R.color.colorPrimary, null))
            btn_profile_posts.setBackgroundColor(resources.getColor(R.color.details_app_bg_color, null))
        }else{
            btn_profile_chat.setBackgroundColor(resources.getColor(R.color.colorPrimary))
            btn_profile_friends.setBackgroundColor(resources.getColor(R.color.colorPrimary))
            btn_profile_posts.setBackgroundColor(resources.getColor(R.color.details_app_bg_color))
        }
    }


    companion object {
        /**
         * Use this factory method to create a new instance of
         * this fragment using the provided parameters.
         *
         * @return A new instance of fragment Profile.
         */
        // TODO: Rename and change types and number of parameters
        @JvmStatic
        fun newInstance() =
            ProfileFragment()
    }

    override fun photoRetrieved(uriString: String?, bitmap: Bitmap?) {
        uriString?.let {
            selectedPhoto = it
        }

        doAsync { uiThread {
            iv_profile_photo.setImageBitmap(bitmap)
            // iv_photo_image.setImageBitmap(bitmap)
        } }
    }
}
