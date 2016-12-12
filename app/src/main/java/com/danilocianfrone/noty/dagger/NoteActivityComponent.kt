package com.danilocianfrone.noty.dagger

import com.danilocianfrone.noty.views.NoteActivity
import com.danilocianfrone.noty.views.controllers.DocumentController
import com.danilocianfrone.noty.views.controllers.FastCreationController
import com.danilocianfrone.noty.views.controllers.NoteCreationController
import com.danilocianfrone.noty.views.controllers.NoteListController
import dagger.Module
import dagger.Provides
import dagger.Subcomponent
import javax.inject.Scope

/**
 *
 */
@Scope annotation class ActivityScope

/**
 *
 */
@Module(subcomponents = arrayOf(NoteControllerComponent::class))
class NoteActivityModule(private val activity: NoteActivity) {
    @Provides @ActivityScope fun provideDocumentController()     = DocumentController()
    @Provides @ActivityScope fun provideFastCreationController() = FastCreationController()
    @Provides @ActivityScope fun provideNoteCreationController() = NoteCreationController()
    @Provides @ActivityScope fun provideNoteListController()     = NoteListController()
}

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
