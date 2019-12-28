package com.ainsigne.mobilesocialblogapp.sample


import com.ainsigne.mobilesocialblogapp.manager.APIManager
import com.ainsigne.mobilesocialblogapp.manager.AuthManager
import com.ainsigne.mobilesocialblogapp.models.Comments
import com.ainsigne.mobilesocialblogapp.models.Posts
import com.ainsigne.mobilesocialblogapp.models.Users


/**
 * SampleView interface for updating the view in the fragments
 **/
interface SampleView {
    fun addedUserUpdateView()
    fun addedPostUpdateView()
    fun addedCommentsUpdateView()
}

/**
 * SampleContract interface for delegating implementations from the SampleServices
 **/
interface SampleContract {
    fun addedUser()
    fun addedPost()
    fun addedComments()
}

/**
 * SamplePresenter interface for implementing the SamplePresenter
 **/
interface SamplePresenter {
    fun addUser(photoUrl: String?, friendsId: String?, userId: String?)
    fun addPost(userId : String?, url : String?)
    fun addComment(replyTo : String?)

    fun signupUser(user : Users)
    fun sendPost(posts : Posts)
    fun sendComment(comment : Comments)

    fun usernameExists(username : String) : Boolean

    fun retrieveAll()
    fun allowedToPost() : Boolean
    fun allowedToComment() : Boolean
    fun allowedToFriend() : Boolean
    fun isAlreadyFriend(friendsId : String? , userId : String?) : Boolean
}


/**
 * SamplePresenter implementation based on the presenter protocol.
 **/
class SamplePresenterImplementation(
    view: SampleView, apiManager: APIManager,
    authManager: AuthManager
) : SamplePresenter, SampleContract {
    override fun addedUser() {
        view.addedUserUpdateView()
    }

    override fun addedPost() {
        view.addedPostUpdateView()
    }

    override fun addedComments() {
        view.addedCommentsUpdateView()
    }

    override fun addUser(photoUrl: String?, friendsId: String?, userId: String?) {
        service.addUser(photoUrl, friendsId, userId)
    }

    override fun addPost(userId: String?, url: String?) {
        service.addPosts(userId,url)
    }

    override fun addComment(replyTo: String?) {
        service.addComments(replyTo)
    }

    override fun signupUser(user: Users) {
        service.addUserToApi(user, true)
    }

    override fun sendPost(posts: Posts) {
        service.addPostsToApi(posts, true)
    }

    override fun sendComment(comment: Comments) {
        service.addCommentsToApi(comment)
    }

    override fun usernameExists(username: String): Boolean {
        return service.usernameExists(username)
    }

    override fun retrieveAll() {
        service.retrieveAllComments()
        service.retrieveAllPosts()
        service.retrieveAllUsers()
    }

    override fun allowedToPost(): Boolean {
        return service.allowedPosts
    }

    override fun allowedToComment(): Boolean {
        return service.allowedComments
    }

    override fun allowedToFriend(): Boolean {
        return service.allowedFriendRequest
    }

    override fun isAlreadyFriend(friendsId: String?, userId: String?): Boolean {
        return service.isAlreadyFriend(friendsId,userId)
    }

    var view: SampleView

    var service: SampleServices

    /**
     * initializes with the SampleFragment as the SampleView for updating
     * and authManager for consuming the authentication api and apiManager for consuming the api
     * related to data
     **/
    init {
        service = SampleServices(apiManager, authManager)
        service.contract = this
        this.view = view
    }


}
