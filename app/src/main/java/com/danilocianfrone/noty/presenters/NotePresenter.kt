package com.danilocianfrone.noty.presenters

import com.danilocianfrone.noty.models.Note
import io.realm.Realm
import io.realm.Sort

/**
 * Presenter used for all the views that needs Note as their models.
 */
class NotePresenter(private val realm: Realm) : AbstractPresenter<MutableList<Note>>() {

    override fun onDeliver(view: IPresentable<MutableList<Note>>): MutableList<Note>? =
            when (view) {
                is NotePresentable.Priorited -> {
                    realm.where(Note::class.java)
                            .equalTo("priorityVal", view.withPriority().Value())
                            .findAllSorted("creation", Sort.DESCENDING)
                }
                is NotePresentable.Queried -> {
                    realm.where(Note::class.java)
                            .contains("content", view.onQueryText())
                            .findAllSorted("creation", Sort.DESCENDING)
                }
                else -> null
            }

    companion object {
        private const val TAG = "NotePresenter"
    }
}
