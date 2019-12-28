package com.ainsigne.mobilesocialblogapp.ui.optionalsignup


import com.ainsigne.mobilesocialblogapp.manager.APIManager
import com.ainsigne.mobilesocialblogapp.manager.AuthManager
import com.ainsigne.mobilesocialblogapp.models.Users


/**
 * OptionalSignupView interface for updating the view in the fragments
 **/
interface OptionalSignupView {
    fun updatedUserUpdateView()
    fun uploadedImageUpdateView()
    fun retrievedAllUpdateView()
}

/**
 * OptionalSignupContract interface for delegating implementations from the OptionalSignupServices
 **/
interface OptionalSignupContract {
    fun updatedUser()
    fun uploadedImage()
    fun retrievedAll()
}

/**
 * OptionalSignupPresenter interface for implementing the OptionalSignupPresenter
 **/
interface OptionalSignupPresenter {
    fun updateUser(user : Users, toUpload : Boolean)
    fun uploadImage(data : String)
    fun retrieveAll()
    fun getUserFrom(username : String) : Users?
}


/**
 * OptionalSignupPresenter implementation based on the presenter protocol.
 **/
class OptionalSignupPresenterImplementation(
    view: OptionalSignupView, apiManager: APIManager,
    authManager: AuthManager
) : OptionalSignupPresenter, OptionalSignupContract {

    override fun getUserFrom(username: String): Users? {
        return service.getUserFrom(username)
    }

    override fun updatedUser() {
        view.updatedUserUpdateView()
    }

    override fun uploadedImage() {
        view.uploadedImageUpdateView()
    }

    override fun retrievedAll() {
        view.retrievedAllUpdateView()
    }

    override fun updateUser(user: Users, toUpload: Boolean) {
        service.updateUser(user, toUpload)
    }

    override fun uploadImage(data: String) {
        service.uploadImage(data)
    }

    override fun retrieveAll() {
        service.retrieveAllUsers()
    }

    var view: OptionalSignupView

    var service: OptionalSignupServices

    /**
     * initializes with the OptionalSignupFragment as the OptionalSignupView for updating
     * and authManager for consuming the authentication api and apiManager for consuming the api
     * related to data
     **/
    init {
        service = OptionalSignupServices(apiManager, authManager)
        service.contract = this
        this.view = view
    }


}
