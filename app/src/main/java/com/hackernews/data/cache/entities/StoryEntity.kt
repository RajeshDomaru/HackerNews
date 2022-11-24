package com.hackernews.data.cache.entities

import androidx.room.Entity
import androidx.room.Ignore
import androidx.room.PrimaryKey

@Entity(tableName = "story_tbl")
data class StoryEntity(
    @PrimaryKey
    var id: Int = 0,
    var `by`: String = "",
    var descendants: Int = 0,
    @Ignore
    var kids: List<Int> = listOf(),
    var score: Int = 0,
    var time: Int = 0,
    var title: String = "",
    var type: String = "",
    var url: String = ""
)
