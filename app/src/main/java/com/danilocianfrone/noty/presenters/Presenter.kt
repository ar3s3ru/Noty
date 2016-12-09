package com.danilocianfrone.noty.presenters

import android.support.v7.widget.RecyclerView

interface Presenter<out T> {

    fun TakeView(view: Presenter.Presentable<T>)
    fun ReleaseView(view: Presenter.Presentable<T>)
    fun onDeliver(view: Presenter.Presentable<T>): T?

    interface Presentable<in T> {
        fun onUpdateView(data: T)
        fun onUpdateError(throwable: Throwable)
        fun notifyUpdate()
    }
}

abstract class AbstractPresenter<out T> : Presenter<T> {

    open protected fun publish(view: Presenter.Presentable<T>) {
        val data = onDeliver(view)
        if (data != null) {
            view.onUpdateView(data)
        } else {
            view.onUpdateError(Exception("No new data found"))
        }
    }

    companion object {
        abstract class RecyclerPresentable<T, VH : RecyclerView.ViewHolder>
            : Presenter.Presentable<T>, RecyclerView.Adapter<VH>() {

            internal var presenter: AbstractPresenter<T>? = null

            override fun notifyUpdate() {
                presenter?.publish(this)
            }
        }
    }
}

//abstract class SinglePresenter<T> : Presenter<T>() {
//
//    protected var bindedView: Presentable<T>? = null
//
//    override fun ReleaseView(view: Presentable<T>) =
//        // TODO: maybe use another exception?
//        if (view == bindedView) { bindedView = null } else { throw IllegalArgumentException() }
//
//    override fun TakeView(view: Presentable<T>) =
//        // TODO: maybe use another exception?
//        if (bindedView != null) { throw IllegalStateException() } else { bindedView = view }
//
//    private fun publish() {
//        try {
//            bindedView?.let {
//                val newValues = onDeliver(it)
//                newValues?.let { bindedView!!.onUpdateView(it) }
//            }
//        }
//        catch (t: Throwable) { bindedView?.onUpdateError(t) }
//    }
//}
//
//abstract class MultiplePresenter<T> : Presenter<T> {
//
//    // TODO: implement a proper pool
//    private val mMap: MutableMap<Updater, Presentable<T>> = mutableMapOf()
//
//    override fun TakeView(view: Presentable<T>): Presenter.Updater {
//        if (mMap.containsValue(view)) { throw Exception() }
//        val updater = Updater()
//        mMap.put(updater, view)
//        return updater
//    }
//
//    override fun ReleaseView(updater: Presenter.Updater) {
//        val view = mMap.remove(updater)
//        view?.onDetach()
//    }
//
//    private fun publish(updater: Updater) {
//        val view = mMap[updater]
//
//        if (view != null) {
//            // TODO: treat the NullPointerException from it.onUpdateView(...)
//            try { val newValues = onDeliver(view); view.onUpdateView(newValues!!) }
//            catch (t: Throwable) { view.onUpdateError(t) }
//        }
//    }
//
//    private inner class Updater() : Presenter.Updater {
//        override fun RequestUpdate() { publish(this) }
//    }
//}