package com.danilocianfrone.noty.views.controllers

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import android.widget.TextView
import butterknife.BindView
import butterknife.OnClick
import com.danilocianfrone.noty.R

class FastCreationController : BaseController() {

    @BindView(R.id.controller_fast_title) lateinit var title: TextView
    @BindView(R.id.controller_fast_content) lateinit var content: EditText

    override fun inflateView(inflater: LayoutInflater, container: ViewGroup): View =
            inflater.inflate(R.layout.controller_fast_creation, container, false)

    override fun onViewBind(view: View) {
        super.onViewBind(view)
    }

    override fun handleBack(): Boolean =
            router.popCurrentController()   // Pop this shit out!

    @OnClick(R.id.controller_fast_add)    fun addClicked() { }
    @OnClick(R.id.controller_fast_cancel) fun cancelClicked() { handleBack() }
}