package com.danilocianfrone.noty.views.controllers.paged

import android.support.v4.widget.SwipeRefreshLayout
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import butterknife.BindView
import com.danilocianfrone.noty.R
import com.danilocianfrone.noty.dagger.SearchControllerModule
import com.danilocianfrone.noty.dagger.SearchControllerScope
import com.danilocianfrone.noty.views.recyclers.NoteSearchAdapter
import javax.inject.Inject

class NoteSearchController() : BasePagedController<NoteSearchAdapter>() {

    @BindView(R.id.controller_search_recycler)
    override lateinit var mRecycler: RecyclerView

    @BindView(R.id.controller_search_swipe)
    override lateinit var mSwiper: SwipeRefreshLayout

    @Inject @SearchControllerScope
    override lateinit var mAdapter: NoteSearchAdapter

    private val objectGraph by lazy {
        notyApplication.objectGraph.plusSearchControllerComponent()
                .withModule(SearchControllerModule(this))
                .build()
    }

    override fun inflateView(inflater: LayoutInflater, container: ViewGroup): View =
            inflater.inflate(R.layout.controller_note_search, container, false)

    override fun onBindObjectGraph() {
        objectGraph.inject(this)
    }
}