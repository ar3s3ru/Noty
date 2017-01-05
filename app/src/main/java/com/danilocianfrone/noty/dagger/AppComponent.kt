package com.danilocianfrone.noty.dagger

import android.app.Application
import android.content.Context
import android.content.SharedPreferences
import android.preference.PreferenceManager
import com.danilocianfrone.noty.Noty
import com.danilocianfrone.noty.models.RealmModule
import com.danilocianfrone.noty.presenters.NotePresenter
import com.danilocianfrone.noty.singleton.Names
import com.danilocianfrone.noty.singleton.PreferenceGetter
import com.danilocianfrone.noty.views.MainActivity
import com.danilocianfrone.noty.views.NoteActivity
import com.danilocianfrone.noty.views.controllers.NoteCreationController
import com.danilocianfrone.noty.views.controllers.NoteListController
import com.squareup.leakcanary.LeakCanary
import com.squareup.leakcanary.RefWatcher
import dagger.Component
import dagger.Module
import dagger.Provides
import io.realm.Realm
import io.realm.RealmConfiguration
import javax.inject.Named
import javax.inject.Scope

/**
 * Scope annotation for [Noty] lifecycle, which is the whole [Application] lifecycle.
 */
@Scope annotation class AppScope

/**
 * Module class for the [Noty] Dagger component.
 * Used to provide dependencies within the whole application.
 */
@Module(subcomponents = arrayOf(PageControllerComponent::class, FastControllerComponent::class))
class AppModule(private val application: Application) {
    /**
     * Provides the application [android.content.Context] to the other subcomponents
     *
     * @return [Noty] [android.content.Context] instance
     */
    @Provides @AppScope fun provideAppContext(): Context = application

    /**
     * Provides the application-level [android.content.SharedPreferences] instance.
     *
     * @return Application [android.content.SharedPreferences] instance
     */
    @Provides @AppScope fun provideAppPrefs(): SharedPreferences =
            PreferenceManager.getDefaultSharedPreferences(application)

    /**
     * Provides a global [Realm] instance throughout the whole [Noty] application.
     *
     * @return [Noty] application [Realm] instance
     */
    @Provides @AppScope fun provideRealmInstance(): Realm {
        // Setup Realm
        Realm.init(application)  // Uses the application
        Realm.setDefaultConfiguration(
                RealmConfiguration.Builder()
                        .name(Names.REALM_NAME)
                        .modules(Realm.getDefaultModule(), RealmModule())
                        .build()
        )

        // Return the Realm default instance, just configurated
        return Realm.getDefaultInstance()
    }

    /**
     * Provides a [NotePresenter] application instance.
     *
     * @return [NotePresenter] application instance
     */
    @Provides @AppScope fun provideNotePresenter(realm: Realm): NotePresenter =
            NotePresenter(realm)

    /**
     * Provides a [RefWatcher] application instance, used for memory leaks debugging
     *
     * @return [RefWatcher] instance
     */
    @Provides @AppScope fun provideRefWatcher(): RefWatcher =
            LeakCanary.install(application)
}

/**
 * Module for [AppComponent] that provides dependency regarding [SharedPreferences] application
 * instance.
 */
@Module class PreferencesModule() {

    /**
     * Provides the value of first boot flag
     *
     * @return true if the [Noty] application is booted for the first time
     */
    @Provides @Named(Names.FIRST_BOOT) fun provideFirstBootValue(prefs: SharedPreferences) =
            PreferenceGetter.firstBoot(prefs)
}

/**
 * Dagger [Noty] application component, used for application-level dependency injection and
 * subcomponents' object graph building.
 */
@AppScope
@Component(modules = arrayOf(AppModule::class, PreferencesModule::class))
interface AppComponent {
    /**
     *
     */
    fun inject(application: Noty)

    /**
     *
     */
    fun inject(activity: MainActivity)

    /**
     *
     */
    fun inject(activity: NoteActivity)

    /**
     *
     */
    fun inject(controller: NoteListController)

    /**
     *
     */
    fun inject(controller: NoteCreationController)

    // SUBCOMPONENTS BUILDING /////////////////////////////////////////////////

    /**
     *
     */
    fun plusPageControllerComponent(): PageControllerComponent.Builder

    /**
     *
     */
    fun plusSearchControllerComponent(): SearchControllerComponent.Builder

    /**
     *
     */
    fun plusFastControllerComponent(): FastControllerComponent.Builder
}
