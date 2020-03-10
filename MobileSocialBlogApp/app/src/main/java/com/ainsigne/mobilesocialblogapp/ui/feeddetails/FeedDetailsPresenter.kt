package com.ainsigne.mobilesocialblogapp.ui.feeddetails


import android.util.Log
import com.ainsigne.mobilesocialblogapp.manager.APIManager
import com.ainsigne.mobilesocialblogapp.manager.AuthManager
import com.ainsigne.mobilesocialblogapp.models.Comments
import com.ainsigne.mobilesocialblogapp.models.Posts
import com.ainsigne.mobilesocialblogapp.models.Users


/**
 * FeedDetailsView interface for updating the view in the fragments
 **/
interface FeedDetailsView {
    fun addedCommentsUpdateView()
    fun updatedPostUpdateView()
    fun retrievedAllUpdateView()
    fun userFrom(author : String) : Users?
    fun upvotePost(post: Posts)
    fun downvotePost(post : Posts)
    fun showComment(shown : Boolean,comment: Comments)
    fun replyToComment()
}

/**
 * FeedDetailsContract interface for delegating implementations from the FeedDetailsServices
 **/
interface FeedDetailsContract {
    fun addedComments()
    fun retrievedAll()
    fun updatedPost()
}

/**
 * FeedDetailsPresenter interface for implementing the FeedDetailsPresenter
 **/
interface FeedDetailsPresenter {
    fun allComments() : List<Comments>?
    fun allUsers() : List<Users>?
    fun allPosts() : List<Posts>?
    fun retrieveAll()
    fun allowedToReply() : Boolean
    fun sendComment(comment : Comments)
    fun commentsFromPost(postId : String) : List<Comments>?
    fun getUserFrom(username : String) : Users?
    fun upvotePost(post: Posts, id : String)
    fun downvotePost(post : Posts, id : String)
}


/**
 * FeedDetailsPresenter implementation based on the presenter protocol.
 **/
class FeedDetailsPresenterImplementation(
    view: FeedDetailsView, apiManager: APIManager,
    authManager: AuthManager
) : FeedDetailsPresenter, FeedDetailsContract {
    override fun addedComments() {
        view.addedCommentsUpdateView()
    }

    override fun retrievedAll() {

        view.retrievedAllUpdateView()
    }

    override fun updatedPost() {
        view.updatedPostUpdateView()
    }

    override fun allComments(): List<Comments>? {
        return service.allComments?.toList()
    }

    override fun allUsers(): List<Users>? {
        return service.allUsers?.toList()
    }

    override fun allPosts(): List<Posts>? {
        return service.allPosts?.toList()

    }

    override fun retrieveAll() {
        service.retrieveAllUsers()
        service.retrieveAllComments()
        service.retrieveAllPosts()
    }

    override fun allowedToReply(): Boolean {
        return  service.allowedReply
    }

    override fun sendComment(comment: Comments) {
        service.addCommentsToApi(comment)
    }

    override fun commentsFromPost(postId: String): List<Comments>? {
        return service.commentsFromPost(postId)
    }

    override fun getUserFrom(username: String): Users? {
        return service.getUserFrom(username)
    }

    override fun upvotePost(post: Posts, id: String) {
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
        service.addPostsToApi(post, true)
    }

    override fun downvotePost(post: Posts, id: String) {
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
        service.addPostsToApi(post, true)
    }

    var view: FeedDetailsView

    var service: FeedDetailsServices

    /**
     * initializes with the FeedDetailsFragment as the FeedDetailsView for updating
     * and authManager for consuming the authentication api and apiManager for consuming the api
     * related to data
     **/
    init {
        service = FeedDetailsServices(apiManager, authManager)
        service.contract = this
        this.view = view
    }


}
