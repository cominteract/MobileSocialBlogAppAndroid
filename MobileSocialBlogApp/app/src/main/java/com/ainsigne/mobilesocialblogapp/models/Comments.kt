package com.ainsigne.mobilesocialblogapp.models

import com.ainsigne.mobilesocialblogapp.utils.Constants
import com.ainsigne.mobilesocialblogapp.utils.toStringFormat
import java.util.*
import kotlin.collections.ArrayList
import kotlin.collections.HashMap

data class Comments(var id : String? = null, var message : String? = null, var author : String? = null, var timestamp : String? = null, var timestamp_from : String? = null,
                    var upvotes : Long = 0, var downvotes : Long = 0, var upvotedId : ArrayList<String>? = null, var downvotedId : ArrayList<String>? = null, var commentedTo : String? = null,
                    var replyTo : String? = null, var userId : String? = null, var user : Users? = null, var replyToComment : Comments? = null, var commentedToPost : Posts? = null){
    companion object{
        fun convertToKeyVal(comments : Comments) : HashMap<String, Any>{
            var map = HashMap<String,Any>()
            comments.id?.let { map["id"] = it }
            comments.message?.let { map["message"] = it }
            comments.author?.let { map["author"] = it }
            comments.userId?.let { map["userId"] = it }
            comments.timestamp?.let { map["timestamp"] = it }
            comments.timestamp_from?.let { map["timestamp_from"] = it }
            comments.replyTo?.let { map["replyTo"] = it }
            comments.commentedTo?.let { map["commentedTo"] = it }
            comments.upvotes?.let { map["upvotes"] = it }
            comments.downvotes?.let { map["downvotes"] = it }
            comments.upvotes?.let { map["upvotes"] = it }
            comments.upvotedId?.let { map["upvotedId"] = it }
            comments.downvotedId?.let { map["downvotedId"] = it }
            return map
        }

        fun comments() : ArrayList<Comments>{
            var comments = ArrayList<Comments>()
            for(i in 0 until 5){
                val comment = Comments()
                comment.id = Constants.getRandomString(22)
                comment.author = Constants.getRandomString(6)
                comment.commentedTo = Constants.getRandomString(22)
                comment.downvotedId = ArrayList()
                comment.downvotedId?.add(Constants.getRandomString(22))
                comment.upvotedId = ArrayList()
                comment.upvotedId?.add(Constants.getRandomString(22))
                comment.downvotes = 5
                comment.message = Constants.getRandomString(300)
                comment.replyTo = Constants.getRandomString(22)
                comment.timestamp = Date().toStringFormat()
                comment.timestamp_from = Date().toStringFormat()
                comment.upvotes = 5
                comment.userId = Constants.getRandomString(22)
                comments.add(comment)
            }
            return comments
        }
    }
}