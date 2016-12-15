package com.danilocianfrone.noty.singleton

import com.bluelinelabs.conductor.Controller
import com.danilocianfrone.noty.views.controllers.*

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
     * Provides an instance of [FastCreationController]
     *
     * @return new [FastCreationController] instance
     */
    fun provideFastCreationController(): Controller = FastCreationController()

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
