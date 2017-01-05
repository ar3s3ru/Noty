package com.danilocianfrone.noty.views.controllers.paged

import android.os.Bundle
import android.support.v4.widget.SwipeRefreshLayout
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import butterknife.BindView
import com.bluelinelabs.conductor.RouterTransaction
import com.bluelinelabs.conductor.changehandler.FadeChangeHandler
import com.danilocianfrone.noty.R
import com.danilocianfrone.noty.dagger.PageControllerModule
import com.danilocianfrone.noty.dagger.PageControllerScope
import com.danilocianfrone.noty.models.Priority
import com.danilocianfrone.noty.presenters.NotePresenter
import com.danilocianfrone.noty.singleton.Names
import com.danilocianfrone.noty.views.controllers.BaseController
import com.danilocianfrone.noty.views.controllers.FastCreationController
import com.danilocianfrone.noty.views.recyclers.NoteListAdapter
import com.danilocianfrone.noty.withInt
import javax.inject.Inject

/**
 * [BaseController] subtype that display and manage a [RecyclerView] displaying [NoteListAdapter],
 * controlled by the application-level instance of [NotePresenter]
 *
 * Implements [NoteListAdapter.Listener] for dataset update callbacks and
 * [SwipeRefreshLayout.OnRefreshListener] for update requesting listening
 */
class PageController(args: Bundle) :
        BasePagedController<NoteListAdapter>(args),
        NoteListAdapter.Listener {

    @BindView(R.id.controller_page_recycler)
    override lateinit var mRecycler: RecyclerView

    @BindView(R.id.controller_page_swipe)
    override lateinit var mSwiper: SwipeRefreshLayout

    @Inject @PageControllerScope lateinit var priority:  Priority
    @Inject @PageControllerScope override lateinit var mAdapter: NoteListAdapter

    private val TAG by lazy { "PageController_${priority.name}" }
    private val objectGraph by lazy {
        notyApplication.objectGraph.plusPageControllerComponent()
                .withModule(PageControllerModule(this))
                .build()
    }

    override fun inflateView(inflater: LayoutInflater, container: ViewGroup): View =
        inflater.inflate(R.layout.controller_note_page, container, false)

    override fun onBindObjectGraph() {
        // Create object graph
        objectGraph.inject(this)
    }

    override fun onClickedElement() {
        // TODO: change this and add a Dialog or a NoteContentController
        // Push a new FastCreationController, for now
        router.pushController(
                RouterTransaction.with(FastCreationController.with(priority))
                        .popChangeHandler(FadeChangeHandler())
                        .pushChangeHandler(FadeChangeHandler())
        )
    }

    override fun onRefreshed() {
        // If the NoteListAdapter has finished its refreshing, and it comes from
        // the SwipeRefreshLayout, set refreshing to false ('cause it's finished, now)
        onlyOnBind {
            if (mSwiper.isRefreshing) { mSwiper.isRefreshing = false }
        }
    }

    internal companion object Factory {

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
