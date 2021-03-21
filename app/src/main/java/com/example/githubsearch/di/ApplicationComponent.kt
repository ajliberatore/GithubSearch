package com.example.githubsearch.di

import android.app.Application
import com.example.data.di.NetworkModule
import com.example.data.repository.GithubUserSearchRepository
import com.example.domain.repository.UserSearchRepository
import com.example.githubsearch.BaseApplication
import dagger.*
import dagger.android.AndroidInjector
import dagger.android.support.AndroidSupportInjectionModule
import javax.inject.Singleton

@Singleton
@Component(modules = [
    AppModule::class,
    ActivityBindingModule::class,
    FragmentBindingModule::class,
    AndroidSupportInjectionModule::class
])
interface ApplicationComponent: AndroidInjector<BaseApplication> {
    @Component.Builder
    interface Builder {
        @BindsInstance
        fun application(application: Application): Builder

        fun build(): ApplicationComponent
    }
}

@Module(includes = [NetworkModule::class])
interface AppModule {
    @Binds
    fun bindUserSearchRepository(githubUserSearchRepository: GithubUserSearchRepository): UserSearchRepository
}
