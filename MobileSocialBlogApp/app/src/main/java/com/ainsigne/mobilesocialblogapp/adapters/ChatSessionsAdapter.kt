package com.ainsigne.mobilesocialblogapp.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.ainsigne.mobilesocialblogapp.R
import com.ainsigne.mobilesocialblogapp.models.ChatSession
import com.ainsigne.mobilesocialblogapp.ui.chat.ChatView
import com.ainsigne.mobilesocialblogapp.utils.Config
import com.ainsigne.mobilesocialblogapp.utils.fromNow
import com.ainsigne.mobilesocialblogapp.utils.toDate
import com.bumptech.glide.Glide
import kotlinx.android.synthetic.main.adapter_chatfriend.view.*

class ChatSessionsAdapter(chats_ : List<ChatSession>, view_ : ChatView) : RecyclerView.Adapter<ChatSessionsAdapter.FeedDataHolder>()  {

    val chats = chats_

    val adapterView = view_

    override fun onCreateViewHolder(p0: ViewGroup, p1: Int): FeedDataHolder {
        val inflatedView = LayoutInflater.from(p0.context)
            .inflate(R.layout.adapter_chatfriend, p0, false)
        return FeedDataHolder(inflatedView)
    }

    override fun getItemCount(): Int {

        return chats.size
    }


    /**
     * FeedDataHolder as custom view holder which will define and bind the
     * data of the app to be displayed on the view
     * @param feedView_ as View that contains information of the feed such as details , subreddit name and the content
     */
    open inner class FeedDataHolder(feedView_: View) : RecyclerView.ViewHolder(feedView_) {

        val feedView = feedView_
        fun bind(chat : ChatSession) {

            feedView.tv_chatfriend_location.text = Config.user?.location
            feedView.tv_chatfriend_username.text = chat.author

            feedView.tv_chatfriend_timestamp.text = chat.timestamp?.toDate()?.fromNow()



            feedView.setOnClickListener{
                adapterView.currentUser()?.id?.let {id ->
                    chat.userIds?.filter { it != id }?.let {filtered ->
                        if(filtered.isNotEmpty())
                            adapterView.navigateToSession(filtered[0])
                    }
                }
            }
            chat.author?.let { author ->



                Glide.with(feedView.context).load(adapterView.userFrom(author)?.photoUrl).into(feedView.iv_chatfriend_icon)
            }

        }
    }

    override fun onBindViewHolder(holder: FeedDataHolder, position: Int) {
        holder.bind(chats[position])
    }
}