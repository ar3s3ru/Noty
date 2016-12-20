package com.danilocianfrone.noty.views.recyclers

import com.danilocianfrone.noty.presenters.NotePresentable

class NoteSearchAdapter() :
        AbstractNoteList(),
        NotePresentable.Queried {

    override fun onQueryText(): String =
            TODO("Add a query here")

    companion object {
        /** View types */
        private const val DATASET_EMPTY = 0
        private const val DATASET_FULL  = 1
    }
}