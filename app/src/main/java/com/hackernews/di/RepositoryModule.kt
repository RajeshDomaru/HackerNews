package com.hackernews.di

import com.hackernews.data.api.ApiServices
import com.hackernews.data.api.repositories.OnStoriesRemoteRepository
import com.hackernews.data.api.repositories.StoriesRemoteRepositoryImpl
import com.hackernews.data.cache.HackerNewsDB
import com.hackernews.data.cache.repositories.OnStoriesLocalRepository
import com.hackernews.data.cache.repositories.StoriesLocalRepositoryImpl
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object RepositoryModule {

    @Provides
    @Singleton
    fun providesStoryRemoteRepository(apiServices: ApiServices):
            OnStoriesRemoteRepository = StoriesRemoteRepositoryImpl(apiServices)

    @Provides
    @Singleton
    fun providesStoryLocalRepository(hackerNewsDB: HackerNewsDB):
            OnStoriesLocalRepository = StoriesLocalRepositoryImpl(hackerNewsDB)

}