package com.danilocianfrone.noty.views.controllers

import android.support.design.widget.TabLayout
import android.support.v4.view.ViewPager
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import butterknife.BindView
import com.bluelinelabs.conductor.Controller
import com.bluelinelabs.conductor.support.ControllerPagerAdapter
import com.danilocianfrone.noty.R
import com.danilocianfrone.noty.dagger.ListControllerModule
import com.danilocianfrone.noty.dagger.NoteControllerScope
import com.danilocianfrone.noty.models.Priority
import com.danilocianfrone.noty.views.NoteActivity
import javax.inject.Inject

class NoteListController : BaseController() {

    @BindView(R.id.controller_note_pager)
    lateinit var pager: ViewPager

    @BindView(R.id.controller_note_tablayout)
    lateinit var tabLayout: TabLayout

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

        pager.adapter = pagerAdapter
        tabLayout.setupWithViewPager(pager, true)
    }

    companion object {
        const val TAG = "NoteListController"
    }
}

class PagerAdapter(
        controller: NoteListController,
        saveControllerState: Boolean,
        val pages: Array<PageController>
) :
        ControllerPagerAdapter(controller, saveControllerState) {

    override fun getItem(position: Int): Controller = pages[position]
    override fun getCount(): Int = pages.size
    override fun getPageTitle(position: Int): CharSequence =
            "${Priority.FromValue(position).String()} Priority"
}
