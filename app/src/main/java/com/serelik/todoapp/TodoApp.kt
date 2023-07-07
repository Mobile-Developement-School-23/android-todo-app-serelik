package com.serelik.todoapp

import android.annotation.SuppressLint
import android.app.Application
import android.content.Context

class TodoApp : Application() {

    override fun onCreate() {
        super.onCreate()
        _context = applicationContext
    }

    companion object {
        @SuppressLint("StaticFieldLeak")
        private var _context: Context? = null
        val context: Context get() = _context!!

    }
}