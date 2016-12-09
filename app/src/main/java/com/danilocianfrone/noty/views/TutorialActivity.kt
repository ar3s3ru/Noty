package com.danilocianfrone.noty.views

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.Button
import butterknife.BindView
import com.danilocianfrone.noty.R

/**
 * This activity will guide the 'new' user to the app functionalities.
 * It'll basically require a way to exit the tutorial and go straight to the NoteActivity.
 */
class TutorialActivity : BaseActivity(), View.OnClickListener {

    @BindView(R.id.button) lateinit var goToButton: Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        goToButton.setOnClickListener(this)
    }

    override fun onSetContentView() {
        setContentView(R.layout.activity_tutorial)
    }

    override fun onClick(view: View) {
        startActivity(Intent(this, NoteActivity::class.java))
    }

    companion object {
        const val TAG = "TutorialActivity"
    }
}
