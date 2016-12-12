package com.danilocianfrone.noty.dagger

import com.danilocianfrone.noty.views.controllers.NoteListController
import com.danilocianfrone.noty.views.controllers.PageController
import com.danilocianfrone.noty.views.controllers.PagerAdapter
import dagger.Subcomponent
import dagger.Module
import dagger.Provides
import java.lang.ref.WeakReference
import javax.inject.Scope

/**
 *
 */
@Scope annotation class NoteControllerScope

/**
 *
 */
@Module(subcomponents = arrayOf(PageControllerComponent::class))
class ListControllerModule(controller: NoteListController) {
    private val refController =  WeakReference<NoteListController>(controller)

    @Provides @NoteControllerScope fun providePageControllers(): Array<PageController> =
            Array(5, ::PageController)
    @Provides @NoteControllerScope fun providePageAdapter(controllers: Array<PageController>) =
            PagerAdapter(refController.get(), false, controllers)
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