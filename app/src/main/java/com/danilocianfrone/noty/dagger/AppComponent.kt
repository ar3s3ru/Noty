package com.danilocianfrone.noty.dagger

import dagger.Component

import com.danilocianfrone.noty.Noty
import com.danilocianfrone.noty.views.MainActivity
import com.danilocianfrone.noty.views.controllers.PageController

@AppScope
@Component(modules = arrayOf(AppModule::class, PreferencesModule::class))
interface AppComponent {
    fun inject(application: Noty)
    fun inject(activity: MainActivity)
    fun inject(controller: PageController)

    fun plusNoteActivity(): NoteActivityComponent.Builder
}
