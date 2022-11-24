package com.hackernews.ui

import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import androidx.lifecycle.viewModelScope
import com.hackernews.data.api.interceptors.InternetService
import com.hackernews.data.api.repositories.StoriesRemoteRepositoryImpl
import com.hackernews.data.cache.repositories.StoriesLocalRepositoryImpl
import com.hackernews.util.states.StoriesViewState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import javax.inject.Inject

@HiltViewModel
class MainActivityViewModel @Inject constructor(
    private val storiesRemoteRepositoryImpl: StoriesRemoteRepositoryImpl,
    storiesLocalRepositoryImpl: StoriesLocalRepositoryImpl
) : ViewModel() {

    private val _viewState: MutableStateFlow<StoriesViewState> =
        MutableStateFlow(StoriesViewState.Init)
    val viewState get() = _viewState.asStateFlow()

    private val _getAllStories = storiesLocalRepositoryImpl.getAllStories()
    val getAllStories get() = _getAllStories.asLiveData()

    init {
        loadTopStories()
    }

    private fun loadTopStories() {

        viewModelScope.launch {

            _viewState.update { StoriesViewState.Loading }

            if (InternetService.instance.isOnline()) {

                val response = withContext(Dispatchers.IO) {
                    storiesRemoteRepositoryImpl.onStoriesResponse()
                }

                if (!response.first) {
                    _viewState.update { StoriesViewState.Error(response.second) }
                }

            }

        }

    }

}