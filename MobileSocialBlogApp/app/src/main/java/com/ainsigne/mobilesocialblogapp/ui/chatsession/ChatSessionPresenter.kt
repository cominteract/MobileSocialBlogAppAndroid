package com.ainsigne.mobilesocialblogapp.ui.chatsession


import com.ainsigne.mobilesocialblogapp.manager.APIManager
import com.ainsigne.mobilesocialblogapp.manager.AuthManager
import com.ainsigne.mobilesocialblogapp.models.ChatMessages
import com.ainsigne.mobilesocialblogapp.models.ChatSession
import com.ainsigne.mobilesocialblogapp.models.Users


/**
 * ChatSessionView interface for updating the view in the fragments
 **/
interface ChatSessionView {
    fun addedChatMessageUpdateView()
    fun addedChatSessionUpdateView()
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
}

/**
 * ChatSessionPresenter interface for implementing the ChatSessionPresenter
 **/
interface ChatSessionPresenter {
    fun allChats() : List<ChatMessages>?
    fun allSessions() : List<ChatSession>?
    fun sendChat(chat : ChatMessages)
    fun sendSession(chat : ChatSession)
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

    override fun sendChat(chat: ChatMessages) {
        service.sendChat(chat)
    }

    override fun sendSession(chat: ChatSession) {
        service.updateSession(chat)
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
