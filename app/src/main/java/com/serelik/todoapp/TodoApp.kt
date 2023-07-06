package com.serelik.todoapp

import android.app.Application
import android.content.Context
import com.serelik.todoapp.di.AppComponent
import com.serelik.todoapp.di.DaggerAppComponent

class TodoApp : Application() {

    lateinit var compoment: AppComponent
        private set

    override fun onCreate() {
        super.onCreate()
        compoment = DaggerAppComponent.factory().create(applicationContext)
    }

}

val Context.compoment: AppComponent
    get() = when (this) {
        is TodoApp -> compoment
        else -> this.applicationContext.compoment
    }