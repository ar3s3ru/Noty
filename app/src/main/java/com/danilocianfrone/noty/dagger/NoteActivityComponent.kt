package com.danilocianfrone.noty.dagger

import com.bluelinelabs.conductor.RouterTransaction
import com.bluelinelabs.conductor.changehandler.FadeChangeHandler
import com.danilocianfrone.noty.Names
import com.danilocianfrone.noty.views.NoteActivity
import com.danilocianfrone.noty.views.controllers.DocumentController
import com.danilocianfrone.noty.views.controllers.FastCreationController
import com.danilocianfrone.noty.views.controllers.NoteCreationController
import com.danilocianfrone.noty.views.controllers.NoteListController
import dagger.Module
import dagger.Provides
import dagger.Subcomponent
import java.lang.ref.WeakReference
import javax.inject.Named
import javax.inject.Scope

/**
 *
 */
@Scope annotation class ActivityScope

/**
 *
 */
@Module(subcomponents = arrayOf(NoteControllerComponent::class))
class NoteActivityModule(activity: NoteActivity) {
    private val refActivity = WeakReference<NoteActivity>(activity)

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
