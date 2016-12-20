package com.danilocianfrone.noty.dagger

import com.danilocianfrone.noty.models.Priority
import com.danilocianfrone.noty.singleton.Names
import com.danilocianfrone.noty.views.controllers.FastCreationController
import dagger.Module
import dagger.Provides
import dagger.Subcomponent
import java.lang.ref.WeakReference
import javax.inject.Scope

@Scope annotation class FastControllerScope

@Module
class FastControllerModule(controller: FastCreationController) {
    private val refController = WeakReference(controller)

    @Provides @FastControllerScope fun providePriority() =
            Priority.FromValue(refController.get().args!!.getInt(Names.PRIORITY))
}

@FastControllerScope
@Subcomponent(modules = arrayOf(FastControllerModule::class))
interface FastControllerComponent {

    fun inject(controller: FastCreationController)

    @Subcomponent.Builder
    interface Builder {
        fun withModule(module: FastControllerModule): Builder
        fun build(): FastControllerComponent
    }
}
