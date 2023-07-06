package com.serelik.todoapp.di

import androidx.lifecycle.ViewModel
import com.serelik.todoapp.list.TodoListFragment
import com.serelik.todoapp.list.TodoListViewModel
import dagger.Binds
import dagger.Module
import dagger.Subcomponent
import dagger.multibindings.IntoMap
import javax.inject.Scope

@Scope
annotation class TodoListFragmentComponentScope

@Subcomponent(modules = [TodoListViewModelModule::class])
@TodoListFragmentComponentScope
interface TodoListFragmentComponent {
    fun inject(fragment: TodoListFragment)

    @Subcomponent.Factory
    interface Factory {

        fun create(): TodoListFragmentComponent
    }
}

@Module
interface TodoListViewModelModule {
    @Binds
    @[IntoMap ViewModelKey(TodoListViewModel::class)]
    fun provideTodoListViewModel(todoListViewModel: TodoListViewModel): ViewModel
}