package com.danilocianfrone.noty.views.widgets

import android.content.Context
import android.support.annotation.ColorInt
import android.support.annotation.ColorRes
import android.support.design.widget.TabLayout
import android.util.AttributeSet
import com.bluelinelabs.conductor.Controller
import com.bluelinelabs.conductor.support.ControllerPagerAdapter

/**
 *
 */
class CoolTabLayout : TabLayout {

    constructor(context: Context) : super(context)
    constructor(context: Context, attrs: AttributeSet) : super(context, attrs)
    constructor(context: Context, attrs: AttributeSet, defStyleAttr: Int) :
            super(context, attrs, defStyleAttr) {
        // TODO: do something here
    }

}

abstract class ColoredPagerAdapter(controller: Controller, saveControllerState: Boolean)
    : ControllerPagerAdapter(controller, saveControllerState) {
    @ColorRes abstract fun getColor(position: Int): Int
}
