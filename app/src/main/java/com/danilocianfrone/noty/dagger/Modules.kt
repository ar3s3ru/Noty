package com.danilocianfrone.noty.dagger

import android.content.Context
import android.content.SharedPreferences
import android.os.Bundle
import android.preference.PreferenceManager

import dagger.Module
import dagger.Provides
import javax.inject.Named

import io.realm.Realm

import com.danilocianfrone.noty.Names
import com.danilocianfrone.noty.presenters.NotePresenter
import com.danilocianfrone.noty.views.NoteActivity
import com.danilocianfrone.noty.views.controllers.*


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

@Module class NoteActivityModule(private val activity: NoteActivity) {
    @Provides @ActivityScope fun provideDocumentController()     = DocumentController()
    @Provides @ActivityScope fun provideFastCreationController() = FastCreationController()
    @Provides @ActivityScope fun provideNoteCreationController() = NoteCreationController()
    @Provides @ActivityScope fun provideNoteListController()     = NoteListController()
}

@Module class ListControllerModule() {
    @Provides fun provideBundles() = arrayOf(Bundle(), Bundle(), Bundle(), Bundle(), Bundle())
    @Provides fun providePageControllers(bundles: Array<Bundle>) =
            Array(bundles.size, {
                val bundle = bundles[it]            // Store actual bundle in value
                bundle.putInt(Names.PRIORITY, it)   // Put the priority key into the bundle
                PageController(bundle)              // Create and put new object
            })
}

@Module class PageControllerModule() {

}
