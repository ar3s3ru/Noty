package com.danilocianfrone.noty.dagger

import dagger.Subcomponent

import com.danilocianfrone.noty.views.controllers.PageController

@Subcomponent(modules = arrayOf(PageControllerModule::class))
interface PageControllerComponent {
    fun inject(controller: PageController)

    @Subcomponent
    interface Builder {
        fun withModule(module: PageControllerModule): Builder
        fun build(): PageControllerComponent
    }
}
