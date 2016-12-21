package com.danilocianfrone.noty.presenters

import com.danilocianfrone.noty.doOnNullable
import java.lang.ref.WeakReference

abstract class AbstractPresenter<T>() : IPresenter<T> {

    override fun TakeView(view: IPresentable<T>) {
        view.mPresenter = WeakReference(this)
        view.onAttach()
    }

    override fun ReleaseView(view: IPresentable<T>) {
        view.mPresenter = null
    }

    override fun publish(view: IPresentable<T>) {
        doOnNullable(
                onDeliver(view),
                { view.onUpdateView(it) },
                { view.onUpdateError(Exception("No new data loaded")) }
        )
    }
}
