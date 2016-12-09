package com.danilocianfrone.noty.views

import android.content.pm.ActivityInfo
import android.os.Bundle
import android.os.PersistableBundle
import android.support.v7.app.AppCompatActivity
import android.util.Log
import butterknife.ButterKnife

import butterknife.Unbinder

import com.danilocianfrone.noty.Noty

/**
 * Activity Base class that has a reference to the application as Noty instance.
 */
abstract class BaseActivity : AppCompatActivity() {
    // Application reference
    protected val notyApplication: Noty by lazy { application as Noty }
    // ButterKnife unbinder
    private lateinit var unbinder: Unbinder

    /**
     *
     */
    protected abstract fun onSetContentView()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        // We want the activities to be only in portrait mode
        requestedOrientation = ActivityInfo.SCREEN_ORIENTATION_PORTRAIT
        // Now, set the content view
        onSetContentView()
        // Then, bind the views with ButterKnife
        unbinder = ButterKnife.bind(this)
    }

    override fun onDestroy() {
        super.onDestroy()
        // Unbind da shit
        unbinder.unbind()
    }
}