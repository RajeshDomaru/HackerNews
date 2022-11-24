package com.hackernews.util.states

import com.hackernews.util.UiText

sealed class StoriesViewState {

    object Init : StoriesViewState()

    object Loading : StoriesViewState()

    object Success : StoriesViewState()

    data class Error(val uiText: UiText, val errorCode: Int? = null) : StoriesViewState()

}
