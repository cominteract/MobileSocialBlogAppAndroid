package com.ainsigne.mobilesocialblogapp.models

import com.ainsigne.mobilesocialblogapp.utils.Constants
import com.ainsigne.mobilesocialblogapp.utils.toStringFormat
import java.util.*
import kotlin.collections.ArrayList
import kotlin.collections.HashMap

data class ChatSession(var id : String? = null,
                       var message : String? = null,
                       var timestamp : String? = null,
                       var userIds : ArrayList<String>? = null,
                       var author : String? = null,
                       var recipient : String? = null
){
    companion object{
        fun convertToKeyVal(session : ChatSession) : HashMap<String, Any>{
            var map = HashMap<String,Any>()
            session.id?.let { map["id"] = it }
            session.author?.let { map["author"] = it }
            session.timestamp?.let { map["timestamp"] = it }
            session.userIds?.let { map["userIds"] = it }
            session.author?.let { map["author"] = it }
            session.recipient?.let { map["recipient"] = it }
            return map
        }

        fun chatSessions() : ArrayList<ChatSession>{
            var sessions = ArrayList<ChatSession>()
            for(i in 0 until 5){
                val session = ChatSession()
                session.id = Constants.getRandomString(22)
                session.userIds = ArrayList()
                session.userIds?.add(Constants.getRandomString(22))
                session.userIds?.add(Constants.getRandomString(22))
                session.author = Constants.getRandomString(7)
                session.timestamp =  Date().toStringFormat()
                session.message  = Constants.getRandomString(300)
                session.recipient = Constants.getRandomString(7)
                sessions.add(session)
            }
            return sessions
        }
    }
}