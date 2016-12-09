package com.danilocianfrone.noty.views

import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import android.util.Log
import butterknife.BindView
import butterknife.ButterKnife
import com.danilocianfrone.noty.Names
import com.danilocianfrone.noty.R
import com.danilocianfrone.noty.dagger.AppScope
import com.danilocianfrone.noty.doWithSharedPrefsEditor
import com.danilocianfrone.noty.models.Note
import com.danilocianfrone.noty.models.Priority
import io.realm.Realm
import javax.inject.Inject

/**
 * Initial activity, started when a new App instance is started.
 * Will show a splashscreen while the app is getting ready to work.
 *
 * On first boot, it will go straight to the TutorialActivity;
 * else, will go to NoteActivity.
 */
class MainActivity : BaseActivity() {

    @Inject @AppScope lateinit var prefs: SharedPreferences
    @Inject @AppScope lateinit var realm: Realm

    @BindView(R.id.adapter_note_view) lateinit var note: NoteView

    override fun onSetContentView() {
        setContentView(R.layout.activity_main)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // Inject object graph
        notyApplication.objectGraph.inject(this)
    }

    override fun onStart() {
        super.onStart()

        Log.i(TAG, note.toString())

        // Check if is first boot
        if (prefs.getBoolean(Names.FIRST_BOOT, true)) {
            Log.i(TAG, "Is first boot")
            // Write something to the database
            realm.executeTransaction {
                for (i in 0..50) {
                    val note = realm.createObject(Note::class.java)
                    note.priority = Priority.FromValue(i)
                    note.content = "Note $i generated as proof"
                }
            }
            // Write new Value
            doWithSharedPrefsEditor(prefs, { it.putBoolean(Names.FIRST_BOOT, false) })
            // Go to TutorialActivity
            startActivity(Intent(this, TutorialActivity::class.java))
        } else {
            Log.i(TAG, "Is not first boot")

            // Go to NoteActivity
            startActivity(Intent(this, NoteActivity::class.java))
        }
    }

    companion object {
        const val TAG = "MainActivity"
    }
}
