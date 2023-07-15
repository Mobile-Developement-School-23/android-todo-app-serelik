package com.serelik.todoapp.di

import androidx.lifecycle.ViewModel
import com.serelik.todoapp.ui.settings.SettingsFragment
import com.serelik.todoapp.ui.settings.SettingsViewModel
import dagger.Binds
import dagger.Module
import dagger.Subcomponent
import dagger.multibindings.IntoMap
import javax.inject.Scope

@Scope
annotation class SettingsFragmentComponentScope

@Subcomponent(modules = [SettingsViewModelModule::class])
@SettingsFragmentComponentScope
interface SettingsFragmentComponent {
    fun inject(fragment: SettingsFragment)

    @Subcomponent.Factory
    interface Factory {

        fun create(): SettingsFragmentComponent
    }
}

@Module
interface SettingsViewModelModule {
    @Binds
    @IntoMap
    @ViewModelKey(SettingsViewModel::class)
    fun provideSettingsViewModel(settingsViewModel: SettingsViewModel): ViewModel
}
