package com.danilocianfrone.noty.views.controllers

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import butterknife.ButterKnife
import butterknife.Unbinder
import com.bluelinelabs.conductor.Controller
import com.danilocianfrone.noty.Noty

/**
 * Base [Controller] class used for concrete application-level [Controller] classes.
 *
 * It includes [ButterKnife] injection, [View] inflating through [BaseController.inflateView],
 * post [View] inflating setup with [BaseController.onViewBind] and a [Noty] application reference
 * for object graph building.
 */
abstract class BaseController : Controller {

    constructor() : super()
    constructor(args: Bundle) : super(args)

    protected var unbinder: Unbinder? = null
    protected val notyApplication: Noty by lazy { applicationContext as Noty }

    /**
     * Used to do [BaseController] [View] inflating.
     *
     * @param inflater [LayoutInflater] to use to do [View] inflating
     * @param container [ViewGroup] to which attach the inflated [View]
     * @return The inflated [View]
     */
    abstract protected fun inflateView(inflater: LayoutInflater, container: ViewGroup): View

    /**
     * Used to do post-inflation [View] setup.
     *
     * @param view Inflated view
     */
    open protected fun onViewBind(view: View) {
        // Do something here...
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup): View {
        // Inflate view
        val view = inflateView(inflater, container)
        // ButterKnife binder
        unbinder = ButterKnife.bind(this, view)
        // Do something with the view
        onViewBind(view)
        // Return the view created
        return view
    }

    override fun onDestroyView(view: View) {
        super.onDestroyView(view)
        // Unbind the unbinder (lol)
        unbinder?.unbind()
    }

    protected inline fun onlyOnBind(lambda: () -> Unit) {
        unbinder?.let { lambda() }
    }
}