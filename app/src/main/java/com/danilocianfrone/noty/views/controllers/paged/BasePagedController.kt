package com.danilocianfrone.noty.views.controllers.paged

import android.content.SharedPreferences
import android.os.Bundle
import android.support.v4.widget.SwipeRefreshLayout
import android.support.v7.widget.GridLayoutManager
import android.support.v7.widget.RecyclerView
import android.view.MotionEvent
import android.view.ScaleGestureDetector
import android.view.View
import com.danilocianfrone.noty.dagger.AppScope
import com.danilocianfrone.noty.presenters.NotePresenter
import com.danilocianfrone.noty.singleton.PreferenceGetter
import com.danilocianfrone.noty.views.controllers.BaseController
import com.danilocianfrone.noty.views.recyclers.AbstractNoteList
import com.squareup.leakcanary.RefWatcher
import javax.inject.Inject

abstract class BasePagedController<A : AbstractNoteList> :
        BaseController,
        SwipeRefreshLayout.OnRefreshListener,
        ScaleGestureDetector.OnScaleGestureListener,
        View.OnTouchListener {

    abstract var mRecycler: RecyclerView
    abstract var mSwiper:   SwipeRefreshLayout
    abstract var mAdapter:  A

    @Inject @AppScope lateinit var refWatcher:   RefWatcher
    @Inject @AppScope lateinit var mPresenter:   NotePresenter
    @Inject @AppScope lateinit var mSharedPrefs: SharedPreferences

    protected lateinit var mScaleGestureDetector: ScaleGestureDetector
    protected var mColumnNumber = MIN_COLUMN

    constructor()             : super()
    constructor(args: Bundle) : super(args)

    override fun onViewBind(view: View) {
        super.onViewBind(view)

        // Bind the object graph here
        onBindObjectGraph()

        // Scale Gesture Detector
        mScaleGestureDetector = ScaleGestureDetector(view.context, this)

        mRecycler.adapter       = mAdapter
        mRecycler.layoutManager = GridLayoutManager(view.context, mColumnNumber)

        mRecycler.setOnTouchListener(this)

        // Controller mediates the refreshing between the RecyclerView Adapter and SwiperLayout
        mSwiper.setOnRefreshListener(this)
    }

    // TODO: implement BasePagedController multibinding and implement this function
    abstract protected fun onBindObjectGraph()

    override fun onAttach(view: View) {
        super.onAttach(view)
        // Take mAdapter as presentable view
        mPresenter.TakeView(mAdapter)
    }

    override fun onDetach(view: View) {
        super.onDetach(view)
        mPresenter.ReleaseView(mAdapter)
    }

    override fun onSaveViewState(view: View, outState: Bundle) {
        super.onSaveViewState(view, outState)
        outState.putParcelable(LAYOUT_MANAGER, mRecycler.layoutManager.onSaveInstanceState())
        outState.putInt(COLUMN_NUMBER, mColumnNumber)
    }

    override fun onRestoreViewState(view: View, savedViewState: Bundle) {
        super.onRestoreViewState(view, savedViewState)
        mRecycler.layoutManager.onRestoreInstanceState(savedViewState.getParcelable(LAYOUT_MANAGER))
        mColumnNumber = savedViewState.getInt(
                COLUMN_NUMBER,
                PreferenceGetter.columnNumber(mSharedPrefs, MIN_COLUMN)
        )

        gridLayoutManager().spanCount = mColumnNumber
    }

    override fun onDestroyView(view: View) {
        super.onDestroyView(view)
        refWatcher.watch(this)
    }

    // When a refresh starts from SwipeRefreshLayout, send an update request to the mAdapter
    override fun onRefresh() { mAdapter.notifyUpdate() }

    override fun onScaleBegin(detector: ScaleGestureDetector): Boolean = true

    override fun onScaleEnd(detector: ScaleGestureDetector) {
        val target = detector.currentSpan - detector.previousSpan
        when {
            // Zooming out
            target > 0f -> {
                if (gridLayoutManager().spanCount > MIN_COLUMN) gridLayoutManager().spanCount--
            }

            // Zooming in
            target < 0f -> {
                if (gridLayoutManager().spanCount < MAX_COLUMN) gridLayoutManager().spanCount++
            }

            else -> return
        }
    }

    override fun onScale(detector: ScaleGestureDetector): Boolean =
            detector.currentSpan > 200 && detector.timeDelta > 200

    override fun onTouch(view: View, event: MotionEvent): Boolean =
            mScaleGestureDetector.onTouchEvent(event)

    protected fun gridLayoutManager(): GridLayoutManager =
            mRecycler.layoutManager as GridLayoutManager

    internal companion object {
        private const val LAYOUT_MANAGER = "BasePagedController.layoutManager"
        private const val COLUMN_NUMBER  = "BasePagedController.columnNumber"

        private const val MIN_COLUMN = 2
        private const val MAX_COLUMN = 4
    }
}

