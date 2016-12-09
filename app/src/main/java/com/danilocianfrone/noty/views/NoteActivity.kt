package com.danilocianfrone.noty.views

import android.os.Bundle
import android.os.PersistableBundle
import android.view.ViewGroup
import butterknife.BindView
import butterknife.ButterKnife
import butterknife.Unbinder
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

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // Inject object graph
        notyApplication.objectGraph.plusNoteActivity()
                .withModule(NoteActivityModule(this))
                .build()
                .inject(this)

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