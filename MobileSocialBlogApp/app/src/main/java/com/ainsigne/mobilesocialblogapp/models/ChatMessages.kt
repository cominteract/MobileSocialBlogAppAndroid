package com.ainsigne.mobilesocialblogapp.models

import com.ainsigne.mobilesocialblogapp.utils.Constants
import com.ainsigne.mobilesocialblogapp.utils.toStringFormat
import java.util.*
import kotlin.collections.ArrayList
import kotlin.collections.HashMap

data class ChatMessages(var id : String? = null,
                        var message : String? = null,
                        var author : String? = null,
                        var timestamp : String? = null,
                        var timestamp_from : String? = null,
                        var replyTo : String? = null,
                        var userId : String? = null,
                        var msgId : Int? = null){
    companion object{
        fun convertToKeyVal(messages : ChatMessages) : HashMap<String, Any>{
            var map = HashMap<String,Any>()
            messages.id?.let { map["id"] = it }
            messages.author?.let { map["author"] = it }
            messages.message?.let { map["message"] = it }
            messages.msgId?.let { map["msgId"] = it }
            messages.replyTo?.let { map["replyTo"] = it}
            messages.timestamp?.let { map["timestamp"] = it }
            messages.timestamp_from?.let { map["timestamp_from"] = it }
            messages.userId?.let { map["userId"] = it }
            messages.author?.let { map["author"] = it }
            return map
        }

        fun chatMessages() : ArrayList<ChatMessages>{
            var messages = ArrayList<ChatMessages>()
            for(i in 0 until 5){
                val message = ChatMessages()
                message.id = Constants.getRandomString(22)
                message.userId = Constants.getRandomString(22)
                message.author = Constants.getRandomString(7)
                message.msgId = 0
                message.replyTo = Constants.getRandomString(22)
                message.timestamp =  Date().toStringFormat()
                message.timestamp_from =  Date().toStringFormat()
                message.message = Constants.getRandomString(300)
                messages.add(message)
            }
            return messages
        }
    }
}