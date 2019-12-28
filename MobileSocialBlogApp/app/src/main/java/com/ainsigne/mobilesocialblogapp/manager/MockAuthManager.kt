package com.ainsigne.mobilesocialblogapp.manager


/**
 * Implements the auth manager abstract class (which derives from the authprotocol) and  mock
 * implementation for authenticating
 **/
class MockAuthManager : AuthManager() {
    override fun isLogged(): Boolean {
        return true
    }

}
