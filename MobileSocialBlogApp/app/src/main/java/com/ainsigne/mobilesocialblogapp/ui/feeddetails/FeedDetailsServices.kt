package com.ainsigne.mobilesocialblogapp.ui.feeddetails

import android.util.Log
import com.ainsigne.mobilesocialblogapp.R
import com.ainsigne.mobilesocialblogapp.manager.*
import com.ainsigne.mobilesocialblogapp.models.Comments
import com.ainsigne.mobilesocialblogapp.models.Posts
import com.ainsigne.mobilesocialblogapp.models.Users
import com.ainsigne.mobilesocialblogapp.utils.Constants

/**
 * FeedDetailsServices for implementing the services needed for the presenter.
 **/
class FeedDetailsServices(
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
     * FeedDetailsView reference of the FeedDetailsFragment
     * as FeedDetailsView type. Must be weak
     **/
    var contract: FeedDetailsContract? = null

    var allUsers : ArrayList<Users>? = null

    var allComments : ArrayList<Comments>? = null

    var allowedReply = false

    var allowedComments = false

    var allPosts : ArrayList<Posts>? = null

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

    fun commentsFromPost(postId : String) : List<Comments>?{

        allComments?.filter { it.commentedTo == postId }?.let {
            return it
        }
        return allComments?.toList()
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
}
