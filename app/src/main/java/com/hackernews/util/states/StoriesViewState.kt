package com.hackernews.util.states

sealed class StoriesViewState {

    object None : StoriesViewState()

    object Loading : StoriesViewState()

    object Success : StoriesViewState()

}
