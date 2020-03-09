package com.ainsigne.mobilesocialblogapp.ui.discover

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager

import com.ainsigne.mobilesocialblogapp.R
import com.ainsigne.mobilesocialblogapp.adapters.DiscoverAdapter
import com.ainsigne.mobilesocialblogapp.base.BaseFragment
import com.ainsigne.mobilesocialblogapp.models.Users
import com.ainsigne.mobilesocialblogapp.utils.Config
import kotlinx.android.synthetic.main.fragment_discover.*
import org.jetbrains.anko.doAsync
import org.jetbrains.anko.uiThread


/**
 * A simple [Fragment] subclass.

 * Use the [Discover.newInstance] factory method to
 * create an instance of this fragment.
 *
 */
class DiscoverFragment : BaseFragment(), DiscoverView {

    var isLoading = false

    var user : Users? = null

    override fun updatedUsersUpdateView() {
        presenter?.retrieveAll()
    }

    override fun retrievedAllUpdateView() {
        Config.getUser()?.let {
            user = presenter?.getUserFrom(it)
        }
        presenter?.allUsers()?.filter { it.username != Config.getUser() }?.let {users ->
            val layoutManager = LinearLayoutManager(this.context)
            rv_discover?.layoutManager = layoutManager
            doAsync {
                uiThread {
                    rv_discover?.adapter = DiscoverAdapter(users, it)
                }
            }
        }


    }

    override fun cancelFriendClicked(bUser: Users) {
        user?.let {
            presenter?.cancelFriend(it,bUser)
        }
    }

    override fun addFriendClicked(bUser: Users) {
        user?.let {
            presenter?.addFriend(it,bUser)
        }
    }

    override fun acceptFriendClicked(bUser: Users) {
        user?.let {
            presenter?.acceptFriend(it,bUser)
        }

    }

    /**
     * injector as DiscoverInjector injects the presenter with the services and data needed for the
     * implementation
     **/
    var injector = DiscoverInjectorImplementation()
    /**
     * presenter as DiscoverPresenter injected automatically to call implementations
     **/
    var presenter: DiscoverPresenter? = null


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        injector.inject(this)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_discover, container, false)


    }

    override fun isAlreadyFriend(bUser : Users) : Boolean{
        user?.friendsId?.let {
            return it.contains(bUser.id)
        }
        return false
    }

    override fun isAlreadyRequested(bUser : Users) : Boolean{
        user?.friendsRequestId?.let {
            return it.contains(bUser.id)
        }
        return false
    }

    override fun isAlreadyInvited(bUser : Users) : Boolean{
        user?.friendsInviteid?.let {
            return it.contains(bUser.id)
        }
        return false
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        presenter?.retrieveAll()
        isLoading = true
    }


    companion object {
        /**
         * Use this factory method to create a new instance of
         * this fragment using the provided parameters.
         *
         * @return A new instance of fragment Discover.
         */
        // TODO: Rename and change types and number of parameters
        @JvmStatic
        fun newInstance() =
            DiscoverFragment()
    }
}
