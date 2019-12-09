package com.sametyildirim.loginapp.presentation.base

import android.os.Bundle

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment

abstract class BaseFragment : Fragment(), FragmentNavigation.View {

    private lateinit var rootView: View
    private lateinit var navigationPresenter: FragmentNavigation.Presenter

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        rootView = inflater.inflate(getLayout(), container, false)
        return rootView
    }

    protected abstract fun getLayout(): Int

    override fun attachPresenter(presenter: FragmentNavigation.Presenter) {
        navigationPresenter = presenter
    }

}