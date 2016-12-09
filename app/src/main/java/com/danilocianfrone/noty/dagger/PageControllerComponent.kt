package com.danilocianfrone.noty.dagger

import com.danilocianfrone.noty.views.controllers.PageController
import dagger.Subcomponent

@PageControllerScope
@Subcomponent(modules = arrayOf(PageControllerModule::class))
interface PageControllerComponent {
    fun inject(controller: PageController)

    @Subcomponent.Builder
    interface Builder {
        fun withModule(module: PageControllerModule): Builder
        fun build(): PageControllerComponent
    }
}
