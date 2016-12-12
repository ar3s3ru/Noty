package com.danilocianfrone.noty.presenters

import android.support.v7.widget.RecyclerView
import com.danilocianfrone.noty.models.Note
import com.danilocianfrone.noty.models.Priority
import io.realm.Realm
import io.realm.RealmResults
import io.realm.Sort
import java.lang.ref.WeakReference

/**
 * Presenter used for all the views that needs Note as their models.
 */
class NotePresenter(private val realm: Realm) : AbstractPresenter<MutableList<Note>>() {

    override fun onBeforeTakeView(view: Presenter.Presentable<MutableList<Note>>) {
        if (view is NotePresentable<*>) { view.presenter = WeakReference(this) }
    }

    override fun ReleaseView(view: Presenter.Presentable<MutableList<Note>>) {
        if (view is NotePresentable<*>) { view.presenter = null }
    }

    override fun onDeliver(view: Presenter.Presentable<MutableList<Note>>): MutableList<Note>? =
            when (view) {
                !is NotePresentable<*> -> null
                else -> {
                    realm.where(Note::class.java)
                            .equalTo("priorityVal", view.target.Value())
                            .findAllSortedAsync("creation", Sort.DESCENDING)
                }
            }

    override fun publish(view: Presenter.Presentable<MutableList<Note>>) {
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

abstract class NotePresentable<VH : RecyclerView.ViewHolder>(internal val target: Priority)
    : AbstractPresenter.Companion.RecyclerPresentable<MutableList<Note>, VH>()