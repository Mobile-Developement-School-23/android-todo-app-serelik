package com.serelik.todoapp.data.local

import android.content.Context
import com.serelik.todoapp.TodoApp
import javax.inject.Inject

class RevisionStorage @Inject constructor(context: Context) {
    private val sharedPref = context.getSharedPreferences(
        SHARED_PREF_KEY, Context.MODE_PRIVATE
    )

    fun saveRevision(revision: Long) {
        with(sharedPref.edit()) {
            putLong(REVISION_KEY, revision)
            apply()
        }
    }

    fun getRevision(): Long {
        return sharedPref.getLong(REVISION_KEY, 0)
    }

    companion object {
        private const val REVISION_KEY = "REVISION_KEY"
        private const val SHARED_PREF_KEY = "SHARED_PREF_KEY_REVISION"
    }

}