package com.ainsigne.mobilesocialblogapp.ui.feed


import com.ainsigne.mobilesocialblogapp.manager.APIManager
import com.ainsigne.mobilesocialblogapp.manager.AuthManager
import com.ainsigne.mobilesocialblogapp.models.Comments
import com.ainsigne.mobilesocialblogapp.models.Posts
import com.ainsigne.mobilesocialblogapp.models.Users


/**
 * FeedView interface for updating the view in the fragments
 **/
interface FeedView {
    fun addedPostUpdateView()
    fun addedCommentsUpdateView()
    fun retrievedAllUpdateView()
    fun downloadedImageUpdateView(image : String)
    fun uploadedImageUpdateView()
    fun currentUser() : Users?
    fun navigateToDetails(postId : String)
    fun navigateToProfile(username: String)
    fun upvotePost(post: Posts)
    fun downvotePost(post : Posts)
    fun userFrom(author : String) : Users?
}

/**
 * FeedContract interface for delegating implementations from the FeedServices
 **/
interface FeedContract {
    fun addedPost()
    fun addedComments()
    fun retrievedAll()
    fun downloadedImage(image : String)
    fun uploadedImage()
}

/**
 * FeedPresenter interface for implementing the FeedPresenter
 **/
interface FeedPresenter {
    fun retrieveAll()
    fun allowedToPost() : Boolean
    fun allowedToComment() : Boolean
    fun allowedToReply() : Boolean
    fun downloadImage(username : String)
    fun allPosts() : List<Posts>?
    fun allUsers() : List<Users>?
    fun allComments() : List<Comments>?
    fun getUserFrom(username : String) : Users?
    fun sendPost(posts : Posts)

    fun uploadImage(data : String)
    fun uploadPostImage(data : String, postId : String)
    fun commentsFromPost(postId : String) : List<Comments>?
    fun upvotePost(post: Posts, id : String)
    fun downvotePost(post : Posts, id : String)
}


/**
 * FeedPresenter implementation based on the presenter protocol.
 **/
class FeedPresenterImplementation(
    view: FeedView, apiManager: APIManager,
    authManager: AuthManager
) : FeedPresenter, FeedContract {
    override fun addedPost() {
        view.addedPostUpdateView()
    }

    override fun addedComments() {
        view.addedCommentsUpdateView()
    }

    override fun retrievedAll() {
        service.listAllImages()
        view.retrievedAllUpdateView()
    }

    override fun downloadedImage(image: String) {
        view.downloadedImageUpdateView(image)
    }

    override fun uploadedImage() {
        view.uploadedImageUpdateView()
    }

    override fun retrieveAll() {
        service.retrieveAllUsers()
        service.retrieveAllComments()
        service.retrieveAllPosts()

    }

    override fun allowedToPost(): Boolean {
        return service.allowedPosts
    }

    override fun allowedToComment(): Boolean {
        return service.allowedComments
    }

    override fun allowedToReply(): Boolean {
        return service.allowedReply
    }

    override fun downloadImage(username: String) {
        service.downloadImage(username)
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

    override fun getUserFrom(username: String): Users? {
        return service.getUserFrom(username)
    }

    override fun sendPost(posts: Posts) {
        service.addPostsToApi(posts, true)
    }

    override fun uploadImage(data: String) {
        service.uploadImage(data)
    }

    override fun uploadPostImage(data: String, postId: String) {
        service.uploadPostImage(data,postId)
    }

    override fun commentsFromPost(postId: String): List<Comments>? {
        return service.commentsFromPost(postId)?.toList()
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
        sendPost(post)
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
        sendPost(post)
    }

    var view: FeedView

    var service: FeedServices

    /**
     * initializes with the FeedFragment as the FeedView for updating
     * and authManager for consuming the authentication api and apiManager for consuming the api
     * related to data
     **/
    init {
        service = FeedServices(apiManager, authManager)
        service.contract = this
        this.view = view
    }


}
