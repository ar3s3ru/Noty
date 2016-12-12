package com.danilocianfrone.noty.dagger

import com.danilocianfrone.noty.views.controllers.NoteListController
import com.danilocianfrone.noty.views.controllers.PageController
import com.danilocianfrone.noty.views.controllers.PagerAdapter
import dagger.Subcomponent
import dagger.Module
import dagger.Provides
import javax.inject.Scope

/**
 *
 */
@Scope annotation class NoteControllerScope

/**
 *
 */
@Module(subcomponents = arrayOf(PageControllerComponent::class))
class ListControllerModule(private val controller: NoteListController) {
    @Provides @NoteControllerScope fun providePageControllers(): Array<PageController> =
            Array(5, ::PageController)
    @Provides @NoteControllerScope fun providePageAdapter(controllers: Array<PageController>) =
            PagerAdapter(controller, true, controllers)
}

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