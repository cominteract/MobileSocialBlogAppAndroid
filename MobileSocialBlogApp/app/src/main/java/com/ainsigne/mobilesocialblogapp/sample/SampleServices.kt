package com.ainsigne.mobilesocialblogapp.sample

import android.util.Log
import com.ainsigne.mobilesocialblogapp.R
import com.ainsigne.mobilesocialblogapp.manager.*
import com.ainsigne.mobilesocialblogapp.models.Comments
import com.ainsigne.mobilesocialblogapp.models.Posts
import com.ainsigne.mobilesocialblogapp.models.Users
import com.ainsigne.mobilesocialblogapp.utils.Constants
import com.ainsigne.mobilesocialblogapp.utils.UsersConversion
import java.io.File
import java.util.*
import kotlin.collections.ArrayList

/**
 * SampleServices for implementing the services needed for the presenter.
 **/
class SampleServices(
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
     * SampleView reference of the SampleFragment
     * as SampleView type. Must be weak
     **/
    var contract: SampleContract? = null


    var allowedComments = false

    var allowedPosts = false

    var allowedReply = false

    var allowedFriendRequest = false

    var allUsers : ArrayList<Users>? = null

    var allComments : ArrayList<Comments>? = null

    var allPosts : ArrayList<Posts>? = null


    fun getRandomString(length: Int) : String {
        val allowedChars = "ABCDEFGHIJKLMNOPQRSTUVWXTZabcdefghiklmnopqrstuvwxyz0123456789"
        return (1..length)
            .map { allowedChars.random() }
            .joinToString("")
    }

    fun isAlreadyFriend(friendsId : String?, userId : String?) : Boolean{
        allUsers?.let  { users ->
            if(users.any { it.id == userId } && users.any { it.id == friendsId }){
                    val aUser = users.filter { it.id == userId }[0]
                    val bUser = users.filter { it.id == friendsId }[0]
                    val friended = (aUser.friendsId != null && bUser.friendsId != null && aUser.friendsId!!.contains(friendsId) && bUser.friendsId!!.contains(userId))
                    return friended
            }
        }
        return false
    }

    fun addUserToApi(aUser : Users, toUpload : Boolean){
        if(aUser.id == null){
            aUser.id = getRandomString(22)
            if(!allUsers?.filter { it.id == aUser.id }.isNullOrEmpty())
                aUser.id = getRandomString(22)
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
            if(err == null)
                contract?.addedUser()
        })
    }

    fun addUser(photoUrl : String?, friendsId: String?, userId: String?) {
        var aUser = Users()
        allUsers?.let { users ->
            if (friendsId != null && userId != null && !users.filter { it.id == userId }.isNullOrEmpty()) {
                aUser = users.filter { it.id == userId }[0]
                if (!users.filter { it.id == friendsId }.isNullOrEmpty()) {
                    val bUser = users.filter { it.id == friendsId }[0]
                    if (aUser.friendsId == null)
                        aUser.friendsId = ArrayList()
                    if (bUser.friendsId == null)
                        bUser.friendsId = ArrayList()
                    bUser.id?.let {
                        aUser.friendsId!!.add(it)
                    }
                    aUser.id?.let {
                        bUser.friendsId!!.add(it)
                    }
                    addUserToApi(aUser, false)
                    addUserToApi(bUser, false)


                } else if (friendsId == null && !users.filter { it.id == userId }.isNullOrEmpty()) {
                    aUser = users.filter { it.id == userId }[0]
                    aUser.photoUrl =
                        "https://alumni.crg.eu/sites/default/files/default_images/default-picture_0_0.png"
                    photoUrl?.let {
                        aUser.photoUrl = it
                    }
                    addUserToApi(aUser, true)
                } else {
                    aUser.firstname = getRandomString(6)
                    aUser.lastname = getRandomString(6)
                    aUser.id = getRandomString(22)
                    aUser.fullname = "${aUser.firstname} ${aUser.lastname}"
                    aUser.password = getRandomString(16)
                    aUser.friendsId = ArrayList<String>()
                    aUser.photoUrl =
                        "https://alumni.crg.eu/sites/default/files/default_images/default-picture_0_0.png"
                    photoUrl?.let {
                        aUser.photoUrl = it
                    }
                    aUser.username = getRandomString(10)
                    aUser.timestampCreated = Date().toString()
                    val userConversion = object : UsersConversion {
                        override fun convertedUser(users: Users?) {
                            if (users == null)
                                addUserToApi(aUser, true)
                            else {
                                aUser.id = getRandomString(22)
                                addUserToApi(aUser, true)
                            }

                        }
                    }
                    aUser.username?.let {
                        apiManager.getUser(it, userConversion)
                    }

                }
            }
        }
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
                }
            }
            apiManager.retrieveAllUsers(usersRetrieved)
        }

        fun retrieveAllPosts(){
            val postsRetrieved = object : PostsRetrieved {
                override fun retrievedPosts(posts: ArrayList<Posts>?, msg: String) {
                    allPosts = posts
                    allowedComments = true
                }
            }
            apiManager.retrieveAllPosts(postsRetrieved)
        }

        fun retrieveAllComments(){
            val commentsRetrieved = object : CommentsRetrieved{
                override fun retrievedComments(comments: ArrayList<Comments>?, msg: String) {
                    allComments = comments
                    allowedReply = true
                }
            }
            apiManager.retrieveAllComments(commentsRetrieved)
        }

        fun addCommentsToApi(comment : Comments){
            if(comment.id == null){
                comment.id = getRandomString(22)
                allComments?.let { comments ->
                    if(!comments.filter { it.id == comment.id }.isNullOrEmpty())
                        comment.id = getRandomString(22)
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

        fun addComments(replyTo : String?){
            if(allPosts == null){
                val postsRetrieved = object : PostsRetrieved {
                    override fun retrievedPosts(posts: ArrayList<Posts>?, msg: String) {
                        allPosts = posts
                        addCommentsFromPosts(replyTo)
                    }
                }
                apiManager.retrieveAllPosts(postsRetrieved)
            }
            else{
                addCommentsFromPosts(replyTo)
            }
        }

        fun addCommentsFromPosts(replyTo : String?){
            allPosts?.let { posts -> allUsers?.let { users ->
                if(posts.isNotEmpty() && users.isNotEmpty() && allowedComments){
                    val rd = Random().nextInt(posts.size - 1)
                    val r = Random().nextInt(users.size - 1)
                    val comment = Comments()
                    comment.id = getRandomString(22)
                    allComments?.filter { it.id == comment.id }?.let {
                        if(it.isNotEmpty())
                            comment.id = getRandomString(22)
                    }
                    comment.author = users[r].username
                    comment.commentedToPost = posts[rd]
                    comment.commentedTo = posts[rd].id
                    comment.timestamp = Date().toString()
                    comment.timestamp_from = Date().toString()
                    comment.upvotes = 0
                    comment.downvotes = 0
                    replyTo?.let { reply ->
                        allComments?.filter { it.id == reply }?.let { comments ->
                                if(comments.isNotEmpty() && allowedReply)
                                {
                                    comment.replyTo = reply
                                    comment.replyToComment = comments[0]
                                }
                            }
                        }
                    addCommentsToApi(comment)
                    }

                }
            }
        }

    fun addPostsToApi(post : Posts , toUpload: Boolean){
        if(post.id == null){
            post.id = getRandomString(22)
            allPosts?.let { allposts ->
                if(!allposts.filter { it.id == post.id }.isNullOrEmpty())
                    post.id = getRandomString(22)
            }
        }
        if(post.author == null || post.userId == null || post.title == null)
        {
            Log.d(" Can't add post ", " Can't add post missing fields ")
            return
        }
        post.url?.let {
            if(!it.contains(Constants.firebaseurl) && !it.contains(Constants.defaultuserurl) && toUpload && File(it).exists()){
                apiManager.uploadImage(File(it), "img_${post.id!!}", completion = { err,msg ->
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

    fun addPostsFromUsers(userId: String?, url : String?){
        allUsers?.let {users ->
            if(users.isNotEmpty() && allowedPosts){
                val rd = Random().nextInt(users.size - 1)
                val post = Posts()
                post.id = getRandomString(22)
                allPosts?.filter { it.id == post.id }?.let {
                    if(it.isNotEmpty())
                    post.id = getRandomString(22)
                }
                if(!users.filter { it.id == userId }.isNullOrEmpty()){
                    val user = users.filter { it.id == userId }[0]
                    post.author = user.username
                    post.userId = user.id
                }
                else{
                    post.author = users[rd].username
                    post.userId = users[rd].id
                }
                post.body = getRandomString(250)
                post.downvotes = 0
                post.upvotes = 0
                post.timestamp = Date().toString()
                post.timestamp_from = Date().toString()
                post.url = Constants.defaultposturl
                url?.let{
                    post.url = it
                }
                addPostsToApi(post, true)
            }
        }
    }

    fun addPosts(userId: String?, url: String?){
        if(allUsers == null){
            val usersRetrieved = object : UsersRetrieved {
                override fun retrievedUsers(users: ArrayList<Users>?, msg: String) {
                    allUsers = users
                    addPostsFromUsers(userId, url)
                }
            }
            apiManager.retrieveAllUsers(usersRetrieved)
        }
        else{
            addPostsFromUsers(userId, url)
        }
    }

    fun usernameExists(username: String) : Boolean{
        allUsers?.filter { it.username == username }?.let {
            return it.isNotEmpty()
        }
        return false
    }
}
