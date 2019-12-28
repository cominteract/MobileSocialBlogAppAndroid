package com.ainsigne.mobilesocialblogapp.ui.chat


import com.ainsigne.mobilesocialblogapp.manager.APIManager
import com.ainsigne.mobilesocialblogapp.manager.AuthManager
import com.ainsigne.mobilesocialblogapp.models.ChatSession
import com.ainsigne.mobilesocialblogapp.models.Users


/**
 * ChatView interface for updating the view in the fragments
 **/
interface ChatView {
    fun retrievedAllUpdateView()
    fun userFrom(author : String) : Users?
    fun currentUser() : Users?
    fun navigateToSession(chatId : String)

}

/**
 * ChatContract interface for delegating implementations from the ChatServices
 **/
interface ChatContract {
    fun retrievedAll()
}

/**
 * ChatPresenter interface for implementing the ChatPresenter
 **/
interface ChatPresenter {
    fun allUsers() : List<Users>?
    fun allSessions() : List<ChatSession>?
    fun retrieveAll()
    fun getUserFrom(username : String) : Users?
}


/**
 * ChatPresenter implementation based on the presenter protocol.
 **/
class ChatPresenterImplementation(
    view: ChatView, apiManager: APIManager,
    authManager: AuthManager
) : ChatPresenter, ChatContract {
    override fun retrievedAll() {
        view.retrievedAllUpdateView()
    }

    override fun allUsers(): List<Users>? {
        return service.allUsers?.toList()
    }

    override fun allSessions(): List<ChatSession>? {
        return service.allSessions?.toList()
    }

    override fun retrieveAll() {
        service.retrieveAllUsers()
        service.retrieveAllChatSessions()
    }

    override fun getUserFrom(username: String): Users? {
        return service.getUserFrom(username)
    }

    var view: ChatView

    var service: ChatServices

    /**
     * initializes with the ChatFragment as the ChatView for updating
     * and authManager for consuming the authentication api and apiManager for consuming the api
     * related to data
     **/
    init {
        service = ChatServices(apiManager, authManager)
        service.contract = this
        this.view = view
    }


}
