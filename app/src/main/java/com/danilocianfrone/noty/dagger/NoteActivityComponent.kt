package com.danilocianfrone.noty.dagger

import com.danilocianfrone.noty.views.NoteActivity
import dagger.Subcomponent

@ActivityScope
@Subcomponent(modules = arrayOf(NoteActivityModule::class))
interface NoteActivityComponent {
    fun inject(activity: NoteActivity)

    fun plusNoteController(): NoteControllerComponent.Builder

    @Subcomponent.Builder
    interface Builder {
        fun withModule(module: NoteActivityModule): Builder
        fun build(): NoteActivityComponent
    }
}
