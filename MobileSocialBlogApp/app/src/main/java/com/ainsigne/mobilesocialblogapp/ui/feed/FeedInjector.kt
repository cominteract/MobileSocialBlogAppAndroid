package com.ainsigne.mobilesocialblogapp.ui.feed

import com.ainsigne.mobilesocialblogapp.data.DataInjector
import com.ainsigne.mobilesocialblogapp.manager.ServicesInjector
import com.ainsigne.mobilesocialblogapp.manager.APIManager
import com.ainsigne.mobilesocialblogapp.manager.AuthManager

/**
 * injector interface template for injection of the services and data
 **/
interface FeedInjector {
    fun inject(fragment: FeedFragment)
}


/**
 * FeedInjector injector implementation of the protocol template for injection
 **/
class FeedInjectorImplementation() {

    /**
     * injects the ___VARIABLE_moduleName___ViewController generated with the services and the
     * presenter used data
     * @param fragment : FeedFragment generated by the MVPTemplate
     **/
    fun inject(fragment: FeedFragment) {
        val data = DataInjector().data
        val services = ServicesInjector(data)
        val presenter = FeedPresenterImplementation(fragment, services.getAPI(), services.getAuth())
        fragment.presenter = presenter
    }


}
