package com.ainsigne.mobilesocialblogapp.base


import android.os.Bundle

import android.view.View
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentTransaction
import com.ainsigne.mobilesocialblogapp.R
import com.ainsigne.mobilesocialblogapp.utils.UINavigation


/// the base fragment used for common functionalities
open class                                                                  BaseFragment : Fragment() {

    /**
     * moveToWithBundle uses fragKey string to navigate
     * by replacing to a certain fragment associated with key and setting the arguments for a fragment
     * initializes the current fragment shown
     * @param fragKey as String key used to determine what fragment to be returned in used in navigation
     * @param bundle as Bundle used to set the arguments of the new fragment
     * @return whether fragment navigation is successful
     */
    fun moveToWithBundle(fragKey: String, bundle: Bundle): Boolean {
        val frag = UINavigation.frag(fragKey)
        frag.arguments = bundle
        activity!!.supportFragmentManager
            .beginTransaction()
            .replace(R.id.fragment_container, frag, fragKey)
            .commit()
        return true
    }

    /**
     * moveTo uses fragKey string to navigate
     * by replacing to a certain fragment associated with key
     * initializes the current fragment shown
     * @param fragKey as String key used to determine what fragment to be returned in used in navigation
     * @return whether fragment navigation is successful
     */
    fun moveTo(fragKey: String?): Boolean {
        if (fragKey != null) {
            activity!!.supportFragmentManager
                .beginTransaction()
                .replace(R.id.fragment_container, UINavigation.frag(fragKey), fragKey)
                .commit()
            return true
        }
        return false
    }

    /**
     * addOnTop uses fragKey string to navigate
     * by adding to a certain fragment associated with key
     * @param fragKey as String key used to determine what fragment to be returned in used in navigation
     * @return whether fragment navigation is successful
     */
    fun addOnTop(fragKey: String?): Boolean {
        if (fragKey != null) {
            activity!!.supportFragmentManager
                .beginTransaction().setCustomAnimations(
                    R.anim.slide_in_from_right, R.anim.slide_out_to_left,
                    R.anim.slide_in_from_left, R.anim.slide_out_to_right
                )
                .replace(R.id.fragment_container, UINavigation.frag(fragKey), fragKey)
                .setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN)
                .addToBackStack(null)
                .commit()

//            activity!!.supportFragmentManager
//                    .beginTransaction().setCustomAnimations(R.anim.slide_right,R.anim.slide_left)
//                    .replace(R.id.fragment_container, UINavigation.frag(fragKey))
//                    .addToBackStack(null)
//                    .commit()

            return true
        }
        return false
    }

    /**
     * addOnTop uses fragKey string to navigate
     * by adding to a certain fragment associated with key and setting the arguments for a fragment
     * @param fragKey as String key used to determine what fragment to be returned in used in navigation
     * @param bundle as Bundle used to set the arguments of the new fragment
     * @return whether fragment navigation is successful
     */
    fun addOnTopWithBundle(fragKey: String?, bundle: Bundle): Boolean {
        if (fragKey != null) {
            val frag = UINavigation.frag(fragKey)
            frag.arguments = bundle
            activity!!.supportFragmentManager
                .beginTransaction().setCustomAnimations(
                    R.anim.slide_in_from_right, R.anim.slide_out_to_left,
                    R.anim.slide_in_from_left, R.anim.slide_out_to_right
                )
                .replace(R.id.fragment_container, frag, fragKey)
                .setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN)
                .addToBackStack(null)
                .commit()

//            activity!!.supportFragmentManager
//                    .beginTransaction().setCustomAnimations(R.anim.slide_right,R.anim.slide_left)
//                    .replace(R.id.fragment_container, frag)
//                    .addToBackStack(null)
//                    .commit()

            return true
        }
        return false
    }

    /**
     * dismiss pops the fragment stack removing the current fragment
     */
    fun dismiss() {
        activity!!.supportFragmentManager.popBackStack()
    }

}