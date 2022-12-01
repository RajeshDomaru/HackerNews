package com.hackernews.data.cache.entities

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "story_tbl")
data class StoryEntity(
    @PrimaryKey
    var id: Int = 0,
    var `by`: String? = null,
    var descendants: Int = 0,
    var kids: String? = null,
    var score: Int = 0,
    var time: Int = 0,
    var title: String? = null,
    var type: String? = null,
    var url: String? = null
)
