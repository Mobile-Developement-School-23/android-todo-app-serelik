package com.serelik.todoapp.di

import android.content.Context
import com.serelik.todoapp.authorizationFragment.AuthorizationFragment
import com.serelik.todoapp.data.network.NetworkModule
import com.serelik.todoapp.edit.TodoEditFragment
import com.serelik.todoapp.list.TodoListFragment
import dagger.BindsInstance
import dagger.Component
import dagger.Module
import javax.inject.Scope


@Scope
annotation class AppScope

@AppScope
@Component(modules = [NetworkModule::class, AppModule::class])
interface AppComponent {
    fun activityComponent(): ActivityComponent.Factory


    @Component.Factory
    interface Factory {

        fun create(@BindsInstance context: Context): AppComponent
    }
}

@Module(subcomponents = [ActivityComponent::class])
interface AppModule {

}