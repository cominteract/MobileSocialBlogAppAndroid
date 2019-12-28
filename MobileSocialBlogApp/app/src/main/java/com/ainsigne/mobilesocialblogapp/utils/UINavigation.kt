package com.ainsigne.mobilesocialblogapp.utils

import android.app.Dialog
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.Fragment
import com.ainsigne.mobilesocialblogapp.R
import com.ainsigne.mobilesocialblogapp.ui.chat.ChatFragment
import com.ainsigne.mobilesocialblogapp.ui.chatsession.ChatSessionFragment
import com.ainsigne.mobilesocialblogapp.ui.discover.DiscoverFragment
import com.ainsigne.mobilesocialblogapp.ui.feed.FeedFragment
import com.ainsigne.mobilesocialblogapp.ui.feeddetails.FeedDetailsFragment
import com.ainsigne.mobilesocialblogapp.ui.optionalsignup.OptionalSignupFragment
import com.ainsigne.mobilesocialblogapp.ui.profile.ProfileFragment
import com.ainsigne.mobilesocialblogapp.ui.signup.SignupFragment
import kotlinx.android.synthetic.main.content_main.view.*


/**
 * UINavigation fragment navigation helper that makes use of keys for accessing fragments
 */
class UINavigation
{
    companion object {


        val feed = "Feed"

        val details = "Details"

        val discover = "Discover"

        val chat = "Chat"

        val profile = "Profile"

        val session = "Session"

        val signup = "Signup"

        val optsignup = "OptSignup"

        /**
         * tabState from layoutId to determine that when tab is clicked
         * to return the key from the view pressed to be used in navigation
         * @param layoutId as Int id of the view pressed
         * @return String as key to be used in navigation
         */
        @JvmStatic
        fun tabState(layoutId : Int, logged : Boolean) : String =
            when(layoutId)
            {
                R.id.navigation_feed -> feed
                R.id.navigation_chat -> chat
                R.id.navigation_activity -> discover
                R.id.navigation_profile -> profile
                else -> feed
            }

        /**
         * frag to return the fragment based on the key input
         * @param fragKey as String to be used in determining what fragment to navigate into
         * @return Fragment that is the instance of fragment to be navigated
         */
        @JvmStatic
        fun frag(fragKey: String) : Fragment =
            when (fragKey) {
                signup -> SignupFragment.newInstance()
                feed -> FeedFragment.newInstance()
                optsignup -> OptionalSignupFragment.newInstance()
                chat -> ChatFragment.newInstance()
                session -> ChatSessionFragment.newInstance()
                discover -> DiscoverFragment.newInstance()
                profile -> ProfileFragment.newInstance()
                details -> FeedDetailsFragment.newInstance()
                else -> FeedFragment.newInstance()
            }


        /**
         * frag to return the fragment based on the key input
         * @param fragKey as String to be used in determining what fragment to navigate into
         * @return DialogFragment that is the instance of dialog fragment to be navigated
         */
        @JvmStatic
        fun dialogFrag(fragKey: String) : DialogFragment =
            when (fragKey) {
                "" -> DialogFragment()
                else -> DialogFragment()
            }

    }
}
