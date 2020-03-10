package com.ainsigne.mobilesocialblogapp.ui.chatsession


import android.util.Log
import com.ainsigne.mobilesocialblogapp.manager.APIManager
import com.ainsigne.mobilesocialblogapp.manager.AuthManager
import com.ainsigne.mobilesocialblogapp.models.CallRecords
import com.ainsigne.mobilesocialblogapp.models.ChatMessages
import com.ainsigne.mobilesocialblogapp.models.ChatSession
import com.ainsigne.mobilesocialblogapp.models.Users
import com.ainsigne.mobilesocialblogapp.utils.Constants
import com.ainsigne.mobilesocialblogapp.utils.fromNow
import com.ainsigne.mobilesocialblogapp.utils.toStringFormat
import kotlinx.android.synthetic.main.fragment_chat_session.*
import java.util.*
import kotlin.collections.ArrayList


/**
 * ChatSessionView interface for updating the view in the fragments
 **/
interface ChatSessionView {
    fun addedChatMessageUpdateView()
    fun addedChatSessionUpdateView()
    fun receivedCallFromUpdateView(callerName : String, conferenceName : String)
    fun retrievedAllUpdateView()
    fun currentUser() : Users?
    fun userFrom(author : String) : Users?
}

/**
 * ChatSessionContract interface for delegating implementations from the ChatSessionServices
 **/
interface ChatSessionContract {
    fun addedChatSession()
    fun addedChatMessage()
    fun retrievedAll()
    fun receivedCallFrom(callerName : String, conferenceName : String)
}

/**
 * ChatSessionPresenter interface for implementing the ChatSessionPresenter
 **/
interface ChatSessionPresenter {
    fun startCall(callRecords: CallRecords)
    fun allChats() : List<ChatMessages>?
    fun allSessions() : List<ChatSession>?
    fun allUsers() : List<Users>?
    fun sendChatWithSession(user : Users, chats : List<ChatMessages>, chatId : String, message : String)
    fun sendChat(chat : ChatMessages)
    fun sendSession(chat : ChatSession)
    fun retrieveCalls(userId : String)
    fun retrieveAll()
    fun getUserFrom(username : String) : Users?
}


/**
 * ChatSessionPresenter implementation based on the presenter protocol.
 **/
class ChatSessionPresenterImplementation(
    view: ChatSessionView, apiManager: APIManager,
    authManager: AuthManager
) : ChatSessionPresenter, ChatSessionContract {

    override fun startCall(callRecords: CallRecords) {
        service.startCall(callRecords)
    }

    override fun receivedCallFrom(callerName: String, conferenceName : String) {
        Log.d(" Call from presenter "," Call from presenter $conferenceName")
        view.receivedCallFromUpdateView(callerName, conferenceName)
    }

    override fun addedChatSession() {
        view.addedChatSessionUpdateView()
    }

    override fun addedChatMessage() {
        view.addedChatMessageUpdateView()
    }

    override fun retrievedAll() {
        view.retrievedAllUpdateView()
    }

    override fun allChats(): List<ChatMessages>? {
        return service.allChats?.toList()
    }

    override fun allSessions(): List<ChatSession>? {
        return service.allSessions?.toList()
    }

    override fun allUsers(): List<Users>? {
        return service.allUsers?.toList()
    }

    override fun sendChatWithSession(
        user: Users,
        chats: List<ChatMessages>,
        chatId: String,
        message: String
    ) {
        val chat = ChatMessages()
        chat.id = Constants.getRandomString(22)
        chat.author = user.username
        chat.userId = user.id
        chat.timestamp = Date().toStringFormat()
        chat.message = message
        chat.replyTo = chatId
        chat.timestamp_from = Date().fromNow()
        chat.msgId = chats.size

        sendChat(chat)
        var session = ChatSession()
        user.id?.let {id->
            allSessions()?.filter { it.userIds != null && it.userIds!!.contains(id) && it.userIds!!.contains(chatId) }?.let {filtered->
                if(filtered.isNotEmpty())
                    session = filtered[0]
            }
        }
        if(session.id == null){
            session.id = Constants.getRandomString(22)
            session.userIds = ArrayList()
            user.id?.let {id->
                session.userIds?.add(id)
                session.userIds?.add(chatId)
            }

        }
        session.message = message
        session.author = user.username
        session.timestamp = Date().toStringFormat()
        sendSession(session)
    }

    override fun sendChat(chat: ChatMessages) {
        service.sendChat(chat)
    }

    override fun sendSession(chat: ChatSession) {
        service.updateSession(chat)
    }

    override fun retrieveCalls(userId: String) {
        Log.d("Call retrieve presenter","Call retrieve presenter $userId")
        service.retrieveCalls(userId)
    }

    override fun retrieveAll() {
        service.retrieveAllUsers()
        service.retrieveAllChatMessages()
        service.retrieveAllChatSessions()
    }

    override fun getUserFrom(username: String): Users? {
        return service.getUserFrom(username)
    }

    var view: ChatSessionView

    var service: ChatSessionServices

    /**
     * initializes with the ChatSessionFragment as the ChatSessionView for updating
     * and authManager for consuming the authentication api and apiManager for consuming the api
     * related to data
     **/
    init {
        service = ChatSessionServices(apiManager, authManager)
        service.contract = this
        this.view = view
    }


}
