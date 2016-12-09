package com.danilocianfrone.noty.dagger

import android.content.Context
import android.content.SharedPreferences
import android.preference.PreferenceManager
import com.danilocianfrone.noty.Names
import com.danilocianfrone.noty.models.Priority
import com.danilocianfrone.noty.presenters.NotePresenter
import com.danilocianfrone.noty.views.NoteActivity
import com.danilocianfrone.noty.views.controllers.*
import com.danilocianfrone.noty.views.recyclers.NoteListAdapter
import dagger.Module
import dagger.Provides
import io.realm.Realm
import javax.inject.Named


@Module(subcomponents = arrayOf(NoteActivityComponent::class))
class AppModule(private val applicationContext: Context) {
    @Provides @AppScope fun provideAppContext(): Context = applicationContext
    @Provides @AppScope fun provideAppPrefs(): SharedPreferences =
            PreferenceManager.getDefaultSharedPreferences(applicationContext)
    @Provides @AppScope fun provideRealmInstance(): Realm =
            Realm.getDefaultInstance()
    @Provides @AppScope fun provideNotePresenter(realm: Realm): NotePresenter =
            NotePresenter(realm)
}

@Module class PreferencesModule() {
    @Provides @Named(Names.FIRST_BOOT) fun provideFirstBootValue(prefs: SharedPreferences) =
            prefs.getBoolean(Names.FIRST_BOOT, true)
}

@Module(subcomponents = arrayOf(NoteControllerComponent::class))
class NoteActivityModule(private val activity: NoteActivity) {
    @Provides @ActivityScope fun provideDocumentController()     = DocumentController()
    @Provides @ActivityScope fun provideFastCreationController() = FastCreationController()
    @Provides @ActivityScope fun provideNoteCreationController() = NoteCreationController()
    @Provides @ActivityScope fun provideNoteListController()     = NoteListController()
}

@Module(subcomponents = arrayOf(PageControllerComponent::class))
class ListControllerModule(private val controller: NoteListController) {
    @Provides @NoteControllerScope fun providePageControllers(): Array<PageController> =
            Array(5, ::PageController)
    @Provides @NoteControllerScope fun providePageAdapter(controllers: Array<PageController>) =
            // TODO: implement save controller state
            PagerAdapter(controller, false, controllers)
}

@Module class PageControllerModule(private val controller: PageController) {
    @Provides @PageControllerScope fun providePriority() =
            // Should be always args != null
            Priority.FromValue(controller.args!!.getInt(Names.PRIORITY))
    @Provides @PageControllerScope fun provideNoteListAdapter(priority: Priority) =
            NoteListAdapter(priority)
}
