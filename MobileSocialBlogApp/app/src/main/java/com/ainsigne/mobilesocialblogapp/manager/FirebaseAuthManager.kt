package com.ainsigne.mobilesocialblogapp.manager


/**
 * Implements the authmanager abstract class (which derives from the authprotocol) and backend
 * for authenticating
 **/
class FirebaseAuthManager : AuthManager() {
    override fun isLogged(): Boolean {
        return true
    }

}
