package com.danilocianfrone.noty.views.recyclers

import android.support.v7.widget.RecyclerView
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import android.widget.TextView
import butterknife.BindView
import butterknife.ButterKnife
import butterknife.Unbinder

import com.danilocianfrone.noty.R
import com.danilocianfrone.noty.models.Note
import com.danilocianfrone.noty.models.Priority
import com.danilocianfrone.noty.presenters.NotePresentable

class NoteListAdapter(priority: Priority)
    : NotePresentable<NoteListAdapter.Companion.ViewHolder>(priority), View.OnClickListener {

    private var dataset: List<Note>? = null

    override fun onUpdateView(data: List<Note>) {
        dataset?.let { notifyItemRangeRemoved(0, it.count()) }
        dataset = data
        notifyItemRangeChanged(0, dataset!!.count())
    }

    override fun onUpdateError(throwable: Throwable) {
        throwable.printStackTrace()
    }

    override fun getItemCount(): Int = dataset?.size ?: 1
    override fun getItemViewType(position: Int): Int =
            if (dataset == null) { DATASET_EMPTY } else { DATASET_FULL }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder =
        ViewHolder(
                LayoutInflater.from(parent.context)
                        .inflate((when (viewType) {
                            DATASET_EMPTY -> R.layout.adapter_note_list_empty
                            DATASET_FULL  -> R.layout.adapter_note_list
                            else -> throw IllegalArgumentException()
                        }), parent, false)
        )

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        dataset?.let {
            val item = it[position]

            if (holder.unbinder == null) {
                // Butterknife binding
                holder.unbinder = ButterKnife.bind(holder, holder.itemView)
            }

            Log.i(
                    "NoteListAdapter",
                    "Object ${item.priority}"
            )

            holder.layout.setBackgroundResource(item.priority.ColorBody())
            holder.content.text  = item.content
            holder.creation.text = item.creation.toString()
            holder.creation.setBackgroundResource(item.priority.ColorTop())
            holder.itemView.setOnClickListener(this)
        }
    }

    override fun onClick(p0: View?) {
        Log.i("NoteListAdapter", "Clicked for updating")
        notifyUpdate()
    }

    companion object {

        /** View types */
        private const val DATASET_EMPTY = 0
        private const val DATASET_FULL  = 1

        class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
            @BindView(R.id.adapter_note_view)     lateinit var layout:   LinearLayout
            @BindView(R.id.adapter_note_content)  lateinit var content:  TextView
            @BindView(R.id.adapter_note_creation) lateinit var creation: TextView

            var unbinder: Unbinder? = null
        }
    }
}
