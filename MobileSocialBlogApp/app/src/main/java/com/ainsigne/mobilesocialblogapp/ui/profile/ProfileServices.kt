package com.ainsigne.mobilesocialblogapp.ui.profile

import android.util.Log
import com.ainsigne.mobilesocialblogapp.R
import com.ainsigne.mobilesocialblogapp.manager.*
import com.ainsigne.mobilesocialblogapp.models.Comments
import com.ainsigne.mobilesocialblogapp.models.Posts
import com.ainsigne.mobilesocialblogapp.models.Users
import com.ainsigne.mobilesocialblogapp.utils.Config
import com.ainsigne.mobilesocialblogapp.utils.Constants
import com.ainsigne.mobilesocialblogapp.utils.UsersConversion
import java.io.File
import java.util.*
import kotlin.collections.ArrayList

/**
 * ProfileServices for implementing the services needed for the presenter.
 **/
class ProfileServices(
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
     * ProfileView reference of the ProfileFragment
     * as ProfileView type. Must be weak
     **/
    var contract: ProfileContract? = null

    var allUsers : ArrayList<Users>? = null

    var allComments : ArrayList<Comments>? = null

    var allPosts : ArrayList<Posts>? = null

    var allowedComments = false

    var allowedReply = false

    fun retrievedAll() : Boolean{
        return allowedReply && allowedComments
    }

    fun getUserFrom(username : String) : Users?{
        allUsers?.filter { it.username == username }?.let {
            if(it.isNotEmpty())
                return it[0]
        }
        return null
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

    fun retrieveAllUsers(){
        val usersRetrieved = object : UsersRetrieved {
            override fun retrievedUsers(users: ArrayList<Users>?, msg: String) {
                allUsers = users
                if(retrievedAll())
                    contract?.retrievedAll()
            }
        }
        apiManager.retrieveAllUsers(usersRetrieved)
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

    fun commentsFromPost(postId : String) : List<Comments>?{

        allComments?.filter { it.commentedTo == postId }?.let {
            return it
        }
        return allComments?.toList()
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
                contract?.updatedPost()
        })
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
                }
            }
        }
    }
}
