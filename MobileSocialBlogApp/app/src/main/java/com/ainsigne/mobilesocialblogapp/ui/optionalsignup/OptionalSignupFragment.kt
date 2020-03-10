package com.ainsigne.mobilesocialblogapp.ui.optionalsignup

import android.graphics.Bitmap
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup

import com.ainsigne.mobilesocialblogapp.R
import com.ainsigne.mobilesocialblogapp.base.BaseFragment
import com.ainsigne.mobilesocialblogapp.models.Users
import com.ainsigne.mobilesocialblogapp.ui.main.MainActivity
import com.ainsigne.mobilesocialblogapp.ui.main.PhotoRetrieval
import com.ainsigne.mobilesocialblogapp.utils.Config
import kotlinx.android.synthetic.main.content_main.*
import kotlinx.android.synthetic.main.fragment_optional_signup.*
import org.jetbrains.anko.doAsync
import org.jetbrains.anko.uiThread


/**
 * A simple [Fragment] subclass.

 * Use the [OptionalSignup.newInstance] factory method to
 * create an instance of this fragment.
 *
 */
class OptionalSignupFragment : BaseFragment(), OptionalSignupView, PhotoRetrieval {




    override fun updatedUserUpdateView() {
        if(selectedPhoto == ""){
            progress_circular.visibility = View.GONE
            main.toggleBottomSheet()
            main.navigateApp()
            // move to main
        }
    }

    override fun uploadedImageUpdateView() {
        user?.let { updatedUser ->
            presenter?.updateUser(updatedUser, false)
        }
        selectedPhoto = ""

        // move to main
    }

    override fun retrievedAllUpdateView() {

    }

    /**
     * injector as OptionalSignupInjector injects the presenter with the services and data needed for the
     * implementation
     **/
    var injector = OptionalSignupInjectorImplementation()
    /**
     * presenter as OptionalSignupPresenter injected automatically to call implementations
     **/
    var presenter: OptionalSignupPresenter? = null

    lateinit var main : MainActivity

    var selectedPhoto = ""

    var user : Users? = null



    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        injector.inject(this)
        if(this.context is MainActivity){
            main = this.context as MainActivity
            main.photoRetrieval = this
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_optional_signup, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        presenter?.retrieveAll()
        btn_optional_browse.setOnClickListener {
            main.toggleBottomSheet()
        }
        btn_optional_upload.setOnClickListener {
            if(et_optional_birthday.text.toString().isNotEmpty() &&  et_optional_location.text.toString().isNotEmpty()){

                Config.getUser()?.let {aUsername ->
                    user = presenter?.getUserFrom(aUsername)
                    user?.birthday = et_optional_birthday.text.toString()
                    user?.location = et_optional_location.text.toString()
                    user?.online = true
                    if(selectedPhoto.isNotEmpty()){
                        user?.photoUrl = selectedPhoto
                        progress_circular.visibility = View.VISIBLE
                        presenter?.uploadImage(selectedPhoto)
                    }
                    else {
                        user?.let { updatedUser ->
                            presenter?.updateUser(updatedUser, false)
                        }
                    }
                }
            }
        }
    }

    override fun photoRetrieved(uriString: String?, bitmap: Bitmap?) {
        uriString?.let {
            selectedPhoto = it
        }
        doAsync { uiThread {
            iv_optional_photo?.setImageBitmap(bitmap)
            main.toggleBottomSheet()
        } }

    }

    companion object {
        /**
         * Use this factory method to create a new instance of
         * this fragment using the provided parameters.
         *
         * @return A new instance of fragment OptionalSignup.
         */
        // TODO: Rename and change types and number of parameters
        @JvmStatic
        fun newInstance() =
            OptionalSignupFragment()
    }
}
