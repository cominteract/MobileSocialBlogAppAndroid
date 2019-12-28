package com.ainsigne.mobilesocialblogapp.manager

import com.ainsigne.mobilesocialblogapp.interfaces.AuthInterface

/**
 * serves as an abstract class for the auth protocol and its implementation
 **/
open class AuthManager : AuthInterface {
    override fun isLogged(): Boolean {
        return false
    }

}
