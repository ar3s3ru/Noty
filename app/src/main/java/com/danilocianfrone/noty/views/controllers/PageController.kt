package com.danilocianfrone.noty.views.controllers

import android.os.Bundle
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import butterknife.BindView
import com.danilocianfrone.noty.BundleBuilder
import com.danilocianfrone.noty.Names
import com.danilocianfrone.noty.R
import com.danilocianfrone.noty.dagger.AppScope
import com.danilocianfrone.noty.dagger.PageControllerModule
import com.danilocianfrone.noty.dagger.PageControllerScope
import com.danilocianfrone.noty.models.Priority
import com.danilocianfrone.noty.presenters.NotePresenter
import com.danilocianfrone.noty.views.recyclers.NoteListAdapter
import javax.inject.Inject

// TODO: implement save controller state
//
// TODO: ControllerPagerAdapter shows 3 controller children at a time, so you need to adjust
//       the Presenter and its takeView
class PageController(args: Bundle) : BaseController(args) {

    constructor(priorityValue: Int) : this(
            BundleBuilder(Bundle())
                    .putInt(Names.PRIORITY, priorityValue)
                    .build()
    )

    @BindView(R.id.controller_page_recycler) lateinit var recycler: RecyclerView

    @Inject @AppScope
    lateinit var presenter: NotePresenter

    @Inject @PageControllerScope
    lateinit var adapter: NoteListAdapter

    @Inject @PageControllerScope
    lateinit var priority: Priority

    private val TAG by lazy { "PageController_${priority.name}" }
    private val objectGraph by lazy {
        (parentController as NoteListController).objectGraph.plusPageController()
                .withModule(PageControllerModule(this))
                .build()
    }

    override fun inflateView(inflater: LayoutInflater, container: ViewGroup): View =
        inflater.inflate(R.layout.controller_note_page, container, false)

    override fun onViewBound(view: View) {
        super.onViewBound(view)

        // Create object graph
        objectGraph.inject(this)

        recycler.layoutManager = LinearLayoutManager(view.context)
        recycler.adapter = adapter
    }

    override fun onAttach(view: View) {
        super.onAttach(view)
        Log.i(TAG, "Taking view")
        presenter.TakeView(adapter) // Take adapter as presentable view
    }

    override fun onDetach(view: View) {
        super.onDetach(view)
        Log.i(TAG, "Releasing view")
        presenter.ReleaseView(adapter)
    }
}
