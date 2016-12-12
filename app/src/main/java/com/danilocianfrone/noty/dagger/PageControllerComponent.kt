package com.danilocianfrone.noty.dagger

import com.danilocianfrone.noty.singleton.Names
import com.danilocianfrone.noty.models.Priority
import com.danilocianfrone.noty.views.controllers.PageController
import com.danilocianfrone.noty.views.recyclers.NoteListAdapter

import dagger.Module
import dagger.Provides
import dagger.Subcomponent
import java.lang.ref.WeakReference

import javax.inject.Scope

/**
 * Scope annotation for {@link com.danilocianfrone.noty.view.controllers.PageController} lifecycle.
 * Target all the objects annotated with this scope to the lifecycle of the PageController component.
 */
@Scope annotation class PageControllerScope

/**
 * Module class for {@link com.danilocianfrone.noty.view.controllers.PageController} Dagger component.
 * Provides all the dependencies needed within the PageController class.
 */
@Module class PageControllerModule(controller: PageController) {

    private val refController = WeakReference<PageController>(controller)

    /**
     * Provides the Priority value that identifies the
     * {@link com.danilocianfrone.noty.view.controllers.PageController} instance.
     *
     * @return Priority value from {@link com.danilocianfrone.noty.view.controllers.PageController} bundle
     */
    @Provides @PageControllerScope fun providePriority() =
            // Should be always args != null
            Priority.FromValue(refController.get().args!!.getInt(Names.PRIORITY))

    /**
     * Provides the {@link com.danilocianfrone.noty.view.recyclers.NoteListAdapter} used with the
     * {@link com.danilocianfrone.noty.view.controllers.PageController} RecyclerView.
     *
     * @param priority: {@link com.danilocianfrone.noty.models.Priority} value that notes showed
     *                  by the adapter must have
     *
     * @return {@link com.danilocianfrone.noty.view.recyclers.NoteListAdapter} instance
     */
    @Provides @PageControllerScope fun provideNoteListAdapter(priority: Priority) =
            NoteListAdapter(priority)
}

/**
 * Dagger2 PageController Component.
 *
 * It's a SubComponent of {@link NoteControllerComponent} (from which is instanced), and its lifecycle
 * is determined by {@link PageControllerScope}
 */
@PageControllerScope
@Subcomponent(modules = arrayOf(PageControllerModule::class))
interface PageControllerComponent {
    /**
     * Build the object graph of the controller and injects all the necessary dependencies
     * @param controller: {@link com.danilocianfrone.noty.view.controllers.PageController} to inject
     */
    fun inject(controller: PageController)

    /**
     * Builder for the {@link PageControllerComponent} using a {@link PageControllerModule}.
     */
    @Subcomponent.Builder
    interface Builder {
        /**
         * Uses the {@link PageControllerModule} passed as dependency satisfier.
         * @param module: {@link PageControllerModule} instance
         */
        fun withModule(module: PageControllerModule): Builder

        /**
         * Builds the {@link PageControllerComponent}, ready to be injected.
         * @return new {@link PageControllerComponent} instance
         */
        fun build(): PageControllerComponent
    }
}
