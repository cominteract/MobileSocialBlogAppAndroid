package com.ainsigne.mobilesocialblogapp.utils

class Constants {
    companion object{
        val fake_build = "fake"
        val users = "users"
        val posts = "posts"
        val chats = "chats"
        val comments = "comments"
        val sessions = "sessions"

        val defaultposturl = "https://blog.us.playstation.com/tachyon/2019/11/49118747543_df228ca2dd_k.jpg?w=1280"

        val defaultuserurl = "https://alumni.crg.eu/sites/default/files/default_images/default-picture_0_0.png"

        val firebaseurl = "https://firebasestorage.googleapis.com"

        fun getRandomString(length: Int) : String {
            val allowedChars = "ABCDEFGHIJKLMNOPQRSTUVWXTZabcdefghiklmnopqrstuvwxyz0123456789"
            return (1..length)
                .map { allowedChars.random() }
                .joinToString("")
        }
    }
}