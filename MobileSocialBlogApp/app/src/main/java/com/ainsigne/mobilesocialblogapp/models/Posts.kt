package com.ainsigne.mobilesocialblogapp.models

import com.ainsigne.mobilesocialblogapp.utils.Constants
import com.ainsigne.mobilesocialblogapp.utils.toStringFormat
import java.util.*
import kotlin.collections.ArrayList
import kotlin.collections.HashMap

data class Posts(var id : String? = null, var title : String? = null, var body : String? = null, var author : String? = null, var users : ArrayList<String>? = null,
                 var userId : String? = null, var timestamp : String? = null, var timestamp_from : String? = null, var url : String? = null, var upvotes : Long = 0,
                 var downvotes : Long = 0, var upvotedId : ArrayList<String>? = null, var downvotedId : ArrayList<String>? = null)
{
    companion object{
        fun convertToKeyVal(posts : Posts) : HashMap<String, Any>{
            var map = HashMap<String,Any>()
            posts.id?.let { map["id"] = it }
            posts.title?.let { map["title"] = it }
            posts.body?.let { map["body"] = it }
            posts.userId?.let { map["userId"] = it }
            posts.author?.let { map["author"] = it }
            posts.timestamp?.let { map["timestamp"] = it }
            posts.timestamp_from?.let { map["timestamp_from"] = it }
            posts.url?.let { map["url"] = it }
            posts.upvotes.let { map["upvotes"] = it }
            posts.downvotes.let { map["downvotes"] = it }

            posts.upvotedId?.let { map["upvotedId"] = it }
            posts.downvotedId?.let { map["downvotedId"] = it }
            return map
        }
        fun posts() : ArrayList<Posts>{
            var posts = ArrayList<Posts>()
            for(i in 0 until 5){
                val post = Posts()
                post.author = Constants.getRandomString(22)
                post.body = Constants.getRandomString(300)
                post.downvotedId = ArrayList()
                post.downvotedId?.add(Constants.getRandomString(22))
                post.upvotedId = ArrayList()
                post.upvotedId?.add(Constants.getRandomString(22))
                post.downvotes = 5
                post.upvotes = 5
                post.id = Constants.getRandomString(22)
                post.timestamp = Date().toStringFormat()
                post.timestamp_from = Date().toStringFormat()
                post.url = Constants.defaultposturl
                post.title = Constants.getRandomString(100)
                post.userId = Constants.getRandomString(22)
                posts.add(post)
            }
            return posts
        }
    }
}