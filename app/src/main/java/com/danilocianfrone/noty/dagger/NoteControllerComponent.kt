package com.danilocianfrone.noty.dagger

import com.danilocianfrone.noty.views.controllers.NoteListController
import dagger.Subcomponent

@NoteControllerScope
@Subcomponent(modules = arrayOf(ListControllerModule::class))
interface NoteControllerComponent {
    fun inject(controller: NoteListController)

    fun plusPageController(): PageControllerComponent.Builder

    @Subcomponent.Builder
    interface Builder {
        fun withModule(module: ListControllerModule): Builder
        fun build(): NoteControllerComponent
    }
}