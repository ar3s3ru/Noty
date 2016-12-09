package com.danilocianfrone.noty

import android.content.SharedPreferences
import android.os.Bundle

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

/**
 * Utility builder for Bundle objects.
 *
 * @param bundle: Bundle from which apply building
 */
internal class BundleBuilder(private val bundle: Bundle) {
    fun putInt(key: String, value: Int): BundleBuilder {
        bundle.putInt(key, value)
        return this
    }

    fun build(): Bundle = bundle
}