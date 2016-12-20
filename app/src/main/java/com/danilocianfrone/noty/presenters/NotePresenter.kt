package com.danilocianfrone.noty.presenters

import com.danilocianfrone.noty.models.Note
import io.realm.Realm
import io.realm.RealmResults
import io.realm.Sort
import java.lang.ref.WeakReference

/**
 * Presenter used for all the views that needs Note as their models.
 */
class NotePresenter(private val realm: Realm) : AbstractPresenter<MutableList<Note>>() {

    override fun onBeforeTakeView(view: IPresentable<MutableList<Note>>) {
        if (view is NotePresentable) { view.mPresenter = WeakReference(this) }
    }

    override fun ReleaseView(view: IPresentable<MutableList<Note>>) {
        if (view is NotePresentable) { view.mPresenter = null }
    }

    override fun onDeliver(view: IPresentable<MutableList<Note>>): MutableList<Note>? =
            when (view) {
                is NotePresentable.Priorited -> {
                    realm.where(Note::class.java)
                            .equalTo("priorityVal", view.withPriority().Value())
                            .findAllSortedAsync("creation", Sort.DESCENDING)
                }
                is NotePresentable.Queried -> {
                    realm.where(Note::class.java)
                            .contains("content", view.onQueryText())
                            .findAllSortedAsync("creation", Sort.DESCENDING)
                }
                else -> null
            }

    override fun publish(view: IPresentable<MutableList<Note>>) {
        // Casting works because onDeliver() is using Realm, lol
        val future = onDeliver(view) as RealmResults<Note>
        future.addChangeListener {
            if (it != null) { view.onUpdateView(it) }
            else { view.onUpdateError(Exception("No new shit to show")) }   // TODO: change this
        }
    }

    companion object {
        private const val TAG = "NotePresenter"
    }
}
