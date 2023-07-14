package com.serelik.todoapp.ui

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.app.AppCompatDelegate
import com.serelik.todoapp.component
import com.serelik.todoapp.data.local.ThemeStorage
import com.serelik.todoapp.data.local.TokenStorage
import com.serelik.todoapp.di.ActivityComponent
import com.serelik.todoapp.model.ThemeType
import com.serelik.todoapp.ui.authorization.AuthorizationFragment
import com.serelik.todoapp.ui.list.TodoListFragment

class MainActivity : AppCompatActivity() {
    private val tokenStorage by lazy { TokenStorage(this) }
    private val themeStorage by lazy { ThemeStorage(this) }

    lateinit var activityComponent: ActivityComponent
        private set

    override fun onCreate(savedInstanceState: Bundle?) {
        activityComponent = this.component.activityComponent().create()
        changeTheme(themeStorage.getTheme())

        super.onCreate(savedInstanceState)

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

    fun changeTheme(themeType: ThemeType) {
        val mode = when (themeType) {
            ThemeType.SYSTEM -> AppCompatDelegate.MODE_NIGHT_FOLLOW_SYSTEM
            ThemeType.DARK -> AppCompatDelegate.MODE_NIGHT_YES
            ThemeType.LIGHT -> AppCompatDelegate.MODE_NIGHT_NO
        }
        AppCompatDelegate.setDefaultNightMode(mode)
        delegate.applyDayNight()
    }
}
