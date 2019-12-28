package com.ainsigne.mobilesocialblogapp.ui.discover


import com.ainsigne.mobilesocialblogapp.manager.APIManager
import com.ainsigne.mobilesocialblogapp.manager.AuthManager
import com.ainsigne.mobilesocialblogapp.models.Users


/**
 * DiscoverView interface for updating the view in the fragments
 **/
interface DiscoverView {
    fun updatedUsersUpdateView()
    fun retrievedAllUpdateView()
    fun cancelFriendClicked(bUser: Users)
    fun addFriendClicked(bUser : Users)
    fun acceptFriendClicked(bUser: Users)

    fun isAlreadyFriend(bUser : Users) : Boolean
    fun isAlreadyRequested(bUser : Users) : Boolean
    fun isAlreadyInvited(bUser : Users) : Boolean

}

/**
 * DiscoverContract interface for delegating implementations from the DiscoverServices
 **/
interface DiscoverContract {
    fun retrievedAll()
    fun updatedUsers()
}

/**
 * DiscoverPresenter interface for implementing the DiscoverPresenter
 **/
interface DiscoverPresenter {
    fun updateUsers(aUser : Users, bUser : Users)
    fun retrieveAll()
    fun allUsers() : List<Users>?
    fun getUserFrom(username : String) : Users?
}


/**
 * DiscoverPresenter implementation based on the presenter protocol.
 **/
class DiscoverPresenterImplementation(
    view: DiscoverView, apiManager: APIManager,
    authManager: AuthManager
) : DiscoverPresenter, DiscoverContract {
    override fun retrievedAll() {
        view.retrievedAllUpdateView()
    }

    override fun updatedUsers() {
        view.updatedUsersUpdateView()
    }

    override fun updateUsers(aUser: Users, bUser: Users) {
        service.addUserToApi(aUser, false)
        service.addUserToApi(bUser, false)
    }

    override fun retrieveAll() {
        service.retrieveAllUsers()
    }

    override fun allUsers(): List<Users>? {
        return service.allUsers?.toList()
    }

    override fun getUserFrom(username: String): Users? {
        return  service.getUserFrom(username)
    }

    var view: DiscoverView

    var service: DiscoverServices

    /**
     * initializes with the DiscoverFragment as the DiscoverView for updating
     * and authManager for consuming the authentication api and apiManager for consuming the api
     * related to data
     **/
    init {
        service = DiscoverServices(apiManager, authManager)
        service.contract = this
        this.view = view
    }


}
