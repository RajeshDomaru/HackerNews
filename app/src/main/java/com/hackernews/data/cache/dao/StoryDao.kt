package com.hackernews.data.cache.dao

import androidx.paging.PagingSource
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.hackernews.data.cache.entities.StoryEntity

@Dao
interface StoryDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun saveStory(vararg storyEntities: StoryEntity)

    @Query("SELECT * FROM story_tbl")
    fun getAllStories(): PagingSource<Int, StoryEntity>

    @Query("SELECT * FROM story_tbl WHERE title LIKE '%' || :searchString || '%' OR `by` LIKE '%' || :searchString || '%'")
    fun getSearchStories(searchString: String): PagingSource<Int, StoryEntity>

    @Query("DELETE FROM story_tbl")
    suspend fun clearAll()

}