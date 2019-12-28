package com.ainsigne.mobilesocialblogapp

import com.ainsigne.mobilesocialblogapp.manager.MockAPIManager
import com.ainsigne.mobilesocialblogapp.manager.MockAuthManager
import com.ainsigne.mobilesocialblogapp.models.Posts
import com.ainsigne.mobilesocialblogapp.models.Users
import com.ainsigne.mobilesocialblogapp.ui.feed.FeedPresenter
import com.ainsigne.mobilesocialblogapp.ui.feed.FeedPresenterImplementation
import com.ainsigne.mobilesocialblogapp.ui.feed.FeedView
import org.junit.Test

import org.junit.Assert.*

/**
 * Example local unit test, which will execute on the development machine (host).
 *
 * See [testing documentation](http://d.android.com/tools/testing).
 */
class PresenterUnitTest {
    lateinit var presenter : FeedPresenterImplementation
    val currentUser = Users.users()[0]
    var view = object : FeedView{
        override fun addedPostUpdateView() {
            assert(true)
        }

        override fun addedCommentsUpdateView() {
            assert(true)

        }

        override fun retrievedAllUpdateView() {
            assert(true)
        }

        override fun downloadedImageUpdateView(image: String) {
            assert(true)
        }

        override fun uploadedImageUpdateView() {
            assert(true)
        }

        override fun upvotePost(post: Posts) {
            assert(true)
        }

        override fun downvotePost(post: Posts) {
            assert(true)
        }

        override fun currentUser(): Users? {
            return currentUser
        }

        override fun navigateToDetails(postId: String) {
        }

        override fun navigateToProfile(username: String) {
        }

        override fun userFrom(author: String): Users? {
            return currentUser
        }

    }
    init {
        presenter = FeedPresenterImplementation(view, MockAPIManager(),MockAuthManager())
    }
    @Test
    fun retrieveAll() {
        presenter.retrieveAll()
    }
}
