package com.danilocianfrone.noty.views

import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import android.util.Log

import io.realm.Realm
import javax.inject.Inject

import com.danilocianfrone.noty.singleton.Names
import com.danilocianfrone.noty.R
import com.danilocianfrone.noty.dagger.AppScope
import com.danilocianfrone.noty.doWithSharedPrefsEditor

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

    override fun onSetContentView() {
        setContentView(R.layout.activity_main)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // Inject object graph
        notyApplication.objectGraph.plus(this)
    }

    override fun onStart() {
        super.onStart()

        Log.i(TAG, "Starting MainActivity")
        when (prefs.getBoolean(Names.FIRST_BOOT, true)) {
            false -> startActivity(Intent(this, NoteActivity::class.java))  // Not first boot
            true  -> {                                                      // First boot
                doWithSharedPrefsEditor(prefs, { it.putBoolean(Names.FIRST_BOOT, false) })
                startActivity(Intent(this, TutorialActivity::class.java))   // Go to tutorial
            }
        }
    }

    companion object {
        const val TAG = "MainActivity"
    }
}
