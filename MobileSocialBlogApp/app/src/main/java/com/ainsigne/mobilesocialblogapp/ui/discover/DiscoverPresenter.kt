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
    fun cancelFriend(aUser : Users, bUser : Users)
    fun acceptFriend(aUser : Users, bUser : Users)
    fun addFriend(aUser : Users, bUser : Users)
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


    override fun cancelFriend(aUser: Users, bUser: Users) {
        if(aUser.friendsRequestId?.filter { it != bUser.id } != null)
            aUser.friendsRequestId = aUser.friendsRequestId?.filter { it != bUser.id } as ArrayList<String>?
        if(aUser.friendsInviteid?.filter { it != bUser.id } != null)
            aUser.friendsInviteid = aUser.friendsInviteid?.filter { it != bUser.id } as ArrayList<String>?
        if(bUser.friendsRequestId?.filter { it != aUser.id } != null)
            bUser.friendsRequestId = bUser.friendsRequestId?.filter { it != aUser.id } as ArrayList<String>?
        if(bUser.friendsInviteid?.filter { it != aUser.id } != null)
            bUser.friendsInviteid = bUser.friendsInviteid?.filter { it != aUser.id } as ArrayList<String>?
        updateUsers(aUser,bUser)
    }

    override fun acceptFriend(aUser: Users, bUser: Users) {
        aUser.friendsRequestId = aUser.friendsRequestId?.filter { it != bUser.id } as ArrayList<String>?
        aUser.friendsInviteid = aUser.friendsInviteid?.filter { it != bUser.id } as ArrayList<String>?
        bUser.friendsRequestId = bUser.friendsRequestId?.filter { it != aUser.id } as ArrayList<String>?
        bUser.friendsInviteid = bUser.friendsInviteid?.filter { it != aUser.id } as ArrayList<String>?
        if(aUser.friendsId == null)
            aUser.friendsId = ArrayList<String>()
        if(bUser.friendsId == null)
            bUser.friendsId = ArrayList<String>()
        bUser.id?.let {
            aUser.friendsId?.add(it)
        }
        aUser.id?.let {
            bUser.friendsId?.add(it)
        }
        updateUsers(aUser,bUser)
    }

    override fun addFriend(aUser: Users, bUser: Users) {
        aUser.friendsRequestId?.let {
            bUser.id?.let {id ->
                it.add(id)
            }
        }
        aUser.friendsInviteid?.let {
            bUser.id?.let {id ->
                it.add(id)
            }
        }

        if(bUser.friendsInviteid == null){
            bUser.friendsInviteid = ArrayList<String>()
            aUser.id?.let {
                bUser.friendsInviteid?.add(it)
            }
        }
        if(aUser.friendsRequestId == null){
            aUser.friendsRequestId = ArrayList<String>()
            bUser.id?.let {
                aUser.friendsRequestId?.add(it)
            }
        }
        updateUsers(aUser,bUser)
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

    var service: DiscoverServices = DiscoverServices(apiManager, authManager)

    /**
     * initializes with the DiscoverFragment as the DiscoverView for updating
     * and authManager for consuming the authentication api and apiManager for consuming the api
     * related to data
     **/
    init {
        service.contract = this
        this.view = view
    }


}
