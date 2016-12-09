package com.danilocianfrone.noty

import android.content.SharedPreferences

/**
 * Create a new editor and uses it with the SharedPreferences passed as argument
 *
 * @param prefs: Shared preferences to edit
 * @param func: Action to execute with the preferences editor
 *
 * @return if the commit has been successful or not
 */
internal inline fun doWithSharedPrefsEditor(prefs: SharedPreferences,
                                            func: (SharedPreferences.Editor) -> Unit): Boolean {
    // Get new editor instance and use it
    val editor = prefs.edit(); func(editor); return editor.commit()
}
