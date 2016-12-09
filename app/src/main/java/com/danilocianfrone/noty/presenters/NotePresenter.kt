package com.danilocianfrone.noty.presenters

import android.support.v7.widget.RecyclerView
import com.danilocianfrone.noty.models.Note
import com.danilocianfrone.noty.models.Priority
import io.realm.Realm

/**
 * Presenter used for all the views that needs Note as their models.
 */
class NotePresenter(private val realm: Realm) : AbstractPresenter<List<Note>>() {

    override fun TakeView(view: Presenter.Presentable<List<Note>>) {
        if (view is NotePresentable<*>) {
            view.presenter = this
            publish(view)
        }
    }

    override fun ReleaseView(view: Presenter.Presentable<List<Note>>) {
        if (view is NotePresentable<*>) {
            view.presenter = null
        }
    }

    override fun onDeliver(view: Presenter.Presentable<List<Note>>): List<Note>? =
            when (view) {
                !is NotePresentable<*> -> null
                else -> realm.where(Note::class.java)
                            .equalTo("priorityVal", view.target.Value())
                            .findAll()
            }
}

abstract class NotePresentable<VH : RecyclerView.ViewHolder>(internal val target: Priority)
    : AbstractPresenter.Companion.RecyclerPresentable<List<Note>, VH>()