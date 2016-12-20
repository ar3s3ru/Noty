package com.danilocianfrone.noty.views.controllers

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import android.widget.TextView
import butterknife.BindView
import butterknife.OnClick
import com.danilocianfrone.noty.R
import com.danilocianfrone.noty.dagger.AppScope
import com.danilocianfrone.noty.dagger.FastControllerModule
import com.danilocianfrone.noty.dagger.FastControllerScope
import com.danilocianfrone.noty.models.Note
import com.danilocianfrone.noty.models.Priority
import com.danilocianfrone.noty.singleton.Names
import com.danilocianfrone.noty.withInt
import io.realm.Realm
import java.util.*
import javax.inject.Inject

class FastCreationController(args: Bundle) :
        BaseController(args),
        Realm.Transaction {

    @BindView(R.id.controller_fast_title) lateinit var title: TextView
    @BindView(R.id.controller_fast_content) lateinit var content: EditText

    @Inject @AppScope lateinit var realm: Realm
    @Inject @FastControllerScope lateinit var priority: Priority

    private val objectGraph by lazy {
        notyApplication.objectGraph.plusFastControllerComponent()
                .withModule(FastControllerModule(this))
                .build()
    }

    override fun inflateView(inflater: LayoutInflater, container: ViewGroup): View =
            inflater.inflate(R.layout.controller_fast_creation, container, false)

    override fun onViewBind(view: View) {
        super.onViewBind(view)
        // Inject object graph
        objectGraph.inject(this)
    }

    override fun handleBack(): Boolean =
            router.popCurrentController()   // Pop this shit out!

    override fun execute(realm: Realm) {
        val date = Date()
        val new  = realm.createObject(Note::class.java, date.time)

        new.creation = date
        new.content  = content.text.toString()
        new.priority = priority
    }

    @OnClick(R.id.controller_fast_add)
    fun addClicked() { realm.executeTransaction(this); handleBack() }

    @OnClick(R.id.controller_fast_cancel)
    fun cancelClicked() { handleBack() }

    internal companion object Factory {

        /**
         * Factory method for [PageController] that specifies the [Priority] target value:
         * [PageController] will display notes with [Priority] value used.
         *
         * @param priority [Priority] target value to use
         * @return New [PageController] instance
         */
        fun with(priority: Priority): FastCreationController = with(priority.Value())

        /**
         * Factory method for [PageController].
         * Similar to [PageController.Companion.with] but uses an [Int] value rather than a [Priority].
         *
         * @param priorityValue [Int] value of [Priority] target
         * @return New [PageController] instance
         */
        fun with(priorityValue: Int): FastCreationController =
                FastCreationController(
                        Bundle().withInt(Names.PRIORITY, priorityValue)
                )
    }
}