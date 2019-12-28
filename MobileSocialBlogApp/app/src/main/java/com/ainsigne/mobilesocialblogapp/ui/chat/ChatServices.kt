package com.ainsigne.mobilesocialblogapp.ui.chat

import com.ainsigne.mobilesocialblogapp.R
import com.ainsigne.mobilesocialblogapp.manager.APIManager
import com.ainsigne.mobilesocialblogapp.manager.AuthManager
import com.ainsigne.mobilesocialblogapp.manager.ChatSessionRetrieved
import com.ainsigne.mobilesocialblogapp.manager.UsersRetrieved
import com.ainsigne.mobilesocialblogapp.models.ChatMessages
import com.ainsigne.mobilesocialblogapp.models.ChatSession
import com.ainsigne.mobilesocialblogapp.models.Users

/**
 * ChatServices for implementing the services needed for the presenter.
 **/
class ChatServices(
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
     * ChatView reference of the ChatFragment
     * as ChatView type. Must be weak
     **/
    var contract: ChatContract? = null

    var retrievedSessions = false

    var retrievedUsers = false

    var allChats : ArrayList<ChatMessages>? = null

    var allSessions : ArrayList<ChatSession>? = null

    var allUsers : ArrayList<Users>? = null

    fun retrievedAll() : Boolean{
        return retrievedUsers && retrievedSessions
    }

    fun retrieveAllUsers(){
        val usersRetrieved = object : UsersRetrieved {
            override fun retrievedUsers(users: ArrayList<Users>?, msg: String) {
                allUsers = users
                retrievedUsers = true

            }
        }
        apiManager.retrieveAllUsers(usersRetrieved)
    }

    fun retrieveAllChatSessions(){
        val sessionsRetrieved = object : ChatSessionRetrieved {
            override fun retrievedChatSession(sessions: ArrayList<ChatSession>?, msg: String) {
                allSessions = sessions
                retrievedSessions = true
                contract?.retrievedAll()
            }
        }
        apiManager.retrieveAllChatSession(sessionsRetrieved)
    }
    fun getUserFrom(username : String) : Users?{
        allUsers?.filter { it.username == username }?.let {
            if(it.isNotEmpty())
                return it[0]
        }
        return null
    }

}
