package com.serelik.todoapp

import android.app.Application
import android.content.Context
import androidx.work.Configuration
import androidx.work.WorkManager
import com.serelik.todoapp.di.AppComponent
import com.serelik.todoapp.di.DaggerAppComponent
import javax.inject.Inject

class TodoApp : Application(), Configuration.Provider {

    lateinit var compoment: AppComponent
        private set

    @Inject
    lateinit var workerConfiguration: Configuration


    override fun onCreate() {
        super.onCreate()
        compoment = DaggerAppComponent.factory().create(applicationContext)
        compoment.inject(this)
        WorkManager.initialize(this, workerConfiguration)
    }

    override fun getWorkManagerConfiguration(): Configuration {
        return workerConfiguration
    }

}

val Context.component: AppComponent
    get() = when (this) {
        is TodoApp -> compoment
        else -> this.applicationContext.component
    }
