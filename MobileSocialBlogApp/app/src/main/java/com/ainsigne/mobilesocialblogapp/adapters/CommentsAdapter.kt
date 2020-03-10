package com.ainsigne.mobilesocialblogapp.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.ainsigne.mobilesocialblogapp.R
import com.ainsigne.mobilesocialblogapp.models.Comments
import com.ainsigne.mobilesocialblogapp.models.Posts
import com.ainsigne.mobilesocialblogapp.models.Users
import com.ainsigne.mobilesocialblogapp.ui.discover.DiscoverView
import com.ainsigne.mobilesocialblogapp.ui.feed.FeedView
import com.ainsigne.mobilesocialblogapp.ui.feeddetails.FeedDetailsView
import com.ainsigne.mobilesocialblogapp.utils.Config
import com.ainsigne.mobilesocialblogapp.utils.fromNow
import com.ainsigne.mobilesocialblogapp.utils.toDate
import com.bumptech.glide.Glide
import kotlinx.android.synthetic.main.adapter_comment.view.*
import kotlinx.android.synthetic.main.adapter_discover.view.*
import kotlinx.android.synthetic.main.adapter_feed.view.*

class CommentsAdapter(comments_ : List<Comments>, post_ : Posts, view_ : FeedDetailsView) : RecyclerView.Adapter<CommentsAdapter.FeedDataHolder>()  {

    val comments = comments_

    val adapterView = view_

    val post = post_

    val headerView = 1

    override fun onCreateViewHolder(p0: ViewGroup, p1: Int): FeedDataHolder {

        val inflatedView : View?

        if(headerView == p1){
            inflatedView = LayoutInflater.from(p0.context)
                .inflate(R.layout.adapter_feed, p0, false)
            return FeedDataHolder(inflatedView)
        }
        else{
            inflatedView = LayoutInflater.from(p0.context)
                .inflate(R.layout.adapter_comment, p0, false)
            return FeedDataHolder(inflatedView)
        }

    }

    override fun getItemCount(): Int {

        return comments.size + 1
    }


    override fun getItemViewType(position: Int): Int {
        if(position == 0)
            return headerView
        return super.getItemViewType(position)
    }

    /**
     * FeedDataHolder as custom view holder which will define and bind the
     * data of the app to be displayed on the view
     * @param feedView_ as View that contains information of the feed such as details , subreddit name and the content
     */
    open inner class FeedDataHolder(feedView_: View) : RecyclerView.ViewHolder(feedView_) {

        val feedView = feedView_
        fun bind(comment : Comments) {

//            feedView.tv_discover_location.text = user.location
//            feedView.tv_discover_username.text = user.username
//            Glide.with(feedView.context).load(user.photoUrl).into(feedView.iv_discover_icon)

            feedView.tv_comment_message.text = comment.message
            feedView.tv_comment_timestamp.text = comment.timestamp?.toDate()?.fromNow()
            feedView.tv_comment_username.text = comment.author
            comment.author?.let {

                Glide.with(feedView.context).load(adapterView.userFrom(it)?.photoUrl).into(feedView.iv_comment_icon)
            }
        }
        fun bind(post : Posts ) {
            feedView.tv_feed_title.text = post.title
            feedView.tv_user_name.text = post.author
            feedView.tv_body.text = post.body
            feedView.tv_timestamp.text = post.timestamp?.toDate()?.fromNow()
            val votes = post.upvotes - post.downvotes
            feedView.tv_num_upvotes.text = votes.toString()

            feedView.iv_upvote_icon.setOnClickListener {  adapterView.upvotePost(post) }
            feedView.iv_downvote_icon.setOnClickListener {  adapterView.downvotePost(post) }
            Glide.with(feedView.context).load(post.url).into(feedView.iv_feed_display_image)
            post.author?.let { author ->
                Glide.with(feedView.context).load(adapterView.userFrom(author)?.photoUrl).into(feedView.iv_feed_icon)
            }

        }
    }

    override fun onBindViewHolder(holder: FeedDataHolder, position: Int) {
        if(holder.itemViewType == headerView)
            holder.bind(post)
        else
            holder.bind(comments[position - 1])
    }
}