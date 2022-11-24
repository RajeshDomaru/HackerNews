package com.hackernews.data.api.dto

import com.hackernews.data.cache.entities.StoryEntity

data class ArticleResponse(
    val id: Int?,
    val `by`: String?,
    val descendants: Int?,
    val kids: List<Int?>?,
    val score: Int?,
    val time: Int?,
    val title: String?,
    val type: String?,
    val url: String?
) {

    fun toStoryEntity(): StoryEntity {
        return StoryEntity(
            id = id ?: 0,
            by = by ?: "",
            descendants = descendants ?: 0,
            kids = kids?.filterNotNull() ?: listOf(),
            score = score ?: 0,
            time = time ?: 0,
            title = type ?: "",
            url = url ?: ""
        )
    }

}
