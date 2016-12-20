package com.danilocianfrone.noty.presenters


interface IPresenter<T> {
    fun TakeView(view: IPresentable<T>)
    fun ReleaseView(view: IPresentable<T>)
    fun onDeliver(view: IPresentable<T>): T?

    fun publish(view: IPresentable<T>)
}