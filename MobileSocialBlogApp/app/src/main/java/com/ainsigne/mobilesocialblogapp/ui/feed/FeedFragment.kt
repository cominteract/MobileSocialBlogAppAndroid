package com.ainsigne.mobilesocialblogapp.ui.feed

import android.Manifest
import android.graphics.Bitmap
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.ainsigne.mobilesocialblogapp.R
import com.ainsigne.mobilesocialblogapp.adapters.FeedAdapter
import com.ainsigne.mobilesocialblogapp.base.BaseFragment
import com.ainsigne.mobilesocialblogapp.models.Posts
import com.ainsigne.mobilesocialblogapp.models.Users
import com.ainsigne.mobilesocialblogapp.ui.main.MainActivity
import com.ainsigne.mobilesocialblogapp.ui.main.PhotoRetrieval
import com.ainsigne.mobilesocialblogapp.utils.Config
import com.ainsigne.mobilesocialblogapp.utils.Constants
import com.ainsigne.mobilesocialblogapp.utils.UINavigation
import com.ainsigne.mobilesocialblogapp.utils.toStringFormat
import com.bumptech.glide.Glide
import kotlinx.android.synthetic.main.fragment_feed.*
import org.jetbrains.anko.doAsync
import org.jetbrains.anko.uiThread
import java.util.*
import kotlin.collections.ArrayList


/**
 * A simple [Fragment] subclass.

 * Use the [Feed.newInstance] factory method to
 * create an instance of this fragment.
 *
 */
class FeedFragment : BaseFragment(), FeedView , PhotoRetrieval{

    lateinit var main : MainActivity

    var selectedPhoto = ""

    var post : Posts? = null

    var user : Users? = null

    var isLoading = false

    var isPosting = false

    var isUploading = false

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
            Log.d(" Am i dpwmvoted "," Am i downvoted ${post.downvotes}")
            presenter?.sendPost(post)
        }
    }

    override fun currentUser(): Users? {
        Config.getUser()?.let {
            return userFrom(it)
        }
        return null
    }

    override fun userFrom(author: String): Users? {
        return presenter?.getUserFrom(author)
    }

    override fun addedPostUpdateView() {
        Config.updateRefreshProfile(false)
        Config.updateRefreshChat(false)
        Config.updateRefreshDiscover(false)
        post = null
        selectedPhoto = ""
        presenter?.retrieveAll()
    }

    override fun addedCommentsUpdateView() {

    }

    override fun retrievedAllUpdateView() {
        presenter?.allPosts()?.let {posts ->
            doAsync {
                uiThread {
                    val layoutManager = LinearLayoutManager(it.context)
                    rv_feed?.layoutManager = layoutManager
                    rv_feed?.adapter = FeedAdapter(posts, it, null)
                    Config.getUser()?.let { username ->
                        user = presenter?.getUserFrom(username)
                        tv_feed_username?.text = user?.username
                        user?.photoUrl?.let {photo ->
                            it.context?.let {context ->
                                Glide.with(context).load(photo).override(32,32).into(iv_feed_photo)
                            }
                        }
                    }
                }
            }
        }
    }

    override fun downloadedImageUpdateView(image: String) {

    }

    override fun uploadedImageUpdateView() {
        post?.url = selectedPhoto
        post?.let {
            presenter?.sendPost(it)
        }
    }

    /**
     * injector as FeedInjector injects the presenter with the services and data needed for the
     * implementation
     **/
    var injector = FeedInjectorImplementation()
    /**
     * presenter as FeedPresenter injected automatically to call implementations
     **/
    var presenter: FeedPresenter? = null

    private val PERMISSIONS_STORAGE = arrayOf<String>(
        Manifest.permission.READ_EXTERNAL_STORAGE,
        Manifest.permission.WRITE_EXTERNAL_STORAGE
    )

    override fun navigateToDetails(postId: String) {
        val bundle = Bundle()
        bundle.putString("postId", postId)
        main.addOnTopWithBundle(UINavigation.details, bundle)
    }


    override fun navigateToProfile(username: String) {
        userFrom(username)?.let {user ->
            val bundle = Bundle()
            bundle.putString("username", user.username)
            main.addOnTopWithBundle(UINavigation.profile, bundle)
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
        return inflater.inflate(R.layout.fragment_feed, container, false)


    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

//        btn_upload.setOnClickListener {
//            main.toggleBottomSheet()
//        }
        presenter?.retrieveAll()
        isLoading = true
        container_sendpost.visibility = View.GONE
        btn_share_thoughts.setOnClickListener {
            isPosting = !isPosting
            container_sendpost.visibility = View.GONE
            btn_share_thoughts.setText("Share your thoughts")
            if(isPosting){
                container_sendpost.visibility = View.VISIBLE
                btn_share_thoughts.setText("Close")
            }
        }

        iv_photoupload.setOnClickListener {
            main.toggleBottomSheet()
        }

        btn_sendpost.setOnClickListener {
            post = Posts()
            user = currentUser()
            post?.id = Constants.getRandomString(22)
            user?.let {
                post?.author = it.username
                post?.userId = it.id
            }
            post?.title = et_feedtitle.text.toString()
            post?.body = et_feedmessage.text.toString()
            post?.downvotes = 0
            post?.upvotes = 0
            post?.timestamp = Date().toStringFormat()
            post?.timestamp_from = Date().toStringFormat()
            if(selectedPhoto.isNotEmpty()){
                post?.id?.let { id ->

                    Log.d(" Uploading "," Uploading ")
                    presenter?.uploadPostImage(selectedPhoto, id)
                }
            }else{
                post?.url = Constants.defaultposturl
                post?.let {sentpost ->

                    Log.d(" Sending "," Sending ")
                    presenter?.sendPost(sentpost)
                }
            }
        }
    }

    override fun onResume() {
        super.onResume()

    }

    companion object {
        /**
         * Use this factory method to create a new instance of
         * this fragment using the provided parameters.
         *
         * @return A new instance of fragment Feed.
         */
        // TODO: Rename and change types and number of parameters
        @JvmStatic
        fun newInstance() =
            FeedFragment()
    }

    override fun photoRetrieved(uriString: String?, bitmap: Bitmap?) {
        uriString?.let {
            selectedPhoto = it
        }

        doAsync { uiThread {
            iv_photoupload.setImageBitmap(bitmap)
            // iv_photo_image.setImageBitmap(bitmap)
        } }
    }
}
