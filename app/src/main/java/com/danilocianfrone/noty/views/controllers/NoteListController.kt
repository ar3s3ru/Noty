package com.danilocianfrone.noty.views.controllers

import android.os.Bundle
import android.support.design.widget.TabLayout
import android.support.v4.view.ViewPager
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup

import butterknife.BindView
import com.bluelinelabs.conductor.Controller
import com.bluelinelabs.conductor.support.ControllerPagerAdapter
import com.danilocianfrone.noty.Names

import com.danilocianfrone.noty.R

class NoteListController : BaseController() {

    @BindView(R.id.controller_note_pager)
    lateinit var pager: ViewPager

    @BindView(R.id.controller_note_tablayout)
    lateinit var tabLayout: TabLayout

    private val bundles = arrayOf(
            Bundle(), Bundle(), Bundle(), Bundle(), Bundle()
    )

    private lateinit var pages: Array<PageController>

    init {
        // Put bundles values
        for (i in 0 until bundles.size) {
            Log.i(TAG, "Writing bundle index=${i}")
            bundles[i].putInt(Names.PRIORITY, i)
        }

        pages = arrayOf(
                PageController(bundles[0]),
                PageController(bundles[1]),
                PageController(bundles[2]),
                PageController(bundles[3]),
                PageController(bundles[4])
        )
    }

    override fun inflateView(inflater: LayoutInflater, container: ViewGroup): View =
        inflater.inflate(R.layout.controller_note_list, container)

    override fun onViewBound(view: View) {
        super.onViewBound(view)

        // TODO: implement save controller state
        pager.adapter = PagerAdapter(this, false, pages)
        tabLayout.setupWithViewPager(pager, true)
    }

    companion object {
        const val TAG = "NoteListController"
    }

    // TODO: refactor this to be static
    inner private class PagerAdapter(
            controller: NoteListController,
            saveControllerState: Boolean,
            val pages: Array<PageController>
    ) :
            ControllerPagerAdapter(controller, saveControllerState) {

        override fun getItem(position: Int): Controller = pages[position]
        override fun getCount(): Int = pages.size
        override fun getPageTitle(position: Int): CharSequence =
                "${pages[position].priority.String()} Priority"
    }
}
