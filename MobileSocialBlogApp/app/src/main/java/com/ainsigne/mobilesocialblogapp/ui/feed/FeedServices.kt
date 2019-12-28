package com.ainsigne.mobilesocialblogapp.ui.feed

import android.util.Log
import com.ainsigne.mobilesocialblogapp.R
import com.ainsigne.mobilesocialblogapp.manager.*
import com.ainsigne.mobilesocialblogapp.models.Comments
import com.ainsigne.mobilesocialblogapp.models.Posts
import com.ainsigne.mobilesocialblogapp.models.Users
import com.ainsigne.mobilesocialblogapp.utils.Config
import com.ainsigne.mobilesocialblogapp.utils.Constants
import java.io.File

/**
 * FeedServices for implementing the services needed for the presenter.
 **/
class FeedServices(
    apiManager: APIManager,
    authManager: AuthManager
) {
    /**
     * apiManager used in consuming the api related to data whether mock or from aws to be initialized
     **/
    var apiManager: APIManager = apiManager
    /**
     * authManager used in consuming the authentication api whether mock or from aws to be initialized
     **/
    var authManager: AuthManager = authManager
    /**
     * FeedView reference of the FeedFragment
     * as FeedView type. Must be weak
     **/
    var contract: FeedContract? = null

    var allowedComments = false

    var allowedPosts = false

    var allowedReply = false

    var allowedFriendRequest = false

    var allUsers : ArrayList<Users>? = null

    var allComments : ArrayList<Comments>? = null

    var allPosts : ArrayList<Posts>? = null

    fun retrievedAll() : Boolean{


        return allowedComments && allowedPosts && allowedReply
    }

    fun getUserFrom(username : String) : Users?{
        allUsers?.filter { it.username == username }?.let {
            if(it.isNotEmpty())
                return it[0]
        }
        return null
    }

    fun retrieveAllUsers(){
        val usersRetrieved = object : UsersRetrieved {
            override fun retrievedUsers(users: ArrayList<Users>?, msg: String) {
                allUsers = users
                allowedPosts = true
                allowedFriendRequest = true
                if(retrievedAll())
                    contract?.retrievedAll()
            }
        }
        apiManager.retrieveAllUsers(usersRetrieved)
    }

    fun retrieveAllPosts(){
        val postsRetrieved = object : PostsRetrieved {
            override fun retrievedPosts(posts: ArrayList<Posts>?, msg: String) {
                allPosts = posts
                allowedComments = true
                if(retrievedAll())
                    contract?.retrievedAll()
            }
        }
        apiManager.retrieveAllPosts(postsRetrieved)
    }

    fun retrieveAllComments(){
        val commentsRetrieved = object : CommentsRetrieved {
            override fun retrievedComments(comments: ArrayList<Comments>?, msg: String) {
                allComments = comments
                allowedReply = true
                if(retrievedAll())
                    contract?.retrievedAll()
            }
        }
        apiManager.retrieveAllComments(commentsRetrieved)
    }

    fun addCommentsToApi(comment : Comments){
        if(comment.id == null){
            comment.id = Constants.getRandomString(22)
            allComments?.let { comments ->
                if(!comments.filter { it.id == comment.id }.isNullOrEmpty())
                    comment.id = Constants.getRandomString(22)
            }
        }
        if(comment.author == null || comment.userId == null || comment.commentedTo == null || comment.message == null){
            Log.d(" Can't add comment ", " Can't add comment missing fields ")
            return
        }
        apiManager.addComments(Comments.convertToKeyVal(comment), completion = { err,msg ->
            if(err == null)
                contract?.addedComments()
        })
    }

    fun addPostsToApi(post : Posts , toUpload: Boolean){
        if(post.id == null){
            post.id = Constants.getRandomString(22)
            allPosts?.let { allposts ->
                if(!allposts.filter { it.id == post.id }.isNullOrEmpty())
                    post.id = Constants.getRandomString(22)
            }
        }
        if(post.author == null || post.userId == null || post.title == null)
        {
            Log.d(" Can't add post ", " Can't add post missing fields ")
            return
        }
        post.url?.let {
            if(!it.contains(Constants.firebaseurl) && !it.contains(Constants.defaultuserurl) && toUpload && File(it).exists()){
                apiManager.uploadImage(File(it), "img_${post.id!!}", completion = { err, msg ->
                    if(err == null)
                        Log.d(" Uploaded "," Uploaded ")
                })
            }
        }
        apiManager.addPosts(Posts.convertToKeyVal(post), completion = { err,msg ->
            if(err == null)
                contract?.addedPost()
        })
    }

    fun addUserToApi(aUser : Users, toUpload : Boolean){
        if(aUser.id == null){
            aUser.id = Constants.getRandomString(22)
            if(!allUsers?.filter { it.id == aUser.id }.isNullOrEmpty())
                aUser.id = Constants.getRandomString(22)
        }
        if(aUser.username == null || aUser.fullname == null || aUser.password == null){
            Log.d(" Can't signup user ", " Can't signup user missing fields ")
            return
        }
        aUser.photoUrl?.let {
            if(!it.contains(Constants.firebaseurl) && !it.contains(Constants.defaultuserurl) && toUpload && File(it).exists()){
                apiManager.uploadImage(File(it), "img_${aUser.id!!}", completion = { err,msg ->
                    if(err == null)
                        Log.d(" Uploaded "," Uploaded ")
                })
            }
        }
        apiManager.addUser(Users.convertToKeyVal(aUser) , completion = { err,msg ->

        })
    }

    fun usernameExists(username: String) : Boolean{
        allUsers?.filter { it.username == username }?.let {
            return it.isNotEmpty()
        }
        return false
    }

    fun commentsFromPost(postId : String) : List<Comments>?{

        allComments?.filter { it.commentedTo == postId }?.let {
            return it
        }
        return allComments?.toList()
    }

    fun uploadPostImage(data : String, postId : String){
        if(File(data).exists())
        {
            apiManager.uploadImage(File(data),"img_$postId", completion = { err,msg ->
                if(err == null)
                    contract?.uploadedImage()
            } )
        }

    }

    fun uploadImage(data : String){
        if(File(data).exists())
        {
            Config.getUser()?.let {
                getUserFrom(it)?.id?.let { id ->
                    apiManager.uploadImage(File(data),"img_${id}", completion = { err, msg ->
                        if(err == null)
                            contract?.uploadedImage()
                    } )
                }
            }
        }
    }

    fun downloadImage(username: String){
        getUserFrom(username)?.id?.let {
            apiManager.downloadImage("img_$it", completion = { err, msg ->
                if(err == null)
                    contract?.downloadedImage(msg)
            })
        }
    }

    fun listAllImages(){
        val referencesRetrieved = object : ReferencesRetrieved {
            override fun didRetrievePostImages(posts: ArrayList<Posts>) {
                if(posts.isNotEmpty())
                {
                    for (post in posts){
                        addPostsToApi(post, true)
                    }
                }
            }

            override fun didRetrieveUserImages(users: ArrayList<Users>) {
                if(users.isNotEmpty())
                {
                    for (user in users){
                        addUserToApi(user, true)
                    }
                }
            }

        }
        if(postsNotUploaded()) {
            val filteredPosts = allPosts?.filter { it.url != null && !it.url!!.contains(Constants.firebaseurl) && !it.url!!.contains(Constants.defaultposturl) }
            apiManager.listImagesForPosts(filteredPosts as ArrayList<Posts>, referencesRetrieved)
        }
        if(usersNotUploaded()){
            var filteredUsers = allUsers?.filter { it.photoUrl != null && !it.photoUrl!!.contains(Constants.firebaseurl) && !it.photoUrl!!.contains(Constants.defaultuserurl) }
            apiManager.listImagesForUsers(filteredUsers as ArrayList<Users>, referencesRetrieved)
        }
    }

    fun postsNotUploaded() : Boolean{
        allPosts?.filter { it.url != null && !it.url!!.contains(Constants.firebaseurl) && !it.url!!.contains(Constants.defaultposturl) }?.let {posts ->

            return posts.isNotEmpty()
        }


        Log.d(" Images Uploaded "," Images Uploaded ${allPosts?.filter { it.url != null  }?.size}")
        return false
    }

    fun usersNotUploaded() : Boolean{
        allUsers?.filter { it.photoUrl != null && !it.photoUrl!!.contains(Constants.firebaseurl) && !it.photoUrl!!.contains(Constants.defaultuserurl) }?.let {users ->
            return users.isNotEmpty()
        }
        Log.d(" Images Uploaded "," Images Uploaded ${allUsers?.filter { it.photoUrl != null && !it.photoUrl!!.contains(Constants.firebaseurl)}?.size}")
        return false
    }
}
