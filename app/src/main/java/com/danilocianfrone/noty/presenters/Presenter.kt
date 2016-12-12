package com.danilocianfrone.noty.presenters

import android.support.v7.widget.RecyclerView

interface Presenter<out T> {
    fun TakeView(view: Presenter.Presentable<T>)
    fun ReleaseView(view: Presenter.Presentable<T>)
    fun onDeliver(view: Presenter.Presentable<T>): T?

    interface Presentable<in T> {
        fun onAttach()
        fun onUpdateView(data: T)
        fun onUpdateError(throwable: Throwable)
        fun notifyUpdate()
    }
}

abstract class AbstractPresenter<out T> : Presenter<T> {

    abstract protected fun onBeforeTakeView(view: Presenter.Presentable<T>)

    override fun TakeView(view: Presenter.Presentable<T>) {
        onBeforeTakeView(view)
        view.onAttach()
    }

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
