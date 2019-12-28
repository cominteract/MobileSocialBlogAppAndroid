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
    fun retrievedAllUpdateView()
    fun userFrom(author : String) : Users?
}

/**
 * FeedDetailsContract interface for delegating implementations from the FeedDetailsServices
 **/
interface FeedDetailsContract {
    fun addedComments()
    fun retrievedAll()
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
