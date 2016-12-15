package com.danilocianfrone.noty.views.controllers

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import android.widget.RadioGroup
import butterknife.BindView
import butterknife.OnClick

import com.danilocianfrone.noty.R
import com.danilocianfrone.noty.dagger.AppScope
import com.danilocianfrone.noty.models.Note
import com.danilocianfrone.noty.models.Priority
import com.squareup.leakcanary.RefWatcher
import io.realm.Realm
import java.util.*
import javax.inject.Inject

class NoteCreationController :
        BaseController(),
        Realm.Transaction {

    // App stuff
    @Inject @AppScope lateinit var realm: Realm
    @Inject @AppScope lateinit var refWatcher: RefWatcher

    @BindView(R.id.controller_creation_content)
    lateinit var content: EditText

    @BindView(R.id.controller_creation_priorities)
    lateinit var group: RadioGroup

    override fun inflateView(inflater: LayoutInflater, container: ViewGroup): View =
            inflater.inflate(R.layout.controller_note_creation, container, false)

    override fun onViewBind(view: View) {
        super.onViewBind(view)

        // Inject application object graph
        notyApplication.objectGraph.inject(this)
    }

    override fun handleBack(): Boolean =
            router.popCurrentController()   // Pop this shit out!

    override fun onDestroyView(view: View) {
        super.onDestroyView(view)
        refWatcher.watch(this)
    }

    override fun execute(realm: Realm) {
        val date = Date()
        val new  = realm.createObject(Note::class.java, date.time)
        new.creation = date
        new.content  = content.text.toString()
        new.priority = fromGroupToPriority()
    }

    /**
     * Execute abort Button onClick handling.
     * Specifically, it acts like a back navigation button.
     */
    @OnClick(R.id.controller_creation_abort) fun abortButton() { handleBack() }

    /**
     * Execute ok Button onClick handling.
     * Execute a Realm write async transaction, then navigates back to NoteListController.
     */
    @OnClick(R.id.controller_creation_ok) fun okButton() {
        realm.executeTransactionAsync(this); handleBack()
    }

    private fun fromGroupToPriority(): Priority =
            when (group.checkedRadioButtonId) {
                R.id.controller_creation_veryhigh -> Priority.VERY_HIGH
                R.id.controller_creation_high     -> Priority.HIGH
                R.id.controller_creation_medium   -> Priority.MEDIUM
                R.id.controller_creation_low      -> Priority.LOW
                R.id.controller_creation_verylow  -> Priority.VERY_LOW
                else -> throw Exception("there can't be different ids aside from them")
            }

    companion object {
        private const val TAG = "NoteCreationController"
    }
}