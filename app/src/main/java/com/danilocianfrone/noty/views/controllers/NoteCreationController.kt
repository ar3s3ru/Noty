package com.danilocianfrone.noty.views.controllers

import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.RadioGroup
import butterknife.BindView

import com.danilocianfrone.noty.R
import com.danilocianfrone.noty.dagger.AppScope
import com.danilocianfrone.noty.models.Note
import com.danilocianfrone.noty.models.Priority
import io.realm.Realm
import javax.inject.Inject

class NoteCreationController :
        BaseController(),
        Realm.Transaction,
        View.OnClickListener {

    // App stuff
    @Inject @AppScope lateinit var realm: Realm

    @BindView(R.id.controller_creation_content)
    lateinit var content: EditText

    @BindView(R.id.controller_creation_priorities)
    lateinit var group: RadioGroup

    @BindView(R.id.controller_creation_ok)
    lateinit var okButton: Button

    @BindView(R.id.controller_creation_abort)
    lateinit var abortButton: Button

    override fun inflateView(inflater: LayoutInflater, container: ViewGroup): View =
            inflater.inflate(R.layout.controller_note_creation, container, false)

    override fun onViewBound(view: View) {
        super.onViewBound(view)

        // Inject application object graph
        notyApplication.objectGraph.inject(this)

        // Set onClick callbacks
        okButton.setOnClickListener(this)
        abortButton.setOnClickListener(this)
    }

    override fun handleBack(): Boolean {
        Log.i(TAG, "handleBack()")
        return router.popController(this)
    }

    override fun onClick(p0: View) {
        when (p0) {
            okButton    -> { realm.executeTransactionAsync(this); handleBack() }
            abortButton -> { handleBack() }
            else        -> return
        }
    }

    override fun execute(realm: Realm) {
        val new = realm.createObject(Note::class.java)
        new.content  = content.text.toString()
        new.priority = fromGroupToPriority()
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