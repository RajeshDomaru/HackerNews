package com.hackernews.di

import android.app.Application
import com.hackernews.data.api.interceptors.InternetService
import dagger.hilt.android.HiltAndroidApp

@HiltAndroidApp
class HackerNewsApplication : Application() {

    override fun onCreate() {

        super.onCreate()

        InternetService.instance.initializeWithApplicationContext(this)

    }

}