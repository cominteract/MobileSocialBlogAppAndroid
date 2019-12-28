package com.ainsigne.mobilesocialblogapp.models

import com.ainsigne.mobilesocialblogapp.utils.Constants
import com.google.firebase.database.IgnoreExtraProperties

@IgnoreExtraProperties
data class Users ( var id : String? = null, var username : String? = null, var password : String? = null, var fullname  : String? = null, var firstname : String? = null,
                   var lastname : String? = null, var location : String? = null, var birthday : String? = null, var timestampCreated : String? = null, var photoUrl : String? = null,
                   var friendsId : ArrayList<String>? = null, var friendsInviteid : ArrayList<String>? = null, var friendsRequestId : ArrayList<String>? = null, var online : Boolean? = null)

{
    fun convertToKeyVal() : Map<String,Any?>{
        return mapOf(
            "id" to id,
            "username" to username,
            "password" to password,
            "full" to fullname,
            "last" to lastname,
            "first" to firstname,
            "timestampCreated" to timestampCreated,
            "photoUrl" to photoUrl,
            "birthday" to birthday,
            "location" to location,
            "online" to online,
            "friendsId" to friendsId,
            "friendsInviteId" to friendsInviteid,
            "friendsRequestId" to friendsRequestId
        )
    }
    companion object{
        fun convertToKeyVal(users : Users) : HashMap<String, Any>{
            var map = HashMap<String,Any>()
            users.id?.let { map["id"] = it }
            users.username?.let { map["username"] = it }
            users.password?.let { map["password"] = it }
            users.fullname?.let { map["full"] = it }
            users.lastname?.let { map["last"] = it }
            users.firstname?.let { map["first"] = it }
            users.timestampCreated?.let { map["timestampCreated"] = it }
            users.photoUrl?.let { map["photoUrl"] = it }
            users.birthday?.let { map["birthday"] = it }
            users.location?.let { map["location"] = it }
            users.friendsId?.let { map["friendsId"] = it }
            users.online?.let { map["online"] = it }
            users.friendsInviteid?.let { map["friendsInviteid"] = it }
            users.friendsRequestId?.let {
                map["friendsRequestId"] = it
            }
            return map
        }

        fun users() : ArrayList<Users>
        {
            var users = ArrayList<Users>()
            for(i in 0 until 5){
                val user = Users()
                user.online = false
                user.id = Constants.getRandomString(22)
                user.friendsId = ArrayList()
                user.friendsId?.add(Constants.getRandomString(22))
                user.friendsInviteid = ArrayList()
                user.friendsInviteid?.add(Constants.getRandomString(22))
                user.friendsRequestId = ArrayList()
                user.friendsRequestId?.add(Constants.getRandomString(22))
                user.lastname = Constants.getRandomString(6)
                user.firstname = Constants.getRandomString(6)
                user.fullname = "${user.firstname} ${user.lastname}"
                user.password = Constants.getRandomString(22)
                user.birthday = "August 31 1986"
                user.location = "Dasma"
                user.photoUrl = Constants.defaultuserurl
                user.username = Constants.getRandomString(22)
                users.add(user)
            }
            return users
        }
    }
}