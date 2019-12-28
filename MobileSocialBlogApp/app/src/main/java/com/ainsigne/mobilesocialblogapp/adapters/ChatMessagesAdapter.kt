package com.ainsigne.mobilesocialblogapp.adapters

import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.ainsigne.mobilesocialblogapp.R
import com.ainsigne.mobilesocialblogapp.models.ChatMessages
import com.ainsigne.mobilesocialblogapp.models.ChatSession
import com.ainsigne.mobilesocialblogapp.models.Posts
import com.ainsigne.mobilesocialblogapp.models.Users
import com.ainsigne.mobilesocialblogapp.ui.chat.ChatView
import com.ainsigne.mobilesocialblogapp.ui.chatsession.ChatSessionView
import com.ainsigne.mobilesocialblogapp.ui.discover.DiscoverView
import com.ainsigne.mobilesocialblogapp.ui.feed.FeedView
import com.bumptech.glide.Glide
import kotlinx.android.synthetic.main.adapter_chatfriend.view.*
import kotlinx.android.synthetic.main.adapter_chatsession.view.*
import kotlinx.android.synthetic.main.adapter_chatsession_reply.view.*

class ChatMessagesAdapter(chats_ : List<ChatMessages>, view_ : ChatSessionView) : RecyclerView.Adapter<ChatMessagesAdapter.FeedDataHolder>()  {

    val chats = chats_

    val adapterView = view_

    val replyView = 1

    override fun onCreateViewHolder(p0: ViewGroup, p1: Int): FeedDataHolder {
        var inflatedView: View?
        if(p1 == replyView){
            inflatedView = LayoutInflater.from(p0.context)
                .inflate(R.layout.adapter_chatsession, p0, false)
            return FeedDataHolder(inflatedView)
        }
        else
        {
            inflatedView = LayoutInflater.from(p0.context)
                .inflate(R.layout.adapter_chatsession_reply, p0, false)
            return FeedDataHolder(inflatedView)
        }

    }

    override fun getItemViewType(position: Int): Int {


        adapterView.currentUser()?.id?.let {
            chats[position].replyTo?.let { reply ->
                if(it.contains(reply)){
                    return replyView
                }
            }
        }
        return super.getItemViewType(position)
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
        fun bind(chat : ChatMessages, viewType : Int) {
            if(viewType == replyView){
                feedView.tv_chatsession_username.text = chat.author
                feedView.tv_chatsession_message.text = chat.message
                chat.author?.let { author ->
                    Glide.with(feedView.context).load(adapterView.userFrom(author)?.photoUrl).into(feedView.iv_chatsession_icon)
                }
            }
            else{
                feedView.tv_chatreply_username.text = chat.author
                feedView.tv_chatreply_message.text = chat.message
                chat.author?.let { author ->
                    Glide.with(feedView.context).load(adapterView.userFrom(author)?.photoUrl).into(feedView.iv_chatreply_icon)
                }
            }
        }
    }

    override fun onBindViewHolder(holder: FeedDataHolder, position: Int) {
        holder.bind(chats[position], holder.itemViewType)
    }
}