package com.example.githubsearch.di

import com.example.githubsearch.MainActivity
import com.example.githubsearch.search.SearchUserFragment
import dagger.Module
import dagger.android.ContributesAndroidInjector
import javax.inject.Scope

@Scope
@Retention(AnnotationRetention.RUNTIME)
annotation class ActivityScope

// Activity
@Module
interface ActivityBindingModule {
    @ActivityScope
    @ContributesAndroidInjector
    fun contributeMainActivity(): MainActivity
}

@Module
interface FragmentBindingModule {
    @ActivityScope // This will be okay for our purposes
    @ContributesAndroidInjector
    fun contributeSearchUserFragment(): SearchUserFragment
}
