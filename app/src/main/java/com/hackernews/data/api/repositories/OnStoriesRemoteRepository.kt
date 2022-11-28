package com.hackernews.data.api.repositories

import com.hackernews.data.api.ApiResponse
import com.hackernews.data.api.ApiServices
import com.hackernews.data.api.dto.ArticleResponseDto
import com.hackernews.data.api.receiveNetworkCall
import com.hackernews.util.events.StoriesEvent
import javax.inject.Inject

interface OnStoriesRemoteRepository {

    suspend fun onStoriesResponse(): StoriesEvent

    suspend fun onArticleResponse(articleId: Int): ApiResponse<ArticleResponseDto?>

}

class StoriesRemoteRepositoryImpl @Inject constructor(
    private val apiServices: ApiServices
) : OnStoriesRemoteRepository {

    override suspend fun onStoriesResponse(): StoriesEvent {

        return when (val topStories = apiServices.getTopStories().receiveNetworkCall()) {

            is ApiResponse.EmptyData -> StoriesEvent.EmptyData

            is ApiResponse.Failure -> StoriesEvent.Error(
                topStories.uiText,
                topStories.errorCode
            )

            is ApiResponse.Success -> {

                topStories.data?.filterNotNull()?.let { articleIds ->

                    StoriesEvent.Success(articleIds)

                } ?: StoriesEvent.EmptyData

            }

        }

    }

    override suspend fun onArticleResponse(articleId: Int): ApiResponse<ArticleResponseDto?> {

        return apiServices.getFirstArticle(articleId).receiveNetworkCall()

    }

}