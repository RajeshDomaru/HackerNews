package com.hackernews.data.api.repositories

import com.hackernews.data.api.ApiResponse
import com.hackernews.data.api.ApiServices
import com.hackernews.data.api.receiveNetworkCall
import com.hackernews.data.cache.HackerNewsDB
import com.hackernews.util.UiText
import javax.inject.Inject

interface OnStoriesRemoteRepository {

    suspend fun onStoriesResponse(): Pair<Boolean, UiText>

}

class StoriesRemoteRepositoryImpl @Inject constructor(
    private val apiServices: ApiServices,
    private val hackerNewsDB: HackerNewsDB
) : OnStoriesRemoteRepository {

    override suspend fun onStoriesResponse(): Pair<Boolean, UiText> {

        return when (val topStories = apiServices.getTopStories().receiveNetworkCall()) {

            is ApiResponse.EmptyData -> Pair(true, UiText.DynamicString(""))

            is ApiResponse.Failure -> Pair(false, topStories.uiText)

            is ApiResponse.Success -> {

                topStories.data.forEach { articleId ->

                    when (val articleResponse =
                        apiServices.getArticle(articleId).receiveNetworkCall()) {

                        is ApiResponse.EmptyData -> {}

                        is ApiResponse.Failure -> {}

                        is ApiResponse.Success -> {
                            hackerNewsDB.storyDao.saveStory(articleResponse.data.toStoryEntity())
                        }

                    }

                }

                Pair(true, UiText.DynamicString(""))

            }

        }

    }

}