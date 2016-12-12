package com.danilocianfrone.noty.views.controllers

import android.os.Bundle
import android.os.Parcelable
import android.support.design.widget.FloatingActionButton
import android.support.design.widget.TabLayout
import android.support.v4.view.ViewPager
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup

import butterknife.BindView

import com.bluelinelabs.conductor.Controller
import com.bluelinelabs.conductor.RouterTransaction
import com.bluelinelabs.conductor.changehandler.FadeChangeHandler
import com.bluelinelabs.conductor.support.ControllerPagerAdapter

import com.danilocianfrone.noty.R
import com.danilocianfrone.noty.dagger.ActivityScope
import com.danilocianfrone.noty.dagger.ListControllerModule
import com.danilocianfrone.noty.dagger.NoteControllerScope
import com.danilocianfrone.noty.models.Priority
import com.danilocianfrone.noty.views.NoteActivity

import javax.inject.Inject

class NoteListController : BaseController(), View.OnClickListener {

    @BindView(R.id.controller_note_pager)
    lateinit var pager: ViewPager

    @BindView(R.id.controller_note_tablayout)
    lateinit var tabLayout: TabLayout

    @BindView(R.id.controller_note_fab)
    lateinit var fabNew: FloatingActionButton

    @Inject @ActivityScope lateinit var noteCreation: NoteCreationController
    @Inject @NoteControllerScope lateinit var pagerAdapter: PagerAdapter

    // Dagger object graph
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
        objectGraph.inject(this)

        // Setup pager and tab layout
        pager.adapter = pagerAdapter
        tabLayout.setupWithViewPager(pager, true)

        // Add onClick callback
        fabNew.setOnClickListener(this)
    }

    override fun onSaveViewState(view: View, outState: Bundle) {
        outState.putParcelable(PAGER, pager.onSaveInstanceState())
        outState.putInt(TAB_LAOUT, tabLayout.selectedTabPosition)
    }

    override fun onRestoreViewState(view: View, savedViewState: Bundle) {
        tabLayout.getTabAt(savedViewState.getInt(TAB_LAOUT))?.select()
        pager.onRestoreInstanceState(
                savedViewState.getParcelable(PAGER)
        )
    }

    override fun onClick(view: View) {
        if (view == fabNew) {
            // Shows up NoteCreation
            router.pushController(
                    RouterTransaction.with(noteCreation)
                            .popChangeHandler(FadeChangeHandler())
                            .pushChangeHandler(FadeChangeHandler())
            )
        }
    }

    override fun handleBack(): Boolean {
        return super.handleBack()
    }

    companion object {
        private const val TAG = "NoteListController"
        private const val PAGER = "$TAG.Pager"
        private const val TAB_LAOUT = "$TAG.TabLayout"
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
