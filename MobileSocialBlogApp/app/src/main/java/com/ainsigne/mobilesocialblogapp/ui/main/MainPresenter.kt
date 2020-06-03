package com.ainsigne.mobilesocialblogapp.ui.main


import android.util.Log
import com.ainsigne.mobilesocialblogapp.manager.APIManager
import com.ainsigne.mobilesocialblogapp.manager.AuthManager


/**
 * MainView interface for updating the view in the activity
 **/
interface MainView {
    fun tokenRefreshedUpdateView()
    fun tokenDeletedUpdateView()
}

/**
 * MainContract interface for delegating implementations from the MainServices
 **/
interface MainContract {
    fun isLogged() : Boolean
    fun tokenRefreshed()
    fun tokenDeleted()
}

/**
 * MainPresenter interface for implementing the MainPresenter
 **/
interface MainPresenter {
    fun isLogged() : Boolean
    fun refreshToken()
    fun deleteToken()
}


/**
 * MainPresenter implementation based on the presenter protocol.
 **/
class MainPresenterImplementation(
    view: MainView, apiManager: APIManager,
    authManager: AuthManager
) : MainPresenter, MainContract {

    var view: MainView

    var service: MainServices = MainServices(apiManager, authManager)

    /**
     * initializes with the RedditAuthSignFragment as the RedditAuthSignView for updating
     * and authManager for consuming the authentication api and apiManager for consuming the api
     * related to data
     **/
    init {
        service.contract = this
        this.view = view
    }

    override fun isLogged(): Boolean {
       return service.isLogged()
    }

    override fun refreshToken() {
        Log.d(" Refreshing token "," Refreshing token ")
        service.retrieveTokens()
    }

    override fun deleteToken() {
        service.deleteTokens()
    }
    override fun tokenRefreshed(){
        view.tokenRefreshedUpdateView()
    }


    override fun tokenDeleted() {
        view.tokenDeletedUpdateView()
    }
}
