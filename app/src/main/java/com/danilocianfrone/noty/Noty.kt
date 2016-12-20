package com.danilocianfrone.noty

import android.app.Application
import com.danilocianfrone.noty.dagger.AppModule
import com.danilocianfrone.noty.dagger.DaggerAppComponent
import com.danilocianfrone.noty.dagger.PreferencesModule

/**
 * Noty Application class.
 * Used for object graph building and access.
 */
class Noty : Application() {

    // We want the object class to be accessible inside the application module
    internal val objectGraph by lazy {
        DaggerAppComponent.builder()
            .appModule(AppModule(this))
            .preferencesModule(PreferencesModule())
            .build()
    }

    override fun onCreate() {
        super.onCreate()

        // Inject object graph
        objectGraph.inject(this)
    }
}