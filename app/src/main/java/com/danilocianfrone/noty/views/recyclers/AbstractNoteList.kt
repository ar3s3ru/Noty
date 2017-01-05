package com.danilocianfrone.noty.views.recyclers

import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.ViewGroup
import butterknife.ButterKnife
import com.danilocianfrone.noty.R
import com.danilocianfrone.noty.models.Note
import com.danilocianfrone.noty.onNull
import com.danilocianfrone.noty.presenters.IPresentable
import com.danilocianfrone.noty.presenters.IPresenter
import java.lang.ref.WeakReference

abstract class AbstractNoteList :
        RecyclerView.Adapter<NoteListViewHolder>(),
        IPresentable<MutableList<Note>> {

    override  var mPresenter: WeakReference<IPresenter<MutableList<Note>>>? = null
    protected var mDataset:   MutableList<Note>? = null

    override fun onAttach() =
            onNull(mDataset, { notifyUpdate() })

    override fun onUpdateError(throwable: Throwable) {
        throwable.printStackTrace()
    }

    override fun onUpdateView(data: MutableList<Note>) {
        if (data.size > 0) {
            mDataset = data
            notifyItemRangeChanged(0, data.size)
        }
    }

    override fun getItemCount(): Int = mDataset?.size ?: 1

    override fun getItemViewType(position: Int): Int =
            when (mDataset == null) {
                true  -> DATASET_EMPTY
                false -> DATASET_FULL
            }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): NoteListViewHolder =
            NoteListViewHolder(
                    LayoutInflater.from(parent.context).inflate(
                            when (viewType) {
                                DATASET_EMPTY -> R.layout.adapter_note_list_empty
                                DATASET_FULL  -> R.layout.adapter_note_list
                                else -> throw IllegalStateException()
                            },
                            parent,
                            false
                    )
            )

    override fun onBindViewHolder(holder: NoteListViewHolder, position: Int) {
        mDataset?.let {
            val item = it[position]

            if (holder.unbinder == null) {
                holder.unbinder = ButterKnife.bind(holder, holder.itemView)
            }

            holder.layout.setBackgroundResource(item.priority.ColorBody())
            holder.content.text  = item.content
            holder.creation.text = item.creation.toString()
            holder.creation.setBackgroundResource(item.priority.ColorTop())
        }
    }

    protected companion object {
        const val DATASET_EMPTY = 0
        const val DATASET_FULL  = 1
    }
}
