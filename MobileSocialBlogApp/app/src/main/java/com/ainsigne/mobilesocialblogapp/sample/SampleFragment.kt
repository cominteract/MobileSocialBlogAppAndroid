package com.ainsigne.mobilesocialblogapp.sample

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup

import com.ainsigne.mobilesocialblogapp.R
import com.ainsigne.mobilesocialblogapp.base.BaseFragment


/**
 * A simple [Fragment] subclass.

 * Use the [Sample.newInstance] factory method to
 * create an instance of this fragment.
 *
 */
class SampleFragment : BaseFragment(), SampleView {
    override fun addedUserUpdateView() {

    }

    override fun addedPostUpdateView() {

    }

    override fun addedCommentsUpdateView() {

    }

    /**
     * injector as SampleInjector injects the presenter with the services and data needed for the
     * implementation
     **/
    var injector = SampleInjectorImplementation()
    /**
     * presenter as SamplePresenter injected automatically to call implementations
     **/
    var presenter: SamplePresenter? = null


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        injector.inject(this)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_sample, container, true)

    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
    }


    companion object {
        /**
         * Use this factory method to create a new instance of
         * this fragment using the provided parameters.
         *
         * @return A new instance of fragment Sample.
         */
        // TODO: Rename and change types and number of parameters
        @JvmStatic
        fun newInstance() =
            SampleFragment()
    }
}
