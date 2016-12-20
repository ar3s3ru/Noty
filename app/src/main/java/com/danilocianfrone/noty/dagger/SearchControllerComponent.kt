package com.danilocianfrone.noty.dagger

import com.danilocianfrone.noty.views.controllers.paged.NoteSearchController
import com.danilocianfrone.noty.views.recyclers.NoteSearchAdapter
import dagger.Module
import dagger.Provides
import dagger.Subcomponent
import java.lang.ref.WeakReference
import javax.inject.Scope


@Scope annotation class SearchControllerScope

@Module class SearchControllerModule(controller: NoteSearchController) {
    private val refController = WeakReference<NoteSearchController>(controller)

    @Provides fun provideSearchAdapter() =
            NoteSearchAdapter()
}

@Subcomponent(modules = arrayOf(SearchControllerModule::class))
interface SearchControllerComponent {

    fun inject(controller: NoteSearchController)

    @Subcomponent.Builder
    interface Builder {
        fun withModule(module: SearchControllerModule): Builder
        fun build(): SearchControllerComponent
    }
}

