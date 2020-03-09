package com.ainsigne.mobilesocialblogapp.ui.discover

import android.util.Log
import com.ainsigne.mobilesocialblogapp.R
import com.ainsigne.mobilesocialblogapp.manager.APIManager
import com.ainsigne.mobilesocialblogapp.manager.AuthManager
import com.ainsigne.mobilesocialblogapp.manager.UsersRetrieved
import com.ainsigne.mobilesocialblogapp.models.Users
import com.ainsigne.mobilesocialblogapp.utils.Constants
import com.ainsigne.mobilesocialblogapp.utils.UsersConversion
import java.io.File
import java.util.*
import kotlin.collections.ArrayList

/**
 * DiscoverServices for implementing the services needed for the presenter.
 **/
class DiscoverServices(
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
     * DiscoverView reference of the DiscoverFragment
     * as DiscoverView type. Must be weak
     **/
    var contract: DiscoverContract? = null

    var allUsers : ArrayList<Users>? = null

    fun retrievedAll() : Boolean{
        return allUsers != null
    }

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
                contract?.retrievedAll()
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
        apiManager.addUser(aUser.convertToKeyVal() , completion = { err,msg ->
            contract?.updatedUsers()
        })
    }


}
