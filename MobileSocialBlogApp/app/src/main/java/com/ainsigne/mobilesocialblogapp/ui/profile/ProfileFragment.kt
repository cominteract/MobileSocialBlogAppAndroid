package com.ainsigne.mobilesocialblogapp.ui.profile

import android.graphics.Bitmap
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

    var isUpdating = false

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
            if(post.upvotedId != null && post.upvotedId!!.contains(id)){
                post.upvotedId = post.upvotedId!!.filter { it != id } as ArrayList<String>?
                post.upvotes = post.upvotes - 1
            }
            else{
                post.upvotes = post.upvotes + 1
                if(post.downvotedId != null && post.downvotedId!!.contains(id)){
                    post.downvotedId = post.downvotedId!!.filter { it != id } as ArrayList<String>?
                    post.downvotes = post.downvotes - 1
                }

                if(post.upvotedId == null)
                    post.upvotedId = ArrayList()
                post.upvotedId?.add(id)
            }
            presenter?.sendPost(post)
        }
    }

    override fun downvotePost(post: Posts) {

        currentUser()?.id?.let { id ->

            if(post.downvotedId != null && post.downvotedId!!.contains(id)){
                post.downvotedId = post.downvotedId!!.filter { it != id } as ArrayList<String>?
                post.downvotes = post.downvotes - 1
            }
            else{
                post.downvotes = post.downvotes + 1
                if(post.upvotedId != null && post.upvotedId!!.contains(id)){
                    post.upvotedId = post.upvotedId!!.filter { it != id } as ArrayList<String>?
                    post.upvotes = post.upvotes - 1
                }

                if(post.downvotedId == null)
                    post.downvotedId = ArrayList()
                post.downvotedId?.add(id)
            }
            presenter?.sendPost(post)
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

    private fun toggleUpdating(){
        isUpdating = !isUpdating
        if(isUpdating){
            container_profile_et.visibility = View.VISIBLE
            container_profile_tv.visibility = View.GONE

        }else{
            container_profile_et.visibility = View.GONE
            container_profile_tv.visibility = View.VISIBLE
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
            btn_profile_logout.setOnClickListener {
                Config.updateUser("")
                Config.user = null
                main.navigateApp()
            }
            iv_profile_photo.setOnClickListener {
                main.toggleBottomSheet()
            }
            btn_profile_update.setOnClickListener { toggleUpdating() }
        }
        else {
            btn_profile_chat.visibility = View.VISIBLE
            btn_profile_chat.setOnClickListener {
                userFrom(userName)?.let {user ->
                    val bundle = Bundle()
                    bundle.putString("chatId", user.id)
                    main.addOnTopWithBundle(UINavigation.session, bundle)
                }

            }
        }

        btn_profile_posts.setOnClickListener{
            isPost = true
            toggleFeed()
        }
        btn_profile_friends.setOnClickListener {
            isPost = false
            toggleFeed()
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
