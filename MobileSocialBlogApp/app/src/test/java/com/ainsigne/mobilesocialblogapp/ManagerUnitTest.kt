package com.ainsigne.mobilesocialblogapp

import com.ainsigne.mobilesocialblogapp.manager.*
import com.ainsigne.mobilesocialblogapp.models.*
import org.junit.Test

import org.junit.Assert.*

/**
 * Example local unit test, which will execute on the development machine (host).
 *
 * See [testing documentation](http://d.android.com/tools/testing).
 */
class ManagerUnitTest {
    val apiManager = MockAPIManager()
    @Test
    fun addChatTest() {
        apiManager.addChats(ChatMessages.convertToKeyVal(ChatMessages.chatMessages()[0]), completion = {
            err,msg ->
            if(err == null)
                assert(true)
            else
                assert(false)
        })
    }

    @Test
    fun addUserTest() {
        apiManager.addUser(Users.convertToKeyVal(Users.users()[0]), completion = {
                err,msg ->
            if(err == null)
                assert(true)
            else
                assert(false)
        })
    }

    @Test
    fun addSessionTest() {
        apiManager.addSession(ChatSession.convertToKeyVal(ChatSession.chatSessions()[0]), completion = {
                err,msg ->
            if(err == null)
                assert(true)
            else
                assert(false)
        })
    }

    @Test
    fun addCommentTest() {
        apiManager.addComments(Comments.convertToKeyVal(Comments.comments()[0]), completion = {
                err,msg ->
            if(err == null)
                assert(true)
            else
                assert(false)
        })
    }

    @Test
    fun addPostTest() {
        apiManager.addPosts(Posts.convertToKeyVal(Posts.posts()[0]), completion = {
                err,msg ->
            if(err == null)
                assert(true)
            else
                assert(false)
        })
    }

    @Test
    fun retrieveChats() {
        val chatMessagesRetrieved = object : ChatMessagesRetrieved{
            override fun retrievedChatMessages(messages: ArrayList<ChatMessages>?, msg: String) {
                assert(true)
            }
        }
        apiManager.retrieveAllChatMessages(chatMessagesRetrieved)
    }

    @Test
    fun retrieveChatSessions(){
        val chatsessionsRetrieved = object : ChatSessionRetrieved{
            override fun retrievedChatSession(sessions: ArrayList<ChatSession>?, msg: String) {
                assert(true)
            }
        }
        apiManager.retrieveAllChatSession(chatsessionsRetrieved)
    }

    @Test
    fun retrieveUsers(){
        val usersRetrieved = object : UsersRetrieved{
            override fun retrievedUsers(users: ArrayList<Users>?, msg: String) {
                assert(true)
            }
        }
        apiManager.retrieveAllUsers(usersRetrieved)
    }

    @Test
    fun retrievePosts(){
        val postsRetrieved = object : PostsRetrieved{
            override fun retrievedPosts(posts: ArrayList<Posts>?, msg: String) {
                assert(true)
            }
        }
        apiManager.retrieveAllPosts(postsRetrieved)
    }


    @Test
    fun retrieveComments(){
        val commentsRetrieved = object : CommentsRetrieved{
            override fun retrievedComments(comments: ArrayList<Comments>?, msg: String) {
                assert(true)
            }
        }
        apiManager.retrieveAllComments(commentsRetrieved)
    }



}
