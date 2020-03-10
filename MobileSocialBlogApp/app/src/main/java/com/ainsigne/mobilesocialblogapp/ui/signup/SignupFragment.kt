package com.ainsigne.mobilesocialblogapp.ui.signup

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup

import com.ainsigne.mobilesocialblogapp.R
import com.ainsigne.mobilesocialblogapp.base.BaseFragment
import com.ainsigne.mobilesocialblogapp.models.Users
import com.ainsigne.mobilesocialblogapp.ui.main.MainActivity
import com.ainsigne.mobilesocialblogapp.utils.Config
import com.ainsigne.mobilesocialblogapp.utils.Constants
import com.ainsigne.mobilesocialblogapp.utils.UINavigation
import kotlinx.android.synthetic.main.content_main.*
import kotlinx.android.synthetic.main.fragment_signup.*
import org.jetbrains.anko.doAsync
import org.jetbrains.anko.uiThread


/**
 * A simple [Fragment] subclass.

 * Use the [Signup.newInstance] factory method to
 * create an instance of this fragment.
 *
 */
class SignupFragment : BaseFragment(), SignupView {


    var user : Users? = Users()

    var viewCreated = false

    /**
     * injector as SignupInjector injects the presenter with the services and data needed for the
     * implementation
     **/
    var injector = SignupInjectorImplementation()
    /**
     * presenter as SignupPresenter injected automatically to call implementations
     **/
    var presenter: SignupPresenter? = null


    lateinit var main : MainActivity

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        injector.inject(this)
        if(this.context is MainActivity){
            main = this.context as MainActivity
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_signup, container, false)


    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        presenter?.retrieveAll()
        btn_login.setOnClickListener {
            val username = et_login_username.text.toString()
            val password = et_login_password.text.toString()
            presenter?.let {
                if(it.usernameExists(username) && it.passwordCorrect(username,password)){
                    Config.updateUser(username)
                    it.getUserFrom(username)?.let {letUser ->
                        user = letUser

                        user?.online?.let {online ->
                                when(online){
                                    !online && (letUser.birthday == null
                                            || letUser.photoUrl == Constants.defaultuserurl || letUser.location == null) -> main.navigateOpt()
                                    online -> {
                                        Config.user = letUser
                                        main.navigateApp()
                                    }
                                    else -> {
                                        user?.online = true
                                        it.signupUser(user!!)
                                    }
                                }
                            }
                        if(user?.online == null)
                        {
                            user?.online = false
                            main.navigateOpt()
                        }
                    }
                }
            }
        }
        btn_signup.setOnClickListener {
            val username = et_signup_username.text.toString()
            val password = et_signup_password.text.toString()
            val exist = presenter?.usernameExists(username)
            if(isSame() && exist != null && !exist && presenter?.getUserFrom(username) == null){

                user?.let {signupUser ->
                    signupUser.id = Constants.getRandomString(22)
                    signupUser.firstname = et_signup_firstname.text.toString()
                    signupUser.lastname = et_signup_lastname.text.toString()
                    signupUser.fullname = "${signupUser.firstname} ${signupUser.lastname}"
                    signupUser.username = username
                    signupUser.password = password
                    Config.updateUser(username)
                    presenter?.signupUser(signupUser)
                }

            }
        }
    }

    override fun retrievedAllUpdateView() {
        Config.getUser()?.let {
            Config.user = presenter?.getUserFrom(it)
            doAsync { uiThread {
                Config.user?.let {currentUser ->
                    if(currentUser.location == null || currentUser.birthday == null)
                        main.navigateOpt()
                    else
                        main.navigateApp()
                }
                if(Config.user == null)
                    main.navigateApp()

            } }
        }
    }

    fun isSame() : Boolean {
        return et_signup_confirmpassword.text.toString().toLowerCase() == et_signup_password.text.toString().toLowerCase()
    }

    override fun onResume() {
        super.onResume()
        if(viewCreated && Config.getUser().isNullOrEmpty()){
            presenter?.retrieveAll()
        }
    }

    override fun addedUserUpdateView() {
        val online = user?.online
        Config.user = user
        if(online != null && online)
        {
            main.navigateApp()
            // move main
        }
        else
        {
            main.navigateOpt()
            // move optional
        }
    }


    companion object {
        /**
         * Use this factory method to create a new instance of
         * this fragment using the provided parameters.
         *
         * @return A new instance of fragment Signup.
         */
        // TODO: Rename and change types and number of parameters
        @JvmStatic
        fun newInstance() =
            SignupFragment()
    }
}
