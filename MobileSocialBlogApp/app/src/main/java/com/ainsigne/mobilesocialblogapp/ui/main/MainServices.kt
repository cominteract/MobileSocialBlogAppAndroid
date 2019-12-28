package com.ainsigne.mobilesocialblogapp.ui.main

import android.util.Log
import com.ainsigne.mobilesocialblogapp.R
import com.ainsigne.mobilesocialblogapp.manager.APIManager
import com.ainsigne.mobilesocialblogapp.manager.AuthManager

import com.ainsigne.mobilesocialblogapp.utils.Config
import com.ainsigne.mobilesocialblogapp.utils.toDate
import com.ainsigne.mobilesocialblogapp.utils.toStringFormat
import java.util.*

/**
 * MainServices for implementing the services needed for the presenter.
 **/
class MainServices(
    apiManager: APIManager,
    authManager: AuthManager
)  {
    /**
     * apiManager used in consuming the api related to data whether mock or from aws to be initialized
     **/
    var apiManager: APIManager = apiManager
    /**
     * authManager used in consuming the authentication api whether mock or from aws to be initialized
     **/
    var authManager: AuthManager = authManager
    /**
     * RedditAuthSignView reference of the RedditAuthSignFragment
     * as RedditAuthSignView type. Must be weak
     **/
    var contract: MainContract? = null

    fun isLogged() : Boolean
    {
        return authManager.isLogged()
    }

}
