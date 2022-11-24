package com.hackernews.data.cache.repositories

import com.hackernews.data.cache.HackerNewsDB
import com.hackernews.data.cache.entities.StoryEntity
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

interface OnStoriesLocalRepository {

    suspend fun saveStory(vararg storyEntities: StoryEntity)

    fun getAllStories(): Flow<List<StoryEntity>>

}

class StoriesLocalRepositoryImpl @Inject constructor(
    private val hackerNewsDB: HackerNewsDB
) : OnStoriesLocalRepository {

    override suspend fun saveStory(vararg storyEntities: StoryEntity) {
        hackerNewsDB.storyDao.saveStory(*storyEntities)
    }

    override fun getAllStories(): Flow<List<StoryEntity>> =
        hackerNewsDB.storyDao.getAllStories()

}