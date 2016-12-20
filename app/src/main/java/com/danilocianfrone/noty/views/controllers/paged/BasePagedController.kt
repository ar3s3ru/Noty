package com.danilocianfrone.noty.views.controllers.paged

import android.os.Bundle
import android.support.v4.widget.SwipeRefreshLayout
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.view.View
import com.danilocianfrone.noty.dagger.AppScope
import com.danilocianfrone.noty.models.Note
import com.danilocianfrone.noty.presenters.AbstractPresenter
import com.danilocianfrone.noty.presenters.NotePresenter
import com.danilocianfrone.noty.views.controllers.BaseController
import com.danilocianfrone.noty.views.recyclers.AbstractNoteList
import com.squareup.leakcanary.RefWatcher
import javax.inject.Inject


abstract class BasePagedController<A : AbstractNoteList> :
        BaseController,
        SwipeRefreshLayout.OnRefreshListener {

    abstract var recycler: RecyclerView
    abstract var swiper:   SwipeRefreshLayout
    abstract var adapter:  A

    @Inject @AppScope lateinit var refWatcher: RefWatcher
    @Inject @AppScope lateinit var presenter:  NotePresenter

    constructor()             : super()
    constructor(args: Bundle) : super(args)

    override fun onViewBind(view: View) {
        super.onViewBind(view)
        // Bind the object graph here
        onBindObjectGraph()

        recycler.layoutManager = LinearLayoutManager(view.context)
        recycler.adapter       = adapter

        swiper.setOnRefreshListener(this)
    }

    // TODO: implement BasePagedController multibinding and implement this function
    abstract protected fun onBindObjectGraph()

    override fun onAttach(view: View) {
        super.onAttach(view)
        // Take adapter as presentable view
        presenter.TakeView(adapter)
    }

    override fun onDetach(view: View) {
        super.onDetach(view)
        presenter.ReleaseView(adapter)
    }

    override fun onSaveViewState(view: View, outState: Bundle) {
        super.onSaveViewState(view, outState)
        outState.putParcelable(LAYOUT_MANAGER, recycler.layoutManager.onSaveInstanceState())
    }

    override fun onRestoreViewState(view: View, savedViewState: Bundle) {
        super.onRestoreViewState(view, savedViewState)
        recycler.layoutManager.onRestoreInstanceState(savedViewState.getParcelable(LAYOUT_MANAGER))
    }

    override fun onDestroyView(view: View) {
        super.onDestroyView(view)
        refWatcher.watch(this)
    }

    // When a refresh starts from SwipeRefreshLayout, send an update request to the adapter
    override fun onRefresh() { adapter.notifyUpdate() }

    internal companion object {
        private const val LAYOUT_MANAGER = "BasePagedController.layoutManager"
    }
}