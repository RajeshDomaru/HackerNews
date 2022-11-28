package com.hackernews.data.api.dto

import com.hackernews.data.cache.entities.StoryEntity

data class ArticleResponseDto(
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
            kids = kids?.toString() ?: "",
            score = score ?: 0,
            time = time ?: 0,
            title = title ?: "",
            type = type ?: "",
            url = url ?: ""
        )
    }

}
