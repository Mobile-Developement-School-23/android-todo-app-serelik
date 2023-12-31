package com.serelik.todoapp.di

import androidx.lifecycle.ViewModelProvider
import com.serelik.todoapp.ui.MainActivity
import dagger.Binds
import dagger.Module
import dagger.Subcomponent
import javax.inject.Scope

@Scope
annotation class ActivityScope

@Subcomponent(modules = [ActivityModule::class])
@ActivityScope
interface ActivityComponent {
    fun todoListFragmentComponent(): TodoListFragmentComponent.Factory
    fun todoEditFragmentComponent(): TodoEditFragmentComponent.Factory
    fun authorizationFragmentComponent(): AuthorizationFragmentComponent.Factory
    fun settingsFragmentComponent(): SettingsFragmentComponent.Factory

    fun inject(mainActivity: MainActivity)

    @Subcomponent.Factory
    interface Factory {

        fun create(): ActivityComponent
    }
}

@Module(
    subcomponents = [
        TodoListFragmentComponent::class,
        TodoEditFragmentComponent::class,
        AuthorizationFragmentComponent::class,
        SettingsFragmentComponent::class
    ]
)
interface ActivityModule {
    @Binds
    fun viewModelFactory(factory: MultiViewModelFactory): ViewModelProvider.Factory
}
