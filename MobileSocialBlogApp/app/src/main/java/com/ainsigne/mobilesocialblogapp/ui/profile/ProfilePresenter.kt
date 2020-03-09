package com.ainsigne.mobilesocialblogapp.ui.profile


import com.ainsigne.mobilesocialblogapp.manager.APIManager
import com.ainsigne.mobilesocialblogapp.manager.AuthManager
import com.ainsigne.mobilesocialblogapp.models.Comments
import com.ainsigne.mobilesocialblogapp.models.Posts
import com.ainsigne.mobilesocialblogapp.models.Users


/**
 * ProfileView interface for updating the view in the fragments
 **/
interface ProfileView {
    fun retrievedAllUpdateView()
    fun addedUserUpdateView()
    fun downloadedImageUpdateView(image : String)
    fun uploadedImageUpdateView()
    fun updatedPostUpdateView()
    fun userFrom(author : String) : Users?
    fun currentUser() : Users?
    fun upvotePost(post : Posts)
    fun downvotePost(post : Posts)
    fun navigateToPost(post: Posts)
    fun navigateToProfile(username: String)

}

/**
 * ProfileContract interface for delegating implementations from the ProfileServices
 **/
interface ProfileContract {
    fun updatedPost()
    fun retrievedAll()
    fun addedUser()
    fun downloadedImage(image : String)
    fun uploadedImage()
}

/**
 * ProfilePresenter interface for implementing the ProfilePresenter
 **/
interface ProfilePresenter {
    fun getUserFrom(username : String) : Users?
    fun retrieveAll()
    fun allPosts() : List<Posts>?
    fun allUsers() : List<Users>?
    fun allComments() : List<Comments>?
    fun sendPost(post : Posts)
    fun addFriend(photoUrl : String? , friendId : String?, userId : String?)
    fun updateUser(user: Users, toUpload: Boolean)
    fun downloadImage(username : String)
    fun uploadImage(data : String)
    fun commentsFromPost(postId : String) : List<Comments>?
    fun upvotePost(post: Posts, id : String)
    fun downvotePost(post : Posts, id : String)
}


/**
 * ProfilePresenter implementation based on the presenter protocol.
 **/
class ProfilePresenterImplementation(
    view: ProfileView, apiManager: APIManager,
    authManager: AuthManager
) : ProfilePresenter, ProfileContract {

    override fun sendPost(post: Posts) {
        service.addPostsToApi(post, false)
    }

    override fun updatedPost() {
        view.updatedPostUpdateView()
    }

    override fun retrievedAll() {
        view.retrievedAllUpdateView()
    }

    override fun addedUser() {
        view.addedUserUpdateView()
    }

    override fun downloadedImage(image: String) {
        view.downloadedImageUpdateView(image)
    }

    override fun uploadedImage() {
        view.uploadedImageUpdateView()
    }

    override fun getUserFrom(username: String): Users? {
        return service.getUserFrom(username)
    }

    override fun retrieveAll() {
        service.retrieveAllUsers()
        service.retrieveAllComments()
        service.retrieveAllPosts()

    }

    override fun allPosts(): List<Posts>? {
        return service.allPosts?.toList()
    }

    override fun allUsers(): List<Users>? {
        return service.allUsers?.toList()
    }

    override fun allComments(): List<Comments>? {
        return service.allComments?.toList()
    }

    override fun addFriend(photoUrl: String?, friendId: String?, userId: String?) {
        service.addUser(photoUrl, friendId, userId)
    }

    override fun updateUser(user: Users, toUpload: Boolean) {
        service.addUserToApi(user, toUpload)
    }

    override fun downloadImage(username: String) {
        service.downloadImage(username)
    }

    override fun uploadImage(data: String) {
        service.uploadImage(data)
    }

    override fun commentsFromPost(postId: String): List<Comments>? {
        return commentsFromPost(postId)

    }

    override fun upvotePost(post: Posts, id : String) {
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
        sendPost(post)
    }

    override fun downvotePost(post: Posts, id : String) {
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
        sendPost(post)
    }

    var view: ProfileView

    var service: ProfileServices

    /**
     * initializes with the ProfileFragment as the ProfileView for updating
     * and authManager for consuming the authentication api and apiManager for consuming the api
     * related to data
     **/
    init {
        service = ProfileServices(apiManager, authManager)
        service.contract = this
        this.view = view
    }


}
