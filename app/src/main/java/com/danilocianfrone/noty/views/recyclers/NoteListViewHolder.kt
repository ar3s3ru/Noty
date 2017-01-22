package com.danilocianfrone.noty.views.recyclers

import android.support.v7.widget.RecyclerView
import android.view.View
import android.widget.LinearLayout
import android.widget.TextView
import butterknife.BindView
import butterknife.Unbinder
import com.danilocianfrone.noty.R
import com.danilocianfrone.noty.views.widgets.NoteLayout

class NoteListViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

    @BindView(R.id.adapter_note_view)     lateinit var layout: NoteLayout
    @BindView(R.id.adapter_note_content)  lateinit var content: TextView
    @BindView(R.id.adapter_note_creation) lateinit var creation: TextView

    var unbinder: Unbinder? = null
}
