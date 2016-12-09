package com.danilocianfrone.noty.dagger

import dagger.Subcomponent

import com.danilocianfrone.noty.views.NoteActivity

@Subcomponent(modules = arrayOf(NoteActivityModule::class))
interface NoteActivityComponent {
    fun inject(activity: NoteActivity)

    @Subcomponent.Builder
    interface Builder {
        fun withModule(module: NoteActivityModule): Builder
        fun build(): NoteActivityComponent
    }
}
