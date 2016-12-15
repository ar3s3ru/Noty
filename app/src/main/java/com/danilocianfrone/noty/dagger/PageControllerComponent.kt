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
 * Scope annotation for [PageController] lifecycle.
 * Target all the objects annotated with this scope to the lifecycle of the [PageControllerComponent].
 */
@Scope annotation class PageControllerScope

/**
 * Module class for [PageController] Dagger component.
 * Provides all the dependencies needed within the PageController class.
 */
@Module class PageControllerModule(controller: PageController) {

    private val refController = WeakReference<PageController>(controller)

    /**
     * Provides the [Priority] value that identifies the [PageController] instance.
     *
     * @return Priority value from [PageController] bundle
     */
    @Provides @PageControllerScope fun providePriority() =
            // Should be always args != null
            // N.B. Could be that refController has an expired reference to PageController!
            Priority.FromValue(refController.get().args!!.getInt(Names.PRIORITY))

    /**
     * Provides the [NoteListAdapter] used with the [PageController] [android.support.v7.widget.RecyclerView].
     *
     * @param priority: [Priority] value that notes showed by the adapter must have
     * @return [NoteListAdapter] instance
     */
    @Provides @PageControllerScope fun provideNoteListAdapter(priority: Priority) =
            NoteListAdapter(refController.get(), priority)
}

/**
 * Dagger2 [PageController] Component.
 *
 * It's a SubComponent of [NoteControllerComponent] (from which is instanced), and its lifecycle
 * is determined by [PageControllerScope]
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
         * Uses the [PageControllerModule] passed as dependency satisfier.
         * @param module: [PageControllerModule}] instance
         */
        fun withModule(module: PageControllerModule): Builder

        /**
         * Builds the [PageControllerComponent], ready to be injected.
         * @return new [PageControllerComponent] instance
         */
        fun build(): PageControllerComponent
    }
}
