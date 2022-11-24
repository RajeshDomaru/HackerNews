package com.hackernews.data.cache

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.hackernews.data.cache.entities.StoryEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface StoryDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun saveStory(vararg storyEntities: StoryEntity)

    @Query("SELECT * FROM story_tbl")
    fun getAllStories(): Flow<List<StoryEntity>>

}