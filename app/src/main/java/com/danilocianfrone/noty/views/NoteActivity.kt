package com.danilocianfrone.noty.views

import android.os.Bundle
import butterknife.BindView
import com.bluelinelabs.conductor.ChangeHandlerFrameLayout
import com.bluelinelabs.conductor.Conductor
import com.bluelinelabs.conductor.Router
import com.bluelinelabs.conductor.RouterTransaction
import com.bluelinelabs.conductor.changehandler.FadeChangeHandler
import com.danilocianfrone.noty.R
import com.danilocianfrone.noty.dagger.ActivityScope
import com.danilocianfrone.noty.dagger.AppScope
import com.danilocianfrone.noty.dagger.NoteActivityModule
import com.danilocianfrone.noty.views.controllers.NoteCreationController
import com.danilocianfrone.noty.views.controllers.NoteListController
import com.squareup.leakcanary.RefWatcher
import javax.inject.Inject

class NoteActivity : BaseActivity(), NoteListController.Listener {

    @BindView(R.id.activity_note_root) lateinit var noteActivityRoot: ChangeHandlerFrameLayout

    @Inject @AppScope lateinit var refWatcher: RefWatcher

    private lateinit var conductorRouter: Router

    @Inject @ActivityScope lateinit var noteCreation: NoteCreationController
    @Inject @ActivityScope lateinit var listController: NoteListController

    private val noteCreationTransaction: RouterTransaction by lazy {
        RouterTransaction.with(noteCreation)
                .popChangeHandler(FadeChangeHandler())
                .pushChangeHandler(FadeChangeHandler())
    }

    private val noteListTransaction: RouterTransaction by lazy {
        RouterTransaction.with(listController)
    }

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
        if (!conductorRouter.hasRootController()) { conductorRouter.setRoot(noteListTransaction) }
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        conductorRouter.saveInstanceState(outState)
    }

    override fun onSetContentView() {
        setContentView(R.layout.activity_note)
    }

    override fun onBackPressed() {
        if (!conductorRouter.handleBack()) {
            super.onBackPressed()
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        refWatcher.watch(this)  // Spots memory leaks on destroying
    }

    override fun onAddButtonClick() {
        conductorRouter.setRoot(noteCreationTransaction)
    }

    companion object {
        const val TAG = "NoteActivity"
    }
}