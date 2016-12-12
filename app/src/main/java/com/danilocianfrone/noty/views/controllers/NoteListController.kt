package com.danilocianfrone.noty.views.controllers

import android.os.Bundle
import android.support.design.widget.FloatingActionButton
import android.support.design.widget.TabLayout
import android.support.v4.view.ViewPager
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup

import butterknife.BindView

import com.bluelinelabs.conductor.Controller
import com.bluelinelabs.conductor.RouterTransaction
import com.bluelinelabs.conductor.changehandler.FadeChangeHandler
import com.bluelinelabs.conductor.support.ControllerPagerAdapter

import com.danilocianfrone.noty.R
import com.danilocianfrone.noty.dagger.*
import com.danilocianfrone.noty.models.Priority
import com.danilocianfrone.noty.views.NoteActivity
import com.squareup.leakcanary.RefWatcher

import javax.inject.Inject

class NoteListController : BaseController(), View.OnClickListener {

    interface Listener {
        fun onAddButtonClick()
    }

    @BindView(R.id.controller_note_pager)
    lateinit var pager: ViewPager

    @BindView(R.id.controller_note_tablayout)
    lateinit var tabLayout: TabLayout

    @BindView(R.id.controller_note_fab)
    lateinit var fabNew: FloatingActionButton

    @Inject @AppScope lateinit var refWatcher: RefWatcher
    @Inject @NoteControllerScope lateinit var pagerAdapter: PagerAdapter

    // Dagger object graph
    private var injected = false
    internal val objectGraph by lazy {
        (activity as NoteActivity).objectGraph.plusNoteController()
                .withModule(ListControllerModule(this))
                .build()
    }

    override fun inflateView(inflater: LayoutInflater, container: ViewGroup): View =
        inflater.inflate(R.layout.controller_note_list, container)

    override fun onViewBound(view: View) {
        super.onViewBound(view)

        // Inject object graph
        injectEventually(objectGraph)

        // Setup pager and tab layout
        pager.adapter = pagerAdapter
        tabLayout.setupWithViewPager(pager)

        // Add onClick callback
        fabNew.setOnClickListener(this)
    }

    override fun onClick(view: View) {
        if (view == fabNew) {
            // Shows up NoteCreation (NoteActivity MUST implement Listener)
            (activity as Listener).onAddButtonClick()
        }
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

    private fun injectEventually(objectGraph: NoteControllerComponent) {
        if (!injected) {
            objectGraph.inject(this)
            injected = true
        }
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
