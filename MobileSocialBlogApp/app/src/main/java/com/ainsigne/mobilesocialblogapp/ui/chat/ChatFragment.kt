package com.ainsigne.mobilesocialblogapp.ui.chat

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager

import com.ainsigne.mobilesocialblogapp.R
import com.ainsigne.mobilesocialblogapp.adapters.ChatMessagesAdapter
import com.ainsigne.mobilesocialblogapp.adapters.ChatSessionsAdapter
import com.ainsigne.mobilesocialblogapp.base.BaseFragment
import com.ainsigne.mobilesocialblogapp.models.Users
import com.ainsigne.mobilesocialblogapp.ui.main.MainActivity
import com.ainsigne.mobilesocialblogapp.utils.Config
import com.ainsigne.mobilesocialblogapp.utils.UINavigation
import kotlinx.android.synthetic.main.fragment_chat.*
import org.jetbrains.anko.doAsync
import org.jetbrains.anko.uiThread


/**
 * A simple [Fragment] subclass.

 * Use the [Chat.newInstance] factory method to
 * create an instance of this fragment.
 *
 */
class ChatFragment : BaseFragment(), ChatView {

    override fun navigateToSession(chatId: String) {
        Log.d(" Navigating to user "," Navigating to user $chatId")
        val bundle = Bundle()
        bundle.putString("chatId", chatId)
        main.addOnTopWithBundle(UINavigation.session, bundle)

    }

    override fun currentUser(): Users? {
        Config.getUser()?.let {
            val user = presenter?.getUserFrom(it)
            return user
        }
        return null
    }

    override fun retrievedAllUpdateView() {

        Config.getUser()?.let {username ->
            val user = presenter?.getUserFrom(username)
            presenter?.allSessions()?.filter { it.userIds != null && it.userIds!!.contains(user?.id) }?.let { sessions ->
                val layoutManager = LinearLayoutManager(this.context)
                rv_chat?.layoutManager = layoutManager
                doAsync {
                    uiThread {
                        rv_chat?.adapter = ChatSessionsAdapter(sessions, it)
                    }
                }
            }
        }



    }

    override fun userFrom(author: String): Users? {
        return presenter?.getUserFrom(author)
    }

    /**
     * injector as ChatInjector injects the presenter with the services and data needed for the
     * implementation
     **/
    var injector = ChatInjectorImplementation()
    /**
     * presenter as ChatPresenter injected automatically to call implementations
     **/
    var presenter: ChatPresenter? = null


    lateinit var main : MainActivity

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        injector.inject(this)
        if(context is MainActivity)
            main = context as MainActivity
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_chat, container, false)


    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        presenter?.retrieveAll()
        main.showTab()
    }


    companion object {
        /**
         * Use this factory method to create a new instance of
         * this fragment using the provided parameters.
         *
         * @return A new instance of fragment Chat.
         */
        // TODO: Rename and change types and number of parameters
        @JvmStatic
        fun newInstance() =
            ChatFragment()
    }
}
