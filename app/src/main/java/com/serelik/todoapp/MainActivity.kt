package com.serelik.todoapp

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.serelik.todoapp.list.TodoListFragment

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        if (savedInstanceState == null) {
            supportFragmentManager
                .beginTransaction()
                .add(android.R.id.content, TodoListFragment())
                .commit()
        }
    }
}