package com.ainsigne.mobilesocialblogapp.ui.chatsession

import android.content.Intent
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
import com.ainsigne.mobilesocialblogapp.models.ChatMessages
import com.ainsigne.mobilesocialblogapp.models.ChatSession
import com.ainsigne.mobilesocialblogapp.models.Users
import com.ainsigne.mobilesocialblogapp.ui.main.MainActivity
import com.ainsigne.mobilesocialblogapp.utils.Config
import com.ainsigne.mobilesocialblogapp.utils.Constants
import com.ainsigne.mobilesocialblogapp.utils.toStringFormat
import kotlinx.android.synthetic.main.fragment_chat_session.*
import org.jetbrains.anko.doAsync
import org.jetbrains.anko.uiThread
import org.jitsi.meet.sdk.*
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

    var ongoingCall : CallRecords? = null

    var meetView : JitsiMeetView? = null

    lateinit var main : MainActivity


    override fun currentUser(): Users? {
        Config.getUser()?.let {
            return presenter?.getUserFrom(it)
        }
        return null
    }

    override fun userFrom(author: String): Users? {
        return presenter?.getUserFrom(author)
    }

    override fun endedCallUpdateView(callRecords: CallRecords) {
        ongoingCall = null
        callRecords.endedId = null
        presenter?.startCall(callRecords)
        meetView?.leave()
        callEndedView()
    }

    override fun receivedCallFromUpdateView(callRecords: CallRecords) {
        callRecords.conferenceName?.let {
            this.conferenceName = it
            this.ongoingCall = callRecords
        }
        if (conferenceName.isNotEmpty() && callRecords.callstate == Constants.CALLSTARTED) {
            callRecords.callstate = Constants.CALLONGOING
            presenter?.startCall(callRecords)
            ongoingCallView()

        }
    }

    private fun ongoingCallView(){

        doAsync {
            uiThread {
                val options =
                    JitsiMeetConferenceOptions.Builder()
                        .setRoom(conferenceName)
                        .build()
                container_meetview.visibility = View.VISIBLE
                rv_chatsession.visibility = View.GONE
                container_chatsession_send.visibility = View.GONE
                main.hideTab()
                meetView?.join(options)
            }
        }
    }

    private fun callEndedView(){

        doAsync {
            uiThread {
                container_meetview.visibility = View.GONE
                rv_chatsession.visibility = View.VISIBLE
                container_chatsession_send.visibility = View.VISIBLE
                main.showTab()
            }
        }
    }

    override fun addedChatMessageUpdateView() {

    }

    override fun addedChatSessionUpdateView() {
        presenter?.retrieveAll()
    }

    private fun initializeJitsi(){
        meetView?.listener = object : JitsiMeetViewListener {
            fun onConferenceFailed(data: Map<String, Any>) {
                Log.d(" Jitsi Fail "," Jitsi Fail ")
                callEndedView()
            }
            override fun onConferenceTerminated(p0: MutableMap<String, Any>?) {
                Log.d(" Jitsi Terminated "," Jitsi Terminated ")
                Log.d(" Leaving "," Leaving from same ")
                ongoingCall?.let {
                    it.callstate = Constants.CALLENDED
                    it.timestampEnded = Date().toStringFormat()
                    it.endedId = chatId
                    presenter?.startCall(it)
                    callEndedView()

                }
                ongoingCall = null
            }
            override fun onConferenceJoined(data: Map<String, Any>) {
                Log.d(" Jitsi Joined "," Jitsi Joined ")
            }
            fun onConferenceLeft(data: Map<String, Any>){
                Log.d(" Jitsi Left "," Jitsi Left ")
            }
            override fun onConferenceWillJoin(data: Map<String, Any>) {
                Log.d(" Jitsi Will Join "," Jitsi Will Join ")
            }
            fun onConferenceWillLeave(data: Map<String, Any>) {
                Log.d(" Jitsi Will Leave"," Jitsi Will Leave ")
            }
            fun onLoadConfigError(data: Map<String, Any>) {
                Log.d(" Config Error "," Config Error ")
            }
        }
    }

    private fun initializeJitsiServer(){
        Config.getUser()?.let { username->
            val serverURL: URL
            serverURL = try {
                URL("https://meet.jit.si")
            } catch (e: MalformedURLException) {
                e.printStackTrace()
                throw RuntimeException("Invalid server URL!")
            }
            val jitsiMeetUserInfo = JitsiMeetUserInfo()

            presenter?.getUserFrom(username)?.photoUrl?.let {photoUrl ->
                try {
                    jitsiMeetUserInfo.avatar =
                        URL(photoUrl)
                } catch (e: MalformedURLException) {
                    e.printStackTrace()
                }
            }
            jitsiMeetUserInfo.displayName = username
            val defaultOptions = JitsiMeetConferenceOptions.Builder()
                .setServerURL(serverURL)
                .setUserInfo(jitsiMeetUserInfo)
                .setWelcomePageEnabled(false)
                .build()
            JitsiMeet.setDefaultConferenceOptions(defaultOptions)
            initializeJitsi()
            presenter?.getUserFrom(username)?.id?.let {
                presenter?.retrieveCalls(it)
            }
        }
    }

    private fun initializeStartCall(user : Users?, chats : List<ChatMessages>)
    {
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
                    ongoingCall = CallRecords()
                    ongoingCall?.id = Constants.getRandomString(19)
                    ongoingCall?.callerId = user?.id
                    ongoingCall?.callerName = user?.username
                    ongoingCall?.calledId = chatId
                    if(chats.first().author == user?.username)
                        ongoingCall?.calledName = chats.first().replyTo
                    else
                        ongoingCall?.calledName = chats.first().author
                    ongoingCall?.conferenceName = conferenceName
                    ongoingCall?.timestampStarted = Date().toStringFormat()
                    ongoingCall?.callstate = Constants.CALLSTARTED
                    ongoingCall?.let { ongoingCall ->
                        presenter?.startCall(ongoingCall)
                    }
                    if (conferenceName.isNotEmpty())
                        ongoingCallView()
                }
            }
        }
    }

    override fun retrievedAllUpdateView() {
        initializeJitsiServer()
        Config.getUser()?.let {username ->
            val user = presenter?.getUserFrom(username)
            presenter?.allChats()?.filter {message ->
                (message.userId == user?.id && message.replyTo == chatId) ||
                        (message.userId == chatId && message.replyTo == user?.id)
            }?.let { chats ->
                val layoutManager = LinearLayoutManager(this.context)
                rv_chatsession.layoutManager = layoutManager
                initializeStartCall(user, chats)
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
        if(context is MainActivity)
            main = context as MainActivity
        // Initialize default options for Jitsi Meet conferences.
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_chat_session, container, false)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        arguments?.getString("chatId")?.let {
            chatId = it

        }
        presenter?.retrieveAll()
        meetView = JitsiMeetView(activity as MainActivity)
        container_meetview.addView(meetView)
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
