package com.danilocianfrone.noty.views.controllers

import android.os.Bundle
import android.support.v4.widget.SwipeRefreshLayout
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import butterknife.BindView
import com.bluelinelabs.conductor.RouterTransaction
import com.bluelinelabs.conductor.changehandler.FadeChangeHandler
import com.danilocianfrone.noty.singleton.Names
import com.danilocianfrone.noty.R
import com.danilocianfrone.noty.dagger.AppScope
import com.danilocianfrone.noty.dagger.PageControllerModule
import com.danilocianfrone.noty.dagger.PageControllerScope
import com.danilocianfrone.noty.models.Priority
import com.danilocianfrone.noty.withInt
import com.danilocianfrone.noty.presenters.NotePresenter
import com.danilocianfrone.noty.singleton.ControllerFactory
import com.danilocianfrone.noty.views.recyclers.NoteListAdapter
import com.squareup.leakcanary.RefWatcher
import javax.inject.Inject

/**
 * [BaseController] subtype that display and manage a [RecyclerView] displaying [NoteListAdapter],
 * controlled by the application-level instance of [NotePresenter]
 *
 * Implements [NoteListAdapter.Listener] for dataset update callbacks and
 * [SwipeRefreshLayout.OnRefreshListener] for update requesting listening
 */
class PageController(args: Bundle) :
        BaseController(args),
        NoteListAdapter.Listener,
        SwipeRefreshLayout.OnRefreshListener {

    @BindView(R.id.controller_page_recycler) lateinit var recycler: RecyclerView
    @BindView(R.id.controller_page_swipe)    lateinit var swiper:   SwipeRefreshLayout

    @Inject @AppScope lateinit var refWatcher: RefWatcher
    @Inject @AppScope lateinit var presenter: NotePresenter

    @Inject @PageControllerScope lateinit var adapter: NoteListAdapter
    @Inject @PageControllerScope lateinit var priority: Priority

    private val TAG by lazy { "PageController_${priority.name}" }
    private val objectGraph by lazy {
        notyApplication.objectGraph.plusPageControllerComponent()
                .withModule(PageControllerModule(this))
                .build()
    }

    override fun inflateView(inflater: LayoutInflater, container: ViewGroup): View =
        inflater.inflate(R.layout.controller_note_page, container, false)

    override fun onViewBind(view: View) {
        super.onViewBind(view)

        // Create object graph
        objectGraph.inject(this)

        recycler.layoutManager = LinearLayoutManager(view.context)
        recycler.adapter       = adapter

        swiper.setOnRefreshListener(this)
    }

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

    override fun onClickedElement() {
        // TODO: change this and add a Dialog or a NoteContentController
        // Push a new FastCreationController, for now
        router.pushController(
                RouterTransaction.with(ControllerFactory.provideFastCreationController())
                        .popChangeHandler(FadeChangeHandler())
                        .pushChangeHandler(FadeChangeHandler())
        )
    }

    override fun onRefreshed() {
        // If the NoteListAdapter has finished its refreshing, and it comes from
        // the SwipeRefreshLayout, set refreshing to false ('cause it's finished, now)
        if (swiper.isRefreshing) { swiper.isRefreshing = false }
    }

    // When a refresh starts from SwipeRefreshLayout, send an update request to the adapter
    override fun onRefresh() { adapter.notifyUpdate() }

    internal companion object {
        private const val LAYOUT_MANAGER = "PageController.LayoutManager"

        /**
         * Factory method for [PageController] that specifies the [Priority] target value:
         * [PageController] will display notes with [Priority] value used.
         *
         * @param priority [Priority] target value to use
         * @return New [PageController] instance
         */
        fun with(priority: Priority): PageController = with(priority.Value())

        /**
         * Factory method for [PageController].
         * Similar to [PageController.Companion.with] but uses an [Int] value rather than a [Priority].
         *
         * @param priorityValue [Int] value of [Priority] target
         * @return New [PageController] instance
         */
        fun with(priorityValue: Int): PageController =
            PageController(
                    Bundle().withInt(Names.PRIORITY, priorityValue)
            )
    }
}
