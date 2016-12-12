package com.danilocianfrone.noty.views

import android.os.Bundle
import butterknife.BindView
import com.bluelinelabs.conductor.ChangeHandlerFrameLayout
import com.bluelinelabs.conductor.Conductor
import com.bluelinelabs.conductor.Router
import com.bluelinelabs.conductor.RouterTransaction
import com.danilocianfrone.noty.R
import com.danilocianfrone.noty.dagger.AppScope
import com.danilocianfrone.noty.singleton.ControllerFactory
import com.squareup.leakcanary.RefWatcher
import javax.inject.Inject

class NoteActivity : BaseActivity() {

    @BindView(R.id.activity_note_root) lateinit var noteActivityRoot: ChangeHandlerFrameLayout

    @Inject @AppScope lateinit var refWatcher: RefWatcher

    private lateinit var conductorRouter: Router

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // Inject object graph
        notyApplication.objectGraph.plus(this)

        // Attach Conductor root
        conductorRouter = Conductor.attachRouter(this, noteActivityRoot, savedInstanceState)
        if (!conductorRouter.hasRootController()) {
            conductorRouter.setRoot(
                    RouterTransaction.with(ControllerFactory.provideNoteListController())
            )
        }
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

    companion object {
        const val TAG = "NoteActivity"
    }
}