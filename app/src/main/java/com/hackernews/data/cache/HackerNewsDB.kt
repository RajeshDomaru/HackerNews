package com.hackernews.data.cache

import androidx.room.Database
import androidx.room.RoomDatabase
import com.hackernews.data.cache.dao.StoryDao
import com.hackernews.data.cache.entities.StoryEntity

@Database(entities = [StoryEntity::class], version = 1)
abstract class HackerNewsDB : RoomDatabase() {

    abstract val storyDao: StoryDao

    companion object {

        const val DATABASE_NAME = "hacker_news_db"

    }

}