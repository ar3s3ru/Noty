package com.danilocianfrone.noty.views.recyclers

import com.danilocianfrone.noty.doOnNullable
import com.danilocianfrone.noty.models.Note
import com.danilocianfrone.noty.models.Priority
import com.danilocianfrone.noty.presenters.NotePresentable
import io.realm.RealmResults
import java.lang.ref.WeakReference

class NoteListAdapter(listener: Listener, priority: Priority) :
        AbstractNoteList(),
        NotePresentable.Priorited {

    interface Listener {
        fun onClickedElement()
        fun onRefreshed()
    }

    // NB: DO NOT USE OR REFERENCE THIS!
    private var mListener: WeakReference<Listener> = WeakReference(listener)
    private val mPriority = priority

    private val TAG: String = "NoteListAdapter_$priority"

    fun notifyInsertion(data: Note): Unit =
            doOnNullable(
                    mDataset,
                    { it.add(0, data); notifyItemInserted(0) },
                    { notifyUpdate() }
            )

    override fun withPriority(): Priority = mPriority

    override fun onUpdateView(data: RealmResults<Note>) {
        super.onUpdateView(data)
        mDataset?.let { mListener.get().onRefreshed() }
    }
}
