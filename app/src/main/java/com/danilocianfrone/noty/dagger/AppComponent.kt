package com.danilocianfrone.noty.dagger

import android.content.Context
import android.content.SharedPreferences
import android.preference.PreferenceManager
import com.danilocianfrone.noty.Names
import com.danilocianfrone.noty.Noty
import com.danilocianfrone.noty.models.RealmModule
import com.danilocianfrone.noty.presenters.NotePresenter
import com.danilocianfrone.noty.views.MainActivity
import com.danilocianfrone.noty.views.controllers.NoteCreationController

import dagger.Component
import dagger.Module
import dagger.Provides

import io.realm.Realm
import io.realm.RealmConfiguration

import javax.inject.Named
import javax.inject.Scope

/**
 * Scope annotation for {@link com.danilocianfrone.noty.Noty} lifecycle, which is the whole
 * Application lifecycle, indeed.
 */
@Scope annotation class AppScope

/**
 * Module class for the {@link com.danilocianfrone.noty.Noty} Dagger component.
 * Used to provide dependencies within the whole application.
 */
@Module(subcomponents = arrayOf(NoteActivityComponent::class))
class AppModule(private val applicationContext: Context) {
    /**
     * Provide the application {@link android.content.Context} to the other subcomponents
     * @return {@link com.danilocianfrone.noty.Noty} {@link android.content.Context} instance
     */
    @Provides @AppScope fun provideAppContext(): Context = applicationContext

    /**
     * Provide the application-level {@link android.content.SharedPreferences} instance.
     * @return Application {@link android.content.SharedPreferences} instance
     */
    @Provides @AppScope fun provideAppPrefs(): SharedPreferences =
            PreferenceManager.getDefaultSharedPreferences(applicationContext)

    /**
     *
     */
    @Provides @AppScope fun provideRealmInstance(): Realm {
        // Setup Realm
        Realm.init(applicationContext)  // Uses the applicationContext
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
     *
     */
    @Provides @AppScope fun provideNotePresenter(realm: Realm): NotePresenter =
            NotePresenter(realm)
}

/**
 *
 */
@Module class PreferencesModule() {
    @Provides @Named(Names.FIRST_BOOT) fun provideFirstBootValue(prefs: SharedPreferences) =
            prefs.getBoolean(Names.FIRST_BOOT, true)
}

/**
 *
 */
@AppScope
@Component(modules = arrayOf(AppModule::class, PreferencesModule::class))
interface AppComponent {
    fun inject(application: Noty)
    fun inject(activity: MainActivity)
    fun inject(controller: NoteCreationController)

    fun plusNoteActivity(): NoteActivityComponent.Builder
}
