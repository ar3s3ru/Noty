package com.danilocianfrone.noty.views

import com.danilocianfrone.noty.R

/**
 * Will show exported and user-selectable preferences.
 * TODO: maybe use PreferenceActivity() as super class?
 */
class PrefsActivity : BaseActivity() {

    override fun onSetContentView() {
        setContentView(R.layout.activity_prefs)
    }

    companion object {
        const val TAG = "PrefsActivity"
    }
}
