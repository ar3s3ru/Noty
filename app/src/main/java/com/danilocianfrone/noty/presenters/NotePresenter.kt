package com.danilocianfrone.noty.presenters

import io.realm.Realm

import com.danilocianfrone.noty.models.Note
import com.danilocianfrone.noty.models.Priority
import com.danilocianfrone.noty.presenters.Presentable as SuperPresentable

/**
 * Presenter used for all the views that needs Note as their models.
 */
class NotePresenter(private val realm: Realm) : MultiplePresenter<List<Note>>() {

    override fun onDeliver(view: SuperPresentable<List<Note>>): List<Note>? =
        if (view is Presentable) {
            realm.where(Note::class.java)
                    .equalTo("priorityVal", view.ofTarget().Value())
                    .findAll()
        } else null

    interface Presentable : SuperPresentable<List<Note>> {
        fun ofTarget(): Priority
    }
}