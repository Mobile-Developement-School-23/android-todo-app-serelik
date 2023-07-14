package com.serelik.todoapp.di

import android.content.Context
import androidx.work.Configuration
import com.serelik.todoapp.TodoApp
import com.serelik.todoapp.data.workers.SyncListTodoWorkerFactory
import dagger.BindsInstance
import dagger.Component
import dagger.Module
import dagger.Provides
import javax.inject.Scope

@Scope
annotation class AppScope

@AppScope
@Component(modules = [NetworkModule::class, AppModule::class])
interface AppComponent {
    fun activityComponent(): ActivityComponent.Factory

    fun inject(app: TodoApp)

    @Component.Factory
    interface Factory {

        fun create(@BindsInstance context: Context): AppComponent
    }
}

@Module(subcomponents = [ActivityComponent::class])
interface AppModule {
    companion object {
        @Provides
        fun provideWorkManagerConfiguration(workerFactory: SyncListTodoWorkerFactory): Configuration {
            return Configuration.Builder()
                .setMinimumLoggingLevel(android.util.Log.DEBUG)
                .setWorkerFactory(workerFactory)
                .build()
        }
    }
}
