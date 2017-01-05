package com.danilocianfrone.noty.singleton

import android.content.SharedPreferences

internal object PreferenceGetter {
    fun firstBoot(prefs: SharedPreferences): Boolean =
            prefs.getBoolean(Names.FIRST_BOOT, true)

    fun columnNumber(prefs: SharedPreferences, min: Int): Int =
            prefs.getInt(Names.COL_NUMBER, min)
}