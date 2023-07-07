package com.serelik.todoapp.ui

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.serelik.todoapp.component
import com.serelik.todoapp.data.local.TokenStorage
import com.serelik.todoapp.di.ActivityComponent
import com.serelik.todoapp.ui.authorization.AuthorizationFragment
import com.serelik.todoapp.ui.list.TodoListFragment

class MainActivity : AppCompatActivity() {
    private val tokenStorage by lazy { TokenStorage(this) }

    lateinit var activityComponent: ActivityComponent
        private set

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        activityComponent = this.component.activityComponent().create()

        if (savedInstanceState == null) {
            val fragment = if (tokenStorage.hasToken()) {
                TodoListFragment()
            } else {
                AuthorizationFragment()
            }

            supportFragmentManager
                .beginTransaction()
                .add(android.R.id.content, fragment)
                .commit()
        }
    }
}
