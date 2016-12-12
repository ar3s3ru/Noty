package com.danilocianfrone.noty.views

import android.os.Bundle
import android.util.Log
import android.view.ViewGroup
import butterknife.BindView
import com.bluelinelabs.conductor.Conductor
import com.bluelinelabs.conductor.Router
import com.bluelinelabs.conductor.RouterTransaction
import com.danilocianfrone.noty.R
import com.danilocianfrone.noty.dagger.ActivityScope
import com.danilocianfrone.noty.dagger.AppScope
import com.danilocianfrone.noty.dagger.NoteActivityModule
import com.danilocianfrone.noty.views.controllers.NoteListController
import com.squareup.leakcanary.RefWatcher
import javax.inject.Inject

class NoteActivity : BaseActivity() {

    @BindView(R.id.activity_note_root) lateinit var noteActivityRoot: ViewGroup

    private lateinit var conductorRouter: Router
    @Inject @ActivityScope lateinit var listController: NoteListController
    @Inject @AppScope lateinit var refWatcher: RefWatcher

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
            conductorRouter.setRoot(RouterTransaction.with(listController))
        }
    }

    override fun onSetContentView() {
        setContentView(R.layout.activity_note)
    }

    override fun onBackPressed() {
//        // TODO: refactor this
//        Log.i(TAG, "Backstack size is ${conductorRouter.backstackSize}")
//        if (conductorRouter.backstackSize == 1 || !conductorRouter.handleBack()) {
//            super.onBackPressed()
//        }
        Log.i(TAG, "handleBack()")
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