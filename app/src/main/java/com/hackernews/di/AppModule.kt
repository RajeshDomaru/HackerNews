package com.hackernews.di

import android.app.Application
import androidx.room.Room
import com.google.gson.GsonBuilder
import com.hackernews.data.api.ApiServices
import com.hackernews.data.api.NetworkResponseAdapterFactory
import com.hackernews.data.cache.HackerNewsDB
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import okhttp3.ConnectionPool
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object AppModule {

    @Provides
    @Singleton
    fun provideHackerNewsDB(application: Application) =
        Room.databaseBuilder(application, HackerNewsDB::class.java, HackerNewsDB.DATABASE_NAME)
            .build()

    @Provides
    @Singleton
    fun provideRetrofit(): ApiServices {

        val logging = HttpLoggingInterceptor()
        logging.setLevel(HttpLoggingInterceptor.Level.BODY)

        val client = OkHttpClient.Builder()
        client.readTimeout(30, TimeUnit.SECONDS)
            .callTimeout(30, TimeUnit.SECONDS)
            .connectTimeout(30, TimeUnit.SECONDS)
            .writeTimeout(30, TimeUnit.SECONDS)
            .retryOnConnectionFailure(true)
            .connectionPool(ConnectionPool(30, 30, TimeUnit.SECONDS))
            .addInterceptor(logging)

        val gson = GsonBuilder().setLenient().create()

        val retrofit = Retrofit.Builder().baseUrl("https://hacker-news.firebaseio.com")
            .client(client.build())
            .addCallAdapterFactory(NetworkResponseAdapterFactory())
            .addConverterFactory(GsonConverterFactory.create(gson))
            .build()

        return retrofit.create(ApiServices::class.java)

    }

}