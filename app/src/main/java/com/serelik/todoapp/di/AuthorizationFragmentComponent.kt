package com.serelik.todoapp.di

import com.serelik.todoapp.authorizationFragment.AuthorizationFragment
import com.serelik.todoapp.edit.TodoEditFragment
import dagger.Subcomponent
import javax.inject.Scope

@Scope
annotation class AuthorizationFragmentComponentScope

@Subcomponent
@AuthorizationFragmentComponentScope
interface AuthorizationFragmentComponent {
    fun inject(fragment: AuthorizationFragment)

    @Subcomponent.Factory
    interface Factory {

        fun create(): AuthorizationFragmentComponent
    }
}