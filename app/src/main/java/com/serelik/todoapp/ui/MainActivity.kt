package com.serelik.todoapp.ui

import android.Manifest
import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.pm.PackageManager
import android.os.Build
import android.os.Bundle
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.app.AppCompatDelegate
import androidx.core.content.ContextCompat
import com.serelik.todoapp.component
import com.serelik.todoapp.data.local.ThemeStorage
import com.serelik.todoapp.data.local.TokenStorage
import com.serelik.todoapp.di.ActivityComponent
import com.serelik.todoapp.model.ThemeType
import com.serelik.todoapp.notification.AlarmReceiver
import com.serelik.todoapp.notification.ReminderManager
import com.serelik.todoapp.ui.authorization.AuthorizationFragment
import com.serelik.todoapp.ui.list.TodoListFragment

class MainActivity : AppCompatActivity() {
    private val tokenStorage by lazy { TokenStorage(this) }
    private val themeStorage by lazy { ThemeStorage(this) }

    val requestPermissionLauncher =
        registerForActivityResult(ActivityResultContracts.RequestPermission(), {
        })


    lateinit var activityComponent: ActivityComponent
        private set

    override fun onCreate(savedInstanceState: Bundle?) {
        activityComponent = this.component.activityComponent().create()
        changeTheme(themeStorage.getTheme())

        super.onCreate(savedInstanceState)

        checkNotificationPermission()

        createNotificationsChannels()
        ReminderManager.startReminder(this)

        if (savedInstanceState == null) {
            navigate()
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

    private fun createNotificationsChannels() {
        val channel = NotificationChannel(
            AlarmReceiver.REMINDERS_NOTIFICATION_CHANNEL_ID,
            "checky check",
            NotificationManager.IMPORTANCE_HIGH
        )
        ContextCompat.getSystemService(this, NotificationManager::class.java)
            ?.createNotificationChannel(channel)
    }

    fun navigate() {
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

    private fun checkNotificationPermission() {
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.POST_NOTIFICATIONS)
            != PackageManager.PERMISSION_GRANTED
            && Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU
        ) {
            requestPermissionLauncher.launch(Manifest.permission.POST_NOTIFICATIONS)
        }
    }

}