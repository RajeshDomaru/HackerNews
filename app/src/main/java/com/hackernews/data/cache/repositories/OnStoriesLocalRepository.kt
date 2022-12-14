package com.hackernews.data.cache.repositories

import androidx.paging.PagingSource
import com.hackernews.data.cache.HackerNewsDB
import com.hackernews.data.cache.entities.StoryEntity
import javax.inject.Inject

interface OnStoriesLocalRepository {

    suspend fun saveStory(vararg storyEntities: StoryEntity)

    fun getAllStories(): PagingSource<Int, StoryEntity>

    fun storySize(): Int

    fun getSearchStories(searchString: String): PagingSource<Int, StoryEntity>

    suspend fun clearAll()

}

class StoriesLocalRepositoryImpl @Inject constructor(
    private val hackerNewsDB: HackerNewsDB
) : OnStoriesLocalRepository {

    override suspend fun saveStory(vararg storyEntities: StoryEntity) =
        hackerNewsDB.storyDao.saveStory(*storyEntities)

    override fun getAllStories(): PagingSource<Int, StoryEntity> =
        hackerNewsDB.storyDao.getAllStories()

    override fun storySize(): Int =
        hackerNewsDB.storyDao.storySize()

    override fun getSearchStories(searchString: String): PagingSource<Int, StoryEntity> =
        hackerNewsDB.storyDao.getSearchStories(searchString)

    override suspend fun clearAll() =
        hackerNewsDB.storyDao.clearAll()

}