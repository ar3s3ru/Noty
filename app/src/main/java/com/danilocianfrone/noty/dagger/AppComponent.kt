package com.danilocianfrone.noty.dagger

import com.danilocianfrone.noty.Noty
import com.danilocianfrone.noty.views.MainActivity
import dagger.Component

@AppScope
@Component(modules = arrayOf(AppModule::class, PreferencesModule::class))
interface AppComponent {
    fun inject(application: Noty)
    fun inject(activity: MainActivity)

    fun plusNoteActivity(): NoteActivityComponent.Builder
}
