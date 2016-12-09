package com.danilocianfrone.noty.views.controllers

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import butterknife.ButterKnife
import butterknife.Unbinder
import com.bluelinelabs.conductor.Controller
import com.danilocianfrone.noty.Noty
import kotlin.properties.Delegates

/**
 *
 */
abstract class BaseController : Controller() {

    private lateinit var unbinder: Unbinder
    protected val notyApplication: Noty by lazy { applicationContext as Noty }

    /**
     *
     */
    abstract protected fun inflateView(inflater: LayoutInflater, container: ViewGroup): View

    /**
     *
     */
    open protected fun onViewBound(view: View) {
        // Do something here...
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup): View {
        // Inflate view
        val view = inflateView(inflater, container)
        // ButterKnife binder
        unbinder = ButterKnife.bind(this, view)
        // Do something with the view
        onViewBound(view)

        return view
    }

    override fun onDestroyView(view: View) {
        super.onDestroyView(view)
        // Unbind the unbinder (lol)
        unbinder.unbind()
    }
}