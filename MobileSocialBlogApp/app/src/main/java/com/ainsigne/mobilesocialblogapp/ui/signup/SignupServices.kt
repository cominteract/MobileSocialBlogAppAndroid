package com.ainsigne.mobilesocialblogapp.ui.signup

import android.util.Log
import com.ainsigne.mobilesocialblogapp.R
import com.ainsigne.mobilesocialblogapp.manager.APIManager
import com.ainsigne.mobilesocialblogapp.manager.AuthManager
import com.ainsigne.mobilesocialblogapp.manager.UsersRetrieved
import com.ainsigne.mobilesocialblogapp.models.Users
import com.ainsigne.mobilesocialblogapp.utils.Config
import com.ainsigne.mobilesocialblogapp.utils.Constants
import java.io.File

/**
 * SignupServices for implementing the services needed for the presenter.
 **/
class SignupServices(
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
     * SignupView reference of the SignupFragment
     * as SignupView type. Must be weak
     **/
    var contract: SignupContract? = null

    var allUsers : ArrayList<Users>? = null

    fun retrieveTokens(){
        apiManager.retrieveAllTokens { error, msg ->
            
        }
    }

    fun usernameExists(username: String) : Boolean{
        allUsers?.filter { it.username == username }?.let {

            return it.isNotEmpty()
        }
        return false
    }

    fun getUserFrom(username : String) : Users?{
        allUsers?.filter { it.username == username }?.let {
            if(it.isNotEmpty())
                return it[0]
        }
        return null
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
        apiManager.addUser(aUser.convertToKeyVal() , completion = { err,msg ->
            if(err == null)
                contract?.addedUser()
        })



    }

    fun passwordCorrect(username : String, password : String) : Boolean{
        allUsers?.filter { it.username == username }?.let {
            if(it.isNotEmpty())
                return it[0].password == password
        }
        return false
    }

    fun signupUser(aUser : Users, toUpload : Boolean)
    {
        addUserToApi(aUser, toUpload)
    }


    fun retrieveAllUsers(){
        val usersRetrieved = object : UsersRetrieved {
            override fun retrievedUsers(users: ArrayList<Users>?, msg: String) {
                allUsers = users
                contract?.retrievedAll()

            }
        }
        apiManager.retrieveAllUsers(usersRetrieved)

    }
}
