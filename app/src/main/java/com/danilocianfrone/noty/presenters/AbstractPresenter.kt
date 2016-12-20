package com.danilocianfrone.noty.presenters

import com.danilocianfrone.noty.doOnNullable


abstract class AbstractPresenter<T>() : IPresenter<T> {

    abstract protected fun onBeforeTakeView(view: IPresentable<T>)

    override fun TakeView(view: IPresentable<T>) {
        onBeforeTakeView(view)
        view.onAttach()
    }

    override fun publish(view: IPresentable<T>) {
        doOnNullable(
                onDeliver(view),
                { view.onUpdateView(it) },
                { view.onUpdateError(Exception("No new data loaded")) }
        )
    }
}
