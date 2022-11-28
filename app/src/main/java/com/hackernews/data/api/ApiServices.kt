package com.hackernews.data.api

import com.hackernews.data.api.dto.ArticleResponseDto
import retrofit2.http.GET
import retrofit2.http.Path

interface ApiServices {

    @GET("/v0/topstories.json?print=pretty")
    suspend fun getTopStories(): NetworkResponse<List<Int?>?, ErrorResponse>

    @GET("/v0/item/{articleid}.json?print=pretty")
    suspend fun getFirstArticle(@Path("articleid") articleId: Int): NetworkResponse<ArticleResponseDto?, ErrorResponse>

}