package com.danilocianfrone.noty.views.controllers

import android.os.Bundle
import android.support.design.widget.FloatingActionButton
import android.support.design.widget.TabLayout
import android.support.v4.view.ViewPager
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup

import butterknife.BindView
import butterknife.OnClick

import com.bluelinelabs.conductor.Controller
import com.bluelinelabs.conductor.RouterTransaction
import com.bluelinelabs.conductor.changehandler.FadeChangeHandler
import com.bluelinelabs.conductor.support.ControllerPagerAdapter

import com.danilocianfrone.noty.R
import com.danilocianfrone.noty.dagger.*
import com.danilocianfrone.noty.models.Priority
import com.danilocianfrone.noty.singleton.ControllerFactory
import com.danilocianfrone.noty.views.NoteActivity
import com.squareup.leakcanary.RefWatcher

import javax.inject.Inject

class NoteListController : BaseController() {

    @BindView(R.id.controller_note_pager)
    lateinit var pager: ViewPager

    @BindView(R.id.controller_note_tablayout)
    lateinit var tabLayout: TabLayout

    @BindView(R.id.controller_note_fab)
    lateinit var fabNew: FloatingActionButton

    @Inject @AppScope lateinit var refWatcher: RefWatcher

    override fun inflateView(inflater: LayoutInflater, container: ViewGroup): View =
        inflater.inflate(R.layout.controller_note_list_constraint, container, false)

    override fun onViewBound(view: View) {
        super.onViewBound(view)

        // Inject object graph
        notyApplication.objectGraph.plus(this)

        // Setup pager and tab layout
        pager.adapter = PagerAdapter(this, true, arrayOf(
                PageController.Companion.with(Priority.VERY_HIGH),
                PageController.Companion.with(Priority.HIGH),
                PageController.Companion.with(Priority.MEDIUM),
                PageController.Companion.with(Priority.LOW),
                PageController.Companion.with(Priority.VERY_LOW)
        ))

        tabLayout.setupWithViewPager(pager, true)
    }

    override fun onDestroyView(view: View) {
        super.onDestroyView(view)
        refWatcher.watch(this)      // Spots memory leaks on destroy
    }

    override fun onSaveViewState(view: View, outState: Bundle) {
        super.onSaveViewState(view, outState)
        outState.putParcelable(PAGER, pager.onSaveInstanceState())
    }

    override fun onRestoreViewState(view: View, savedViewState: Bundle) {
        super.onRestoreViewState(view, savedViewState)
        pager.onRestoreInstanceState(savedViewState.getParcelable(PAGER))
    }

    @OnClick(R.id.controller_note_fab) fun fabClicked() {
        router.pushController(
                RouterTransaction.with(ControllerFactory.provideNoteCreationController())
                        .popChangeHandler(FadeChangeHandler())
                        .pushChangeHandler(FadeChangeHandler())
        )
    }

    companion object {
        private const val TAG = "NoteListController"
        private const val PAGER = "$TAG.Pager"
    }
}

class PagerAdapter(controller: NoteListController,
                   saveControllerState: Boolean,
                   val pages: Array<PageController>)
    // --------------------------------------------------------
    : ControllerPagerAdapter(controller, saveControllerState) {

    override fun getItem(position: Int): Controller =
            pages[position]

    override fun getCount(): Int =
            pages.size

    override fun getPageTitle(position: Int): CharSequence =
            "${Priority.FromValue(position).String()} Priority"
}
