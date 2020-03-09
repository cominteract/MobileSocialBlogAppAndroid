package com.ainsigne.mobilesocialblogapp.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.ainsigne.mobilesocialblogapp.R
import com.ainsigne.mobilesocialblogapp.models.Posts
import com.ainsigne.mobilesocialblogapp.ui.feed.FeedView
import com.ainsigne.mobilesocialblogapp.ui.profile.ProfileView
import com.ainsigne.mobilesocialblogapp.utils.fromNow
import com.ainsigne.mobilesocialblogapp.utils.hoursFromNow
import com.ainsigne.mobilesocialblogapp.utils.toDate
import com.bumptech.glide.Glide
import kotlinx.android.synthetic.main.adapter_feed.view.*

class FeedAdapter(posts_ : List<Posts>, view_ : FeedView?, pview_ : ProfileView?) : RecyclerView.Adapter<FeedAdapter.FeedDataHolder>()  {

    val posts = posts_

    val adapterView = view_

    val profileView = pview_

    override fun onCreateViewHolder(p0: ViewGroup, p1: Int): FeedDataHolder {
        val inflatedView = LayoutInflater.from(p0.context)
            .inflate(R.layout.adapter_feed, p0, false)
        return FeedDataHolder(inflatedView)
    }

    override fun getItemCount(): Int {

        return posts.size
    }


    /**
     * FeedDataHolder as custom view holder which will define and bind the
     * data of the app to be displayed on the view
     * @param feedView_ as View that contains information of the feed such as details , subreddit name and the content
     */
    open inner class FeedDataHolder(feedView_: View) : RecyclerView.ViewHolder(feedView_) {

        val feedView = feedView_
        fun bind(post : Posts ) {
            feedView.tv_feed_title.text = post.title
            feedView.tv_user_name.text = post.author
            feedView.tv_body.text = post.body
            feedView.tv_timestamp.text = post.timestamp?.toDate()?.fromNow()

            val votes = post.upvotes - post.downvotes
            feedView.tv_num_upvotes.text = votes.toString()
            Glide.with(feedView.context).load(post.url).into(feedView.iv_feed_display_image)

            adapterView?.let {feed ->

                feedView.setOnClickListener {
                    post.id?.let { id ->
                        feed.navigateToDetails(id)
                    }
                }

                feedView.tv_user_name.setOnClickListener {
                    post.author?.let {friend ->
                        feed.navigateToProfile(friend)
                    }
                }
                feedView.iv_feed_icon.setOnClickListener {
                    post.author?.let {friend ->
                        feed.navigateToProfile(friend)
                    }
                }

                feedView.iv_upvote_icon.setOnClickListener {
                    feed.upvotePost(post)
                }
                feedView.iv_downvote_icon.setOnClickListener {
                    feed.downvotePost(post)
                }

                post.author?.let { author ->
                    Glide.with(feedView.context).load(feed.userFrom(author)?.photoUrl).into(feedView.iv_feed_icon)
                }
            }

            profileView?.let {profile ->

                feedView.setOnClickListener {
                    post.id?.let { id ->
                        profile.navigateToPost(post)
                    }
                }
                feedView.iv_upvote_icon.setOnClickListener {
                    profile.upvotePost(post)
                }
                feedView.iv_downvote_icon.setOnClickListener {
                    profile.downvotePost(post)
                }

                post.author?.let { author ->
                    Glide.with(feedView.context).load(profile.userFrom(author)?.photoUrl).into(feedView.iv_feed_icon)
                }
            }


        }
    }

    override fun onBindViewHolder(holder: FeedDataHolder, position: Int) {
        holder.bind(posts[position])
    }
}