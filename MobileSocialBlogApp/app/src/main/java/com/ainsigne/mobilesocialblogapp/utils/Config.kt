package com.ainsigne.mobilesocialblogapp.utils

import android.preference.PreferenceManager
import com.ainsigne.mobilesocialblogapp.models.Users
import com.ainsigne.mobilesocialblogapp.ui.main.MainActivity

class Config {
    companion object{
        val username_key = "username"
        val online_key = "online"
        val ref_feed_key = "reffeed"
        val ref_chat_key = "refchat"
        val ref_prof_key = "refprof"
        val ref_discover_key = "refdiscover"


        lateinit var context: MainActivity

        var user : Users? = null

        fun updateUser(value : String)
        {
            var pref = (PreferenceManager
            .getDefaultSharedPreferences(context))
            val editor = pref!!.edit()
            editor.putString(username_key,value)
            editor.apply()
        }

        fun getUser() : String?
        {
            var pref = (PreferenceManager
                .getDefaultSharedPreferences(context))
            return pref.getString(username_key,null)
        }

        fun updateOnline(value : Boolean)
        {
            var pref = (PreferenceManager
                .getDefaultSharedPreferences(context))
            val editor = pref!!.edit()
            editor.putBoolean(online_key,value)
            editor.apply()
        }

        fun getOnline() : Boolean?
        {
            var pref = (PreferenceManager
                .getDefaultSharedPreferences(context))
            return pref.getBoolean(online_key, false)
        }

        fun updateRefreshFeed(value : Boolean)
        {
            var pref = (PreferenceManager
                .getDefaultSharedPreferences(context))
            val editor = pref!!.edit()
            editor.putBoolean(ref_feed_key,value)
            editor.apply()
        }

        fun getRefreshFeed() : Boolean?
        {
            var pref = (PreferenceManager
                .getDefaultSharedPreferences(context))
            return pref.getBoolean(ref_feed_key,false)
        }

        fun updateRefreshChat(value : Boolean)
        {
            var pref = (PreferenceManager
                .getDefaultSharedPreferences(context))
            val editor = pref!!.edit()
            editor.putBoolean(ref_chat_key,value)
            editor.apply()
        }

        fun getRefreshChat() : Boolean?
        {
            var pref = (PreferenceManager
                .getDefaultSharedPreferences(context))
            return pref.getBoolean(ref_chat_key,false)
        }

        fun updateRefreshProfile(value : Boolean)
        {
            var pref = (PreferenceManager
                .getDefaultSharedPreferences(context))
            val editor = pref!!.edit()
            editor.putBoolean(ref_prof_key,value)
            editor.apply()
        }

        fun getRefreshProfile() : Boolean?
        {
            var pref = (PreferenceManager
                .getDefaultSharedPreferences(context))
            return pref.getBoolean(ref_prof_key,false)
        }

        fun updateRefreshDiscover(value : Boolean)
        {
            var pref = (PreferenceManager
                .getDefaultSharedPreferences(context))
            val editor = pref!!.edit()
            editor.putBoolean(ref_discover_key,value)
            editor.apply()
        }

        fun getRefreshDiscover() : Boolean?
        {
            var pref = (PreferenceManager
                .getDefaultSharedPreferences(context))
            return pref.getBoolean(ref_discover_key,false)
        }
    }
}