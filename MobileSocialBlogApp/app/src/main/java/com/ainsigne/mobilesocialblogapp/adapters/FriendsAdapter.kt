package com.ainsigne.mobilesocialblogapp.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.ainsigne.mobilesocialblogapp.R
import com.ainsigne.mobilesocialblogapp.models.Posts
import com.ainsigne.mobilesocialblogapp.models.Users
import com.ainsigne.mobilesocialblogapp.ui.discover.DiscoverView
import com.ainsigne.mobilesocialblogapp.ui.feed.FeedView
import com.ainsigne.mobilesocialblogapp.ui.profile.ProfileView
import com.bumptech.glide.Glide
import kotlinx.android.synthetic.main.adapter_discover.view.*
import kotlinx.android.synthetic.main.adapter_feed.view.*
import kotlinx.android.synthetic.main.adapter_friends.view.*

class FriendsAdapter(users_ : List<Users>, view_ : ProfileView) : RecyclerView.Adapter<FriendsAdapter.FeedDataHolder>()  {


    val users = users_

    val adapterView = view_

    override fun onCreateViewHolder(p0: ViewGroup, p1: Int): FeedDataHolder {
        val inflatedView = LayoutInflater.from(p0.context)
            .inflate(R.layout.adapter_friends, p0, false)
        return FeedDataHolder(inflatedView)
    }

    override fun getItemCount(): Int {

        return users.size
    }


    /**
     * FeedDataHolder as custom view holder which will define and bind the
     * data of the app to be displayed on the view
     * @param feedView_ as View that contains information of the feed such as details , subreddit name and the content
     */
    open inner class FeedDataHolder(feedView_: View) : RecyclerView.ViewHolder(feedView_) {

        val feedView = feedView_
        fun bind(user : Users) {

            feedView.tv_friends_location.text = user.location
            feedView.tv_friends_username.text = user.username
            Glide.with(feedView.context).load(user.photoUrl).into(feedView.iv_friends_icon)

            feedView.setOnClickListener {
                user.username?.let {friend ->
                    adapterView.navigateToProfile(friend)
                }
            }

        }
    }

    override fun onBindViewHolder(holder: FeedDataHolder, position: Int) {
        holder.bind(users[position])
    }
}