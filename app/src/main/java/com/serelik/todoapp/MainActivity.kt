package com.serelik.todoapp

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.serelik.todoapp.authorizationFragment.AuthorizationFragment
import com.serelik.todoapp.data.local.TokenStorage
import com.serelik.todoapp.list.TodoListFragment

class MainActivity : AppCompatActivity() {
    private val tokenStorage by lazy { TokenStorage() }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        if (savedInstanceState == null) {

            val fragment = if (tokenStorage.hasToken())
                TodoListFragment()
            else
                AuthorizationFragment()

            supportFragmentManager
                .beginTransaction()
                .add(android.R.id.content, fragment)
                .commit()
        }
    }
}