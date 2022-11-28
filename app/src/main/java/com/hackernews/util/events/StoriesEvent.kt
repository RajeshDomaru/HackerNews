package com.hackernews.util.events

import com.hackernews.util.UiText

sealed class StoriesEvent {

    data class Success(val articlesIds: List<Int>) : StoriesEvent()

    object EmptyData : StoriesEvent()

    data class Error(val uiText: UiText, val errorCode: Int? = null) : StoriesEvent()

}
