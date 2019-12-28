package com.ainsigne.mobilesocialblogapp.utils

import com.ainsigne.mobilesocialblogapp.models.*
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.ValueEventListener

interface UsersConversion {
    fun convertedUser( users  : Users?)
}

interface PostsConversion {
    fun convertedPosts( posts : Posts?)
}

interface CommentsConversion {
    fun convertedComments( comments: Comments?)
}

interface ChatMessagesConversion {
    fun convertedChatMessages( messages: ChatMessages?)
}

interface ChatSessionConversion {
    fun convertedChatSessions( sessions: ChatSession?)
}

class Conversions {

    companion object{
        fun convertToPosts(ref : DatabaseReference, conversion : PostsConversion){
            ref.addValueEventListener(object : ValueEventListener{
                override fun onDataChange(snapshot: DataSnapshot) {
                    val posts = Posts()
                    val value = snapshot.value as? HashMap<String,Any>
                    if(!snapshot.exists())
                    {
                        conversion.convertedPosts(null)
                        return
                    }
                    value?.let { posts.id = it["id"] as? String }
                    value?.let { posts.author = it["author"] as? String }
                    value?.let { posts.title = it["title"] as? String }
                    value?.let { posts.body = it["body"] as? String }
                    value?.let { posts.url = it["url"] as? String }
                    value?.let { posts.timestamp = it["timestamp"] as? String }
                    value?.let { posts.timestamp_from = it["timestamp_from"] as? String }
                    value?.let {
                        if(it["upvotes"] is Int){
                            posts.upvotes = it["upvotes"] as Long
                        }
                    }
                    value?.let {
                        if(it["downvotes"] is Int){
                            posts.downvotes = it["downvotes"] as Long
                        }
                    }
                    value?.let { posts.upvotedId = it["upvotedId"] as? ArrayList<String> }
                    value?.let { posts.downvotedId = it["downvotedId"] as? ArrayList<String> }
                    conversion.convertedPosts(posts)
                }

                override fun onCancelled(p0: DatabaseError) {

                }

            })
        }

        fun convertToAllPosts(map : HashMap<String, Any>) : ArrayList<Posts>{
            var posts = ArrayList<Posts>()
            for(key in map.keys){
                if(map[key] is HashMap<*,*>)
                {
                    val value = map[key] as HashMap<String,Any>
                    val post = Posts()
                    post.id = value["id"] as? String
                    post.author = value["author"] as? String
                    post.title = value["title"] as? String
                    post.body = value["body"] as? String
                    post.timestamp = value["timestamp"] as? String
                    post.timestamp_from = value["timestamp_from"] as? String
                    post.url = value["url"] as? String
                    post.upvotedId = value["upvotedId"] as? ArrayList<String>?
                    post.downvotedId = value["downvotedId"] as? ArrayList<String>?
                    post.userId = value["userId"] as? String
                    value["upvotes"]?.let {
                        post.upvotes = it as Long
                    }
                    value["downvotes"]?.let {
                        post.downvotes = it as Long
                    }
                    posts.add(post)
                }
            }
            return posts
        }


        fun convertToUsers(ref : DatabaseReference, conversion : UsersConversion){
            ref.addValueEventListener(object : ValueEventListener{
                override fun onDataChange(snapshot: DataSnapshot) {
                    val users = Users()
                    val value = snapshot.value as? HashMap<String,Any>
                    if(!snapshot.exists())
                    {
                        conversion.convertedUser(null)
                        return
                    }
                    value?.let { users.id = it["id"] as? String }
                    value?.let { users.fullname = it["full"] as? String }
                    value?.let { users.firstname = it["first"] as? String }
                    value?.let { users.lastname = it["last"] as? String }
                    value?.let { users.photoUrl = it["photoUrl"] as? String }
                    value?.let { users.timestampCreated = it["timestampCreated"] as? String }
                    value?.let { users.username = it["username"] as? String }
                    value?.let { users.password = it["password"] as? String }
                    value?.let { users.location = it["location"] as? String }
                    value?.let { users.birthday = it["birthday"] as? String }
                    value?.let { users.online = it["online"] as? Boolean }
                    value?.let { users.friendsId = it["friendsId"] as? ArrayList<String> }
                    value?.let { users.friendsInviteid = it["friendsInviteid"] as? ArrayList<String> }
                    value?.let { users.friendsRequestId = it["friendsRequestId"] as? ArrayList<String> }
                    conversion.convertedUser(users)
                }

                override fun onCancelled(p0: DatabaseError) {

                }

            })
        }

        fun convertToAllUsers(map : HashMap<String, Any>) : ArrayList<Users>{
            var users = ArrayList<Users>()
            for(key in map.keys){
                if(map[key] is HashMap<*,*>)
                {
                    val value = map[key] as HashMap<String,Any>
                    val user = Users()
                    user.id = value["id"] as? String
                    user.fullname = value["full"] as? String
                    user.firstname = value["first"] as? String
                    user.lastname = value["last"] as? String
                    user.timestampCreated = value["timestampCreated"] as? String
                    user.location = value["location"] as? String
                    user.photoUrl = value["photoUrl"] as? String
                    user.birthday = value["birthday"] as? String
                    user.username = value["username"] as? String
                    user.password = value["password"] as? String
                    user.friendsInviteid = value["friendsInviteid"] as? ArrayList<String>
                    user.friendsId = value["friendsId"] as? ArrayList<String>
                    user.friendsRequestId = value["friendsRequestId"] as? ArrayList<String>
                    user.online = value["online"] as? Boolean
                    users.add(user)
                }
            }
            return users
        }


        fun convertToComments(ref : DatabaseReference, conversion : CommentsConversion){
            ref.addValueEventListener(object : ValueEventListener{
                override fun onDataChange(snapshot: DataSnapshot) {
                    val comments = Comments()
                    val value = snapshot.value as? HashMap<String,Any>
                    if(!snapshot.exists())
                    {
                        conversion.convertedComments(null)
                        return
                    }
                    value?.let { comments.id = it["id"] as? String }
                    value?.let { comments.author = it["author"] as? String }
                    value?.let { comments.message = it["message"] as? String }
                    value?.let { comments.replyTo = it["replyTo"] as? String }
                    value?.let { comments.commentedTo = it["commentedTo"] as? String }
                    value?.let { comments.userId = it["userId"] as? String }
                    value?.let { comments.timestamp = it["timestamp"] as? String }
                    value?.let { comments.timestamp_from = it["timestamp_from"] as? String }
                    value?.let {
                        if(it["upvotes"] is Int){
                            comments.upvotes = it["upvotes"] as Long
                        }
                    }
                    value?.let {
                        if(it["downvotes"] is Int){
                            comments.downvotes = it["downvotes"] as Long
                        }
                    }
                    value?.let { comments.upvotedId = it["upvotedId"] as? ArrayList<String> }
                    value?.let { comments.downvotedId = it["downvotedId"] as? ArrayList<String> }
                    conversion.convertedComments(comments)
                }

                override fun onCancelled(p0: DatabaseError) {

                }

            })
        }

        fun convertToAllComments(map : HashMap<String, Any>) : ArrayList<Comments>{
            var comments = ArrayList<Comments>()
            for(key in map.keys){
                if(map[key] is HashMap<*,*>)
                {
                    val value = map[key] as HashMap<String,Any>
                    val comment = Comments()
                    comment.id = value["id"] as? String
                    comment.author = value["author"] as? String
                    comment.message = value["message"] as? String
                    comment.replyTo = value["replyTo"] as? String
                    comment.commentedTo = value["commentedTo"] as? String
                    comment.timestamp = value["timestamp"] as? String
                    comment.timestamp_from = value["timestamp_from"] as? String
                    comment.userId = value["userId"] as? String
                    comment.upvotedId = value["upvotedId"] as? ArrayList<String>
                    comment.downvotedId = value["downvotedId"] as? ArrayList<String>
                    comment.userId = value["userId"] as? String
                    value["upvotes"].let {
                        comment.upvotes = it as Long
                    }
                    value["downvotes"].let {
                        comment.downvotes = it as Long
                    }
                    comments.add(comment)
                }
            }
            return comments
        }


        fun convertToChatSession(ref : DatabaseReference, conversion : ChatSessionConversion){
            ref.addValueEventListener(object : ValueEventListener{
                override fun onDataChange(snapshot: DataSnapshot) {
                    val session = ChatSession()
                    val value = snapshot.value as? HashMap<String,Any>
                    if(!snapshot.exists())
                    {
                        conversion.convertedChatSessions(null)
                        return
                    }
                    value?.let { session.id = it["id"] as? String }
                    value?.let { session.author = it["author"] as? String }
                    value?.let { session.message = it["message"] as? String }
                    value?.let { session.timestamp = it["timestamp"] as? String }
                    value?.let { session.userIds = it["userIds"] as? ArrayList<String> }
                    conversion.convertedChatSessions(session)
                }

                override fun onCancelled(p0: DatabaseError) {

                }

            })
        }

        fun convertToAllChatSession(map : HashMap<String, Any>) : ArrayList<ChatSession>{
            var sessions = ArrayList<ChatSession>()
            for(key in map.keys){
                if(map[key] is HashMap<*,*>)
                {
                    val value = map[key] as HashMap<String,Any>
                    val session = ChatSession()
                    session.id = value["id"] as? String
                    session.author = value["author"] as? String
                    session.message = value["title"] as? String
                    session.timestamp = value["timestamp"] as? String
                    session.userIds = value["userIds"] as? ArrayList<String>?

                    sessions.add(session)
                }
            }
            return sessions
        }

        fun convertToChatMessages(ref : DatabaseReference, conversion : ChatMessagesConversion){
            ref.addValueEventListener(object : ValueEventListener{
                override fun onDataChange(snapshot: DataSnapshot) {
                    val chatmessage = ChatMessages()
                    val value = snapshot.value as? HashMap<String,Any>
                    if(!snapshot.exists())
                    {
                        conversion.convertedChatMessages(null)
                        return
                    }
                    value?.let { chatmessage.id = it["id"] as? String }
                    value?.let { chatmessage.author = it["author"] as? String }
                    value?.let { chatmessage.message = it["message"] as? String }
                    value?.let { chatmessage.timestamp = it["timestamp"] as? String }
                    value?.let { chatmessage.userId = it["userId"] as? String }
                    value?.let { chatmessage.replyTo = it["replyTo"] as? String }
                    value?.let { chatmessage.msgId = it["msgId"] as? Int }
                    value?.let { chatmessage.timestamp_from = it["timestamp_from"] as? String }

                    conversion.convertedChatMessages(chatmessage)
                }

                override fun onCancelled(p0: DatabaseError) {

                }

            })
        }

        fun convertToAllChatMessages(map : HashMap<String, Any>) : ArrayList<ChatMessages>{
            var messages = ArrayList<ChatMessages>()
            for(key in map.keys){
                if(map[key] is HashMap<*,*>)
                {
                    val value = map[key] as HashMap<String,Any>
                    val message = ChatMessages()
                    message.id = value["id"] as? String
                    message.author = value["author"] as? String
                    message.message = value["message"] as? String
                    message.timestamp = value["timestamp"] as? String
                    message.timestamp_from = value["timestamp"] as? String
                    message.userId = value["userId"] as? String
                    message.replyTo = value["replyTo"] as? String
                    message.msgId = value["msgId"] as? Int


                    messages.add(message)
                }
            }
            return messages
        }

    }
}