package com.danilocianfrone.noty.presenters

import java.lang.ref.WeakReference


interface IPresentable<T> {

    var mPresenter: WeakReference<IPresenter<T>>?

    fun onAttach()
    fun onUpdateView(data: T)
    fun onUpdateError(throwable: Throwable)

    fun notifyUpdate() {
        mPresenter?.get()?.publish(this)
    }
}