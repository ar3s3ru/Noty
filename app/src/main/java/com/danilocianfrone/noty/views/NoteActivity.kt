package com.danilocianfrone.noty.views

import android.os.Bundle
import android.view.ViewGroup
import butterknife.BindView
import com.bluelinelabs.conductor.Conductor
import com.bluelinelabs.conductor.Router
import com.bluelinelabs.conductor.RouterTransaction
import com.danilocianfrone.noty.R
import com.danilocianfrone.noty.dagger.NoteActivityModule
import com.danilocianfrone.noty.views.controllers.NoteListController
import javax.inject.Inject

class NoteActivity : BaseActivity() {

    @BindView(R.id.activity_note_root) lateinit var noteActivityRoot: ViewGroup
    private lateinit var conductorRouter: Router

    @Inject lateinit var listController: NoteListController

    // Dagger object graph
    internal val objectGraph by lazy {
        notyApplication.objectGraph.plusNoteActivity()
                .withModule(NoteActivityModule(this))
                .build()
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // Inject object graph
        objectGraph.inject(this)

        // Attach Conductor root
        conductorRouter = Conductor.attachRouter(this, noteActivityRoot, savedInstanceState)
        if (!conductorRouter.hasRootController()) {
            conductorRouter.setRoot(RouterTransaction.with(NoteListController()))
        }
    }

    override fun onSetContentView() {
        setContentView(R.layout.activity_note)
    }

    companion object {
        const val TAG = "NoteActivity"
    }
}