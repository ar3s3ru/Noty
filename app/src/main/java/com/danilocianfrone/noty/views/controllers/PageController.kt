package com.danilocianfrone.noty.views.controllers

import android.os.Bundle
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup

import javax.inject.Inject
import butterknife.BindView
import com.danilocianfrone.noty.Names
import com.danilocianfrone.noty.Noty

import com.danilocianfrone.noty.R
import com.danilocianfrone.noty.dagger.AppScope
import com.danilocianfrone.noty.models.Note
import com.danilocianfrone.noty.models.Priority
import com.danilocianfrone.noty.presenters.NotePresenter
import com.danilocianfrone.noty.presenters.Presenter
import com.danilocianfrone.noty.views.recyclers.NoteListAdapter

// TODO: implement save controller state
//
// TODO: ControllerPagerAdapter shows 3 controller children at a time, so you need to adjust
//       the Presenter and its takeView
class PageController(args: Bundle) : BaseController(), NotePresenter.Presentable {

    @BindView(R.id.controller_page_recycler) lateinit var recycler: RecyclerView

    @Inject @AppScope lateinit var presenter: NotePresenter
    private lateinit var adapter: NoteListAdapter
    private lateinit var updater: Presenter.Updater

    internal val priority: Priority = Priority.FromValue(args.getInt(Names.PRIORITY, 0))
    private val TAG = "PageController_${priority.name}"

    override fun inflateView(inflater: LayoutInflater, container: ViewGroup): View =
        inflater.inflate(R.layout.controller_note_page, container, false)

    override fun onViewBound(view: View) {
        super.onViewBound(view)

        // Create object graph
        notyApplication.objectGraph.inject(this)

        adapter = NoteListAdapter()

        recycler.layoutManager = LinearLayoutManager(view.context)
        recycler.adapter = adapter
    }

    override fun onAttach(view: View) {
        super.onAttach(view)
        Log.i(TAG, "Taking view")
        updater = presenter.TakeView(this) // Take adapter as presentable view
        updater.RequestUpdate()              // Request first updater
    }

    override fun onDetach(view: View) {
        super.onDetach(view)
        Log.i(TAG, "Releasing view")
        presenter.ReleaseView(updater)
    }

    override fun ofTarget(): Priority = priority

    override fun onUpdateView(data: List<Note>) {
        adapter.dataset = data
        adapter.notifyDataSetChanged()
    }

    override fun onUpdateError(throwable: Throwable) {
        throwable.printStackTrace()
    }

    override fun onDetach() {
        Log.i(TAG, "View released")
    }
}