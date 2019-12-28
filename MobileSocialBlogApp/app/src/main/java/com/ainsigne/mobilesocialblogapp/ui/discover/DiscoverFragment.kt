package com.ainsigne.mobilesocialblogapp.ui.discover

import android.os.Bundle
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
        if(user?.friendsRequestId?.filter { it != bUser.id } != null)
            user?.friendsRequestId = user?.friendsRequestId?.filter { it != bUser.id } as ArrayList<String>
        if(user?.friendsInviteid?.filter { it != bUser.id } != null)
            user?.friendsInviteid = user?.friendsInviteid?.filter { it != bUser.id } as ArrayList<String>
        if(bUser.friendsRequestId?.filter { it != user?.id } != null)
            bUser.friendsRequestId = bUser.friendsRequestId?.filter { it != user?.id } as ArrayList<String>
        if(bUser.friendsInviteid?.filter { it != user?.id } != null)
            bUser.friendsInviteid = bUser.friendsInviteid?.filter { it != user?.id } as ArrayList<String>
        user?.let {
            presenter?.updateUsers(it,bUser)
        }
    }

    override fun addFriendClicked(bUser: Users) {
        user?.friendsRequestId?.let {
            bUser.id?.let {id ->
                it.add(id)
            }
        }
        user?.friendsInviteid?.let {
            bUser.id?.let {id ->
                it.add(id)
            }
        }

        if(bUser.friendsInviteid == null){
            bUser.friendsInviteid = ArrayList<String>()
            user?.id?.let {
                bUser.friendsInviteid?.add(it)
            }
        }
        if(user?.friendsRequestId == null){
            user?.friendsRequestId = ArrayList<String>()
            bUser.id?.let {
                user?.friendsRequestId?.add(it)
            }
        }
        user?.let {
            presenter?.updateUsers(it,bUser)
        }

    }

    override fun acceptFriendClicked(bUser: Users) {
        user?.friendsRequestId = user?.friendsRequestId?.filter { it != bUser.id } as ArrayList<String>
        user?.friendsInviteid = user?.friendsInviteid?.filter { it != bUser.id } as ArrayList<String>
        bUser.friendsRequestId = bUser.friendsRequestId?.filter { it != user?.id } as ArrayList<String>
        bUser.friendsInviteid = bUser.friendsInviteid?.filter { it != user?.id } as ArrayList<String>
        if(user?.friendsId == null)
            user?.friendsId = ArrayList<String>()
        if(bUser.friendsId == null)
            bUser.friendsId = ArrayList<String>()
        bUser.id?.let {
            user?.friendsId?.add(it)
        }
        user?.id?.let {
            bUser.friendsId?.add(it)
        }
        user?.let {
            presenter?.updateUsers(it, bUser)
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
