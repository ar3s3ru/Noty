package com.danilocianfrone.noty.singleton

import com.bluelinelabs.conductor.Controller
import com.danilocianfrone.noty.views.controllers.*

object ControllerFactory {
    fun provideDocumentController():     Controller = DocumentController()
    fun provideFastCreationController(): Controller = FastCreationController()
    fun provideNoteCreationController(): Controller = NoteCreationController()
    fun provideNoteListController():     Controller = NoteListController()
}
