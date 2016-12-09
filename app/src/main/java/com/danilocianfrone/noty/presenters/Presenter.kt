package com.danilocianfrone.noty.presenters

interface Presenter<T> {
    fun TakeView(view: Presentable<T>): Updater
    fun ReleaseView(updater: Updater)
    fun onDeliver(view: Presentable<T>): T?

    interface Updater {
        fun RequestUpdate()
    }
}

interface Presentable<in T> {
    fun onUpdateView(data: T)
    fun onUpdateError(throwable: Throwable)
    fun onDetach()
}

abstract class SinglePresenter<T> : Presenter<T>, Presenter.Updater {

    protected var bindedView: Presentable<T>? = null

    override fun ReleaseView(updater: Presenter.Updater) { bindedView = null }

    override fun TakeView(view: Presentable<T>): Presenter.Updater {
        if (bindedView == null) { throw Exception() }
        bindedView = view
        return this
    }

    override fun RequestUpdate() { publish() }

    private fun publish() {
        try {
            bindedView?.let {
                val newValues = onDeliver(it)
                newValues?.let { bindedView!!.onUpdateView(it) }
            }
        }
        catch (t: Throwable) { bindedView?.onUpdateError(t) }
    }
}

abstract class MultiplePresenter<T> : Presenter<T> {

    // TODO: implement a proper pool
    private val mMap: MutableMap<Updater, Presentable<T>> = mutableMapOf()

    override fun TakeView(view: Presentable<T>): Presenter.Updater {
        if (mMap.containsValue(view)) { throw Exception() }
        val updater = Updater()
        mMap.put(updater, view)
        return updater
    }

    override fun ReleaseView(updater: Presenter.Updater) {
        val view = mMap.remove(updater)
        view?.onDetach()
    }

    private fun publish(updater: Updater) {
        val view = mMap[updater]

        if (view != null) {
            // TODO: treat the NullPointerException from it.onUpdateView(...)
            try { val newValues = onDeliver(view); view.onUpdateView(newValues!!) }
            catch (t: Throwable) { view.onUpdateError(t) }
        }
    }

    private inner class Updater() : Presenter.Updater {
        override fun RequestUpdate() { publish(this) }
    }
}