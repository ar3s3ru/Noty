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
internal fun Bundle.withInt(key: String, value: Int): Bundle {
    putInt(key, value)
    return this
}

internal inline fun <T> doOnNullable(data: T?, onNotNull: (T) -> Unit, onNull: () -> Unit) =
        if (data != null) onNotNull(data) else onNull()

internal inline fun onNull(data: Any?, onNull: () -> Unit) {
    if (data == null) { onNull() }
}