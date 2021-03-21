package com.example.data.di

import android.app.Application
import com.example.data.network.GithubPlatform
import com.example.data.network.GithubService
import dagger.Module
import dagger.Provides
import java.io.File

@Module
class NetworkModule {

    @Provides
    fun provideGithubService(githubPlatform: GithubPlatform): GithubService = githubPlatform.githubService

    @Provides
    fun provideCacheDir(application: Application): File = application.applicationContext.cacheDir
}
