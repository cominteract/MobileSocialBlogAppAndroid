package com.ainsigne.mobilesocialblogapp.ui.feeddetails

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView

import com.ainsigne.mobilesocialblogapp.R
import com.ainsigne.mobilesocialblogapp.adapters.CommentsAdapter
import com.ainsigne.mobilesocialblogapp.base.BaseFragment
import com.ainsigne.mobilesocialblogapp.models.Comments
import com.ainsigne.mobilesocialblogapp.models.Posts
import com.ainsigne.mobilesocialblogapp.models.Users
import com.ainsigne.mobilesocialblogapp.ui.main.MainActivity
import com.ainsigne.mobilesocialblogapp.utils.Config
import com.ainsigne.mobilesocialblogapp.utils.Constants
import com.ainsigne.mobilesocialblogapp.utils.UINavigation
import com.ainsigne.mobilesocialblogapp.utils.toStringFormat
import kotlinx.android.synthetic.main.fragment_feed_details.*
import org.jetbrains.anko.doAsync
import org.jetbrains.anko.uiThread
import java.util.*


/**
 * A simple [Fragment] subclass.

 * Use the [FeedDetails.newInstance] factory method to
 * create an instance of this fragment.
 *
 */
class FeedDetailsFragment : BaseFragment(), FeedDetailsView {

    var replyTo = ""

    var isCommenting = false

    var postId = ""

    var post : Posts? = null

    override fun userFrom(author: String): Users? {
        presenter?.allUsers()?.filter{ it.username == author }?.let {
            return it[0]
        }
        return null
    }

    override fun addedCommentsUpdateView() {
        presenter?.retrieveAll()
    }

    override fun retrievedAllUpdateView() {
        presenter?.allPosts()?.filter { it.id == postId }?.let {
            post = it[0]
        }


        var withComments = false

        presenter?.allComments()?.let {comments->
            if(comments.isEmpty()){
                doAsync {
                    uiThread {
                        val linearLayoutManager = LinearLayoutManager(it.context)
                        linearLayoutManager.orientation = RecyclerView.VERTICAL
                        rv_details?.layoutManager = linearLayoutManager
                        if(post != null)
                            rv_details?.adapter = CommentsAdapter(comments, post!!, it)
                    }
                }
            }
            else{
                comments.filter {  it.commentedTo != null && it.commentedTo!!.contains(postId) }?.let{ filtered ->
                    doAsync {
                        uiThread {
                            val linearLayoutManager = LinearLayoutManager(it.context)
                            linearLayoutManager.orientation = RecyclerView.VERTICAL
                            rv_details?.layoutManager = linearLayoutManager
                            if(post != null)
                                rv_details?.adapter = CommentsAdapter(filtered, post!!, it)
                        }
                    }
                }
            }
        }

    }



    /**
     * injector as FeedDetailsInjector injects the presenter with the services and data needed for the
     * implementation
     **/
    var injector = FeedDetailsInjectorImplementation()
    /**
     * presenter as FeedDetailsPresenter injected automatically to call implementations
     **/
    var presenter: FeedDetailsPresenter? = null



    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        injector.inject(this)

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_feed_details, container, false)


    }

    fun toggleCommenting()
    {
        btn_details_comment.text = "Comment on this post"
        container_details_send.visibility = View.GONE
        if(isCommenting) {
            btn_details_comment.text = "Close"
            container_details_send.visibility = View.VISIBLE
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        container_details_send.visibility = View.GONE
        arguments?.getString("postId")?.let { postId =  it }
        btn_details_comment.setOnClickListener {
            isCommenting = !isCommenting
            toggleCommenting()
        }
        btn_details_sendcomment.setOnClickListener {
            val comment = Comments()
            comment.id = Constants.getRandomString(22)
            Config.getUser()?.let {
                val user = presenter?.getUserFrom(it)
                comment.author = user?.username
                comment.userId = user?.id
            }
            comment.message = et_details_comment.text.toString()
            comment.commentedTo = post?.id
            comment.commentedToPost = post
            comment.downvotes = 0
            comment.upvotes = 0
            comment.timestamp = Date().toStringFormat()
            presenter?.sendComment(comment)
            isCommenting = !isCommenting
            toggleCommenting()
        }
        presenter?.retrieveAll()
    }


    companion object {
        /**
         * Use this factory method to create a new instance of
         * this fragment using the provided parameters.
         *
         * @return A new instance of fragment FeedDetails.
         */
        // TODO: Rename and change types and number of parameters
        @JvmStatic
        fun newInstance() =
            FeedDetailsFragment()
    }
}
