package com.danilocianfrone.noty.singleton

import com.bluelinelabs.conductor.Controller
import com.danilocianfrone.noty.models.Priority
import com.danilocianfrone.noty.views.controllers.DocumentController
import com.danilocianfrone.noty.views.controllers.FastCreationController
import com.danilocianfrone.noty.views.controllers.NoteCreationController
import com.danilocianfrone.noty.views.controllers.NoteListController

/**
 * Factory object for [Controller] used throughout the application
 */
internal object ControllerFactory {

    /**
     * Provides an instance of [DocumentController]
     *
     * @return new [DocumentController] instance
     */
    fun provideDocumentController(): Controller = DocumentController()

    /**
     * Provides an instance of [NoteCreationController]
     *
     * @return new [NoteCreationController] instance
     */
    fun provideNoteCreationController(): Controller = NoteCreationController()

    /**
     * Provides an instance of [NoteListController]
     *
     * @return new [NoteListController] instance
     */
    fun provideNoteListController(): Controller = NoteListController()
}
