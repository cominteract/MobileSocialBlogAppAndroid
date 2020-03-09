package com.ainsigne.mobilesocialblogapp.ui.chatsession

import android.graphics.Bitmap
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager

import com.ainsigne.mobilesocialblogapp.R
import com.ainsigne.mobilesocialblogapp.adapters.ChatMessagesAdapter
import com.ainsigne.mobilesocialblogapp.base.BaseFragment
import com.ainsigne.mobilesocialblogapp.models.ChatMessages
import com.ainsigne.mobilesocialblogapp.models.ChatSession
import com.ainsigne.mobilesocialblogapp.models.Users
import com.ainsigne.mobilesocialblogapp.utils.Config
import com.ainsigne.mobilesocialblogapp.utils.Constants
import com.ainsigne.mobilesocialblogapp.utils.fromNow
import kotlinx.android.synthetic.main.fragment_chat_session.*
import org.jetbrains.anko.doAsync
import org.jetbrains.anko.uiThread
import java.util.*
import kotlin.collections.ArrayList


/**
 * A simple [Fragment] subclass.

 * Use the [ChatSession.newInstance] factory method to
 * create an instance of this fragment.
 *
 */
class ChatSessionFragment : BaseFragment(), ChatSessionView {

    var chatId = ""

    override fun currentUser(): Users? {
        Config.getUser()?.let {
            return presenter?.getUserFrom(it)
        }
        return null
    }

    override fun userFrom(author: String): Users? {
        return presenter?.getUserFrom(author)
    }

    override fun addedChatMessageUpdateView() {

    }

    override fun addedChatSessionUpdateView() {
        presenter?.retrieveAll()
    }



    override fun retrievedAllUpdateView() {
        Config.getUser()?.let {username ->
            val user = presenter?.getUserFrom(username)
            presenter?.allChats()?.filter {message ->
                (message.userId == user?.id && message.replyTo == chatId) ||
                        (message.userId == chatId && message.replyTo == user?.id)
            }?.let { chats ->
                val layoutManager = LinearLayoutManager(this.context)
                rv_chatsession.layoutManager = layoutManager
                doAsync {
                    uiThread {
                        rv_chatsession.adapter = ChatMessagesAdapter(chats, it)
                        btn_chat_send.setOnClickListener {
                            user?.let {aUser ->
                                presenter?.sendChatWithSession(aUser,chats,chatId,et_chat_message.text.toString())
                            }
                        }
                    }
                }
            }
        }
    }

    /**
     * injector as ChatSessionInjector injects the presenter with the services and data needed for the
     * implementation
     **/
    var injector = ChatSessionInjectorImplementation()
    /**
     * presenter as ChatSessionPresenter injected automatically to call implementations
     **/
    var presenter: ChatSessionPresenter? = null


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        injector.inject(this)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_chat_session, container, false)


    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        arguments?.getString("chatId")?.let {
            chatId = it
        }
        presenter?.retrieveAll()
    }


    companion object {
        /**
         * Use this factory method to create a new instance of
         * this fragment using the provided parameters.
         *
         * @return A new instance of fragment ChatSession.
         */
        // TODO: Rename and change types and number of parameters
        @JvmStatic
        fun newInstance() =
            ChatSessionFragment()
    }
}
