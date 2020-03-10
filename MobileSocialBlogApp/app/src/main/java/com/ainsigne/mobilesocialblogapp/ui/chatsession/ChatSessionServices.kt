package com.ainsigne.mobilesocialblogapp.ui.chatsession

import android.util.Log
import com.ainsigne.mobilesocialblogapp.R
import com.ainsigne.mobilesocialblogapp.manager.*
import com.ainsigne.mobilesocialblogapp.models.CallRecords
import com.ainsigne.mobilesocialblogapp.models.ChatMessages
import com.ainsigne.mobilesocialblogapp.models.ChatSession
import com.ainsigne.mobilesocialblogapp.models.Users

/**
 * ChatSessionServices for implementing the services needed for the presenter.
 **/
class ChatSessionServices(
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
     * ChatSessionView reference of the ChatSessionFragment
     * as ChatSessionView type. Must be weak
     **/
    var contract: ChatSessionContract? = null


    var callAPIManager : CallAPIManager = CallAPIManager()

    var allChats : ArrayList<ChatMessages>? = null

    var allSessions : ArrayList<ChatSession>? = null

    var allUsers : ArrayList<Users>? = null

    var retrievedChats = false
    var retrievedUsers = false
    var retrievedSessions = false

    fun startCall(call : CallRecords){
        callAPIManager.addCall(CallRecords.convertToKeyVal(call), completion = { err,msg ->


        })
    }

    fun retrieveCalls(userId : String){
        val retrievedCalls = object : CallsRetrieved {
            override fun retrievedCalls(callRecords: CallRecords, msg: String) {
                Log.d(" Call from service "," Call from service ${callRecords.conferenceName}")
                callRecords.callerName?.let {
                    callRecords.conferenceName?.let { conferenceName ->
                        contract?.receivedCallFrom(it, conferenceName)
                    }
                }
            }
        }
        Log.d("Call retrieve service","Call retrieve service $userId")
        callAPIManager.retrieveIncomingCall(userId, retrievedCalls)
    }

    fun retrievedAll() : Boolean {
        return retrievedChats && retrievedSessions && retrievedUsers
    }

    fun sendChat(chatMessage : ChatMessages){
        apiManager.addChats(ChatMessages.convertToKeyVal(chatMessage), completion = { err,msg ->
            if(err == null)
                contract?.addedChatMessage()
        })
    }

    fun updateSession(chatSession: ChatSession){
        apiManager.addSession(ChatSession.convertToKeyVal(chatSession), completion = { err,msg ->
            if(err == null)
                contract?.addedChatSession()
        })
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
                retrievedUsers = true
                if(retrievedAll())
                    contract?.retrievedAll()

            }
        }
        apiManager.retrieveAllUsers(usersRetrieved)
    }

    fun retrieveAllChatSessions(){
        val sessionsRetrieved = object : ChatSessionRetrieved {
            override fun retrievedChatSession(sessions: ArrayList<ChatSession>?, msg: String) {
                allSessions = sessions
                retrievedSessions = true
                if(retrievedAll())
                    contract?.retrievedAll()
            }
        }
        apiManager.retrieveAllChatSession(sessionsRetrieved)
    }

    fun retrieveAllChatMessages(){
        val chatsRetrieved = object : ChatMessagesRetrieved {
            override fun retrievedChatMessages(messages: ArrayList<ChatMessages>?, msg: String) {
                allChats = messages
                retrievedChats = true
                if(retrievedAll())
                    contract?.retrievedAll()
            }

        }
        apiManager.retrieveAllChatMessages(chatsRetrieved)
    }

}
