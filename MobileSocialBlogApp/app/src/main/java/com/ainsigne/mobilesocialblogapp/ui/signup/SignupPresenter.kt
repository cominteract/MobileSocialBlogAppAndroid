package com.ainsigne.mobilesocialblogapp.ui.signup


import com.ainsigne.mobilesocialblogapp.manager.APIManager
import com.ainsigne.mobilesocialblogapp.manager.AuthManager
import com.ainsigne.mobilesocialblogapp.models.Users


/**
 * SignupView interface for updating the view in the fragments
 **/
interface SignupView {
    fun addedUserUpdateView()
    fun retrievedAllUpdateView()
}

/**
 * SignupContract interface for delegating implementations from the SignupServices
 **/
interface SignupContract {
    fun addedUser()
    fun retrievedAll()
}

/**
 * SignupPresenter interface for implementing the SignupPresenter
 **/
interface SignupPresenter {
    fun signupUser(user : Users)
    fun passwordCorrect(username : String, password : String) : Boolean
    fun usernameExists(username : String): Boolean
    fun retrieveAll()
    fun getUserFrom(username : String) : Users?
}


/**
 * SignupPresenter implementation based on the presenter protocol.
 **/
class SignupPresenterImplementation(
    view: SignupView, apiManager: APIManager,
    authManager: AuthManager
) : SignupPresenter, SignupContract {


    override fun retrievedAll() {
        view.retrievedAllUpdateView()
    }

    override fun addedUser() {
        view.addedUserUpdateView()
    }

    override fun signupUser(user: Users) {
        service.signupUser(user,true)
    }

    override fun passwordCorrect(username: String, password: String): Boolean {
        return service.passwordCorrect(username,password)
    }

    override fun usernameExists(username: String): Boolean {
        return service.usernameExists(username)
    }

    override fun retrieveAll() {
        service.retrieveAllUsers()
    }

    override fun getUserFrom(username: String): Users? {
        return service.getUserFrom(username)
    }

    var view: SignupView

    var service: SignupServices

    /**
     * initializes with the SignupFragment as the SignupView for updating
     * and authManager for consuming the authentication api and apiManager for consuming the api
     * related to data
     **/
    init {
        service = SignupServices(apiManager, authManager)
        service.contract = this
        this.view = view
    }


}
