package com.danilocianfrone.noty.views.recyclers

import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup

import com.danilocianfrone.noty.R
import com.danilocianfrone.noty.models.Note
import com.danilocianfrone.noty.views.NoteView

class NoteListAdapter() : RecyclerView.Adapter<NoteListAdapter.ViewHolder>() {

    internal lateinit var dataset: List<Note>

    override fun getItemCount(): Int = dataset.size

    override fun onCreateViewHolder(parent: ViewGroup?, viewType: Int): ViewHolder =
        ViewHolder(
                LayoutInflater
                        .from(parent?.context)
                        .inflate(R.layout.adapter_note_list, parent, false)
        )

    override fun onBindViewHolder(holder: ViewHolder?, position: Int) {
        (holder?.itemView as NoteView).of(dataset[position])
    }

    companion object class ViewHolder(itemView: View)
        : RecyclerView.ViewHolder(itemView)
}
