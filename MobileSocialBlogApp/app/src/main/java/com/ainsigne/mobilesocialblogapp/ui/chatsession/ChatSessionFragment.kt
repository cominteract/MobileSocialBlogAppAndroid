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

                            val chat = ChatMessages()
                            chat.id = Constants.getRandomString(22)
                            chat.author = user?.username
                            chat.userId = user?.id
                            chat.timestamp = Date().toString()
                            chat.message = et_chat_message.text.toString()
                            chat.replyTo = chatId
                            chat.timestamp_from = Date().fromNow()
                            chat.msgId = chats.size

                            presenter?.sendChat(chat)
                            var session = ChatSession()
                            user?.id?.let {id->
                                presenter?.allSessions()?.filter { it.userIds != null && it.userIds!!.contains(id) && it.userIds!!.contains(chatId) }?.let {filtered->
                                    if(filtered.isNotEmpty())
                                        session = filtered[0]
                                }
                            }
                            if(session.id == null){
                                session.id = Constants.getRandomString(22)
                                session.userIds = ArrayList()
                                user?.id?.let {id->
                                    session.userIds?.add(id)
                                    session.userIds?.add(chatId)
                                }

                            }
                            session.message = et_chat_message.text.toString()
                            session.author = user?.username
                            session.timestamp = Date().toString()
                            presenter?.sendSession(session)

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
