package com.serelik.todoapp.di

import androidx.lifecycle.ViewModelProvider
import androidx.work.Configuration
import com.serelik.todoapp.data.workers.SyncListTodoWorkerFactory
import dagger.Binds
import dagger.Module
import dagger.Provides
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

    @Subcomponent.Factory
    interface Factory {

        fun create(): ActivityComponent
    }

}

@Module(
    subcomponents = [TodoListFragmentComponent::class,
        TodoEditFragmentComponent::class,
        AuthorizationFragmentComponent::class]
)
interface ActivityModule {
    @Binds
    abstract fun viewModelFactory(factory: MultiViewModelFactory): ViewModelProvider.Factory


}