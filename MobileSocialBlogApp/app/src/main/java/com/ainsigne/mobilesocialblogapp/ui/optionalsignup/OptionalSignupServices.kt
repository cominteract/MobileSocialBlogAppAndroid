package com.ainsigne.mobilesocialblogapp.ui.optionalsignup

import android.util.Log
import com.ainsigne.mobilesocialblogapp.R
import com.ainsigne.mobilesocialblogapp.manager.APIManager
import com.ainsigne.mobilesocialblogapp.manager.AuthManager
import com.ainsigne.mobilesocialblogapp.manager.UsersRetrieved
import com.ainsigne.mobilesocialblogapp.models.Users
import com.ainsigne.mobilesocialblogapp.utils.Constants
import java.io.File
import com.ainsigne.mobilesocialblogapp.utils.Config

/**
 * OptionalSignupServices for implementing the services needed for the presenter.
 **/
class OptionalSignupServices(
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
     * OptionalSignupView reference of the OptionalSignupFragment
     * as OptionalSignupView type. Must be weak
     **/
    var contract: OptionalSignupContract? = null

    var allUsers : ArrayList<Users>? = null

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
            }
        }
        apiManager.retrieveAllUsers(usersRetrieved)
    }

    fun addUserToApi(aUser : Users, toUpload : Boolean){
        if(aUser.id == null){
            aUser.id = Constants.getRandomString(22)
            if(!allUsers?.filter { it.id == aUser.id }.isNullOrEmpty())
                aUser.id = Constants.getRandomString(22)
        }
        if(aUser.username == null || aUser.fullname == null || aUser.password == null){
            Log.d(" Can't signup user ", " Can't signup user missing fields ")
            return
        }
        aUser.photoUrl?.let {
            if(!it.contains(Constants.firebaseurl) && !it.contains(Constants.defaultuserurl) && toUpload && File(it).exists()){
                apiManager.uploadImage(File(it), "img_${aUser.id!!}", completion = { err, msg ->
                    if(err == null)
                        Log.d(" Uploaded "," Uploaded ")
                })
            }
        }
        apiManager.addUser(Users.convertToKeyVal(aUser) , completion = { err,msg ->
            if(err == null)
                contract?.updatedUser()
        })
    }

    fun updateUser(aUser : Users, toUpload : Boolean)
    {
        addUserToApi(aUser, toUpload)
    }

    fun usernameExists(username: String) : Boolean{
        allUsers?.filter { it.username == username }?.let {
            return it.isNotEmpty()
        }
        return false
    }

    fun uploadImage(data : String){

        if(File(data).exists()){
            Config.getUser()?.let {
                val user = getUserFrom(it)
                val id = user?.id
                apiManager.uploadImage(File(data), "img_$id", completion =  { err,msg ->
                    if(err == null)
                        contract?.uploadedImage()
                })
            }
        }

    }
}
