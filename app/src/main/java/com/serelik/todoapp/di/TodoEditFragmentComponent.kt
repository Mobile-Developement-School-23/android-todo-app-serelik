package com.serelik.todoapp.di

import androidx.lifecycle.ViewModel
import com.serelik.todoapp.edit.TodoEditFragment
import com.serelik.todoapp.edit.TodoEditViewModel
import dagger.Binds
import dagger.Module
import dagger.Subcomponent
import dagger.multibindings.IntoMap
import javax.inject.Scope

@Scope
annotation class TodoEditFragmentComponentScope

@Subcomponent(modules = [TodoEditViewModelModule::class])
@TodoEditFragmentComponentScope
interface TodoEditFragmentComponent {
    fun inject(fragment: TodoEditFragment)

    @Subcomponent.Factory
    interface Factory {

        fun create(): TodoEditFragmentComponent
    }
}

@Module
interface TodoEditViewModelModule {
    @Binds
    @IntoMap
    @ViewModelKey(TodoEditViewModel::class)
    fun provideTodoListViewModel(todoEditViewModel: TodoEditViewModel): ViewModel
}
