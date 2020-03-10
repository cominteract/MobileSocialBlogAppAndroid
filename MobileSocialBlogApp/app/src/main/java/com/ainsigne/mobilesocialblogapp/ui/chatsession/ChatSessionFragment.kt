package com.ainsigne.mobilesocialblogapp.ui.chatsession

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.ainsigne.mobilesocialblogapp.R
import com.ainsigne.mobilesocialblogapp.adapters.ChatMessagesAdapter
import com.ainsigne.mobilesocialblogapp.base.BaseFragment
import com.ainsigne.mobilesocialblogapp.models.CallRecords
import com.ainsigne.mobilesocialblogapp.models.ChatSession
import com.ainsigne.mobilesocialblogapp.models.Users
import com.ainsigne.mobilesocialblogapp.utils.Config
import com.ainsigne.mobilesocialblogapp.utils.Constants
import com.ainsigne.mobilesocialblogapp.utils.toStringFormat
import kotlinx.android.synthetic.main.fragment_chat_session.*
import org.jetbrains.anko.doAsync
import org.jetbrains.anko.uiThread
import org.jitsi.meet.sdk.JitsiMeet
import org.jitsi.meet.sdk.JitsiMeetActivity
import org.jitsi.meet.sdk.JitsiMeetConferenceOptions
import org.jitsi.meet.sdk.JitsiMeetUserInfo
import java.net.MalformedURLException
import java.net.URL
import java.util.*


/**
 * A simple [Fragment] subclass.

 * Use the [ChatSession.newInstance] factory method to
 * create an instance of this fragment.
 *
 */
class ChatSessionFragment : BaseFragment(), ChatSessionView {

    var chatId = ""

    var conferenceName = ""

    override fun currentUser(): Users? {
        Config.getUser()?.let {
            return presenter?.getUserFrom(it)
        }
        return null
    }

    override fun userFrom(author: String): Users? {
        return presenter?.getUserFrom(author)
    }

    override fun receivedCallFromUpdateView(callerName: String, conferenceName : String) {
        Log.d(" Call from fragment "," Call from fragment $conferenceName")
        this.conferenceName = conferenceName
        if (conferenceName.isNotEmpty()) {
            val options =
                JitsiMeetConferenceOptions.Builder()
                    .setRoom(conferenceName)
                    .build()
            JitsiMeetActivity.launch(activity, options)
        }
    }

    override fun addedChatMessageUpdateView() {

    }

    override fun addedChatSessionUpdateView() {
        presenter?.retrieveAll()
    }



    override fun retrievedAllUpdateView() {


        Config.getUser()?.let { username->
            Log.d("Call retrieve frag","Call retrieve frag $username")
            presenter?.getUserFrom(username)?.id?.let {
                Log.d("Call retrieve frag","Call retrieve frag $it")
                presenter?.retrieveCalls(it)
            }
        }

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
                        iv_chat_video.setOnClickListener {
                            user?.id.let { id ->
                                conferenceName = "${id}_$chatId"
                            }
                            val callRecords = CallRecords()
                            callRecords.id = Constants.getRandomString(19)
                            callRecords.callerId = user?.id
                            callRecords.callerName = user?.username
                            callRecords.calledId = chatId
                            if(chats.first().author == user?.username)
                                callRecords.calledName = chats.first().replyTo
                            else
                                callRecords.calledName = chats.first().author
                            callRecords.conferenceName = conferenceName
                            callRecords.timestampStarted = Date().toStringFormat()
                            presenter?.startCall(callRecords)
                            if (conferenceName.isNotEmpty()) {
                                val options =
                                    JitsiMeetConferenceOptions.Builder()
                                        .setRoom(conferenceName)
                                        .build()
                                JitsiMeetActivity.launch(activity, options)
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
        // Initialize default options for Jitsi Meet conferences.
        val serverURL: URL
        serverURL = try {
            URL("https://meet.jit.si")
        } catch (e: MalformedURLException) {
            e.printStackTrace()
            throw RuntimeException("Invalid server URL!")
        }
        val jitsiMeetUserInfo = JitsiMeetUserInfo()
        try {
            jitsiMeetUserInfo.avatar =
                URL("https://lh3.googleusercontent.com/-GR9eJoXnIcw/AAAAAAAAAAI/AAAAAAAAAAA/AKF05nAvdr7a2g5Zj8wIiCeOSPiualjlwg/photo.jpg?sz=46")
        } catch (e: MalformedURLException) {
            e.printStackTrace()
        }
        jitsiMeetUserInfo.displayName = "Andre android"
        val defaultOptions = JitsiMeetConferenceOptions.Builder()
            .setServerURL(serverURL)
            .setUserInfo(jitsiMeetUserInfo)
            .setWelcomePageEnabled(false)
            .build()
        JitsiMeet.setDefaultConferenceOptions(defaultOptions)
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
