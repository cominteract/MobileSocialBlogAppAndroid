package com.ainsigne.mobilesocialblogapp.ui.main

import com.ainsigne.mobilesocialblogapp.data.DataInjector
import com.ainsigne.mobilesocialblogapp.manager.ServicesInjector
import com.ainsigne.mobilesocialblogapp.manager.APIManager
import com.ainsigne.mobilesocialblogapp.manager.AuthManager

/**
 * injector interface template for injection of the services and data
 **/
interface MainInjector {
    fun inject(activity: MainActivity)
}


/**
 * Main injector implementation of the protocol template for injection
 **/
class MainImplementation() {

    /**
     * injects the MainActivity generated with the services and the
     * presenter used data
     * @param activity : MainActivity generated by the MVPTemplate
     **/
    fun inject(activity: MainActivity) {
        val data = DataInjector().data
        val services = ServicesInjector(data)
        val presenter = MainPresenterImplementation(activity, services.getAPI(), services.getAuth())
        activity.presenter = presenter
    }
}