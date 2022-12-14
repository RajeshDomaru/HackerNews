package com.hackernews.ui.home

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.switchMap
import androidx.lifecycle.viewModelScope
import androidx.paging.*
import com.hackernews.R
import com.hackernews.data.api.ApiResponse
import com.hackernews.data.api.interceptors.InternetService
import com.hackernews.data.api.repositories.StoriesRemoteRepositoryImpl
import com.hackernews.data.cache.repositories.StoriesLocalRepositoryImpl
import com.hackernews.util.UiText
import com.hackernews.util.events.StoriesEvent
import com.hackernews.util.events.UiEvent
import com.hackernews.util.extensions.isValidString
import com.hackernews.util.states.StoriesViewState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class StoriesViewModel @Inject constructor(
    private val storiesRemoteRepositoryImpl: StoriesRemoteRepositoryImpl,
    private val storiesLocalRepositoryImpl: StoriesLocalRepositoryImpl
) : ViewModel() {

    private val _viewState: MutableStateFlow<StoriesViewState> =
        MutableStateFlow(StoriesViewState.None)
    val viewState get() = _viewState.asStateFlow()

    private val _uiEvent = Channel<UiEvent>()
    val uiEvent get() = _uiEvent.receiveAsFlow()

    private val currentQuery = MutableLiveData("")

    @ExperimentalPagingApi
    val stories = currentQuery.switchMap { searchString ->
        Pager(config = PagingConfig(100, enablePlaceholders = false)) {
            if (searchString.isValidString())
                storiesLocalRepositoryImpl.getSearchStories(searchString)
            else storiesLocalRepositoryImpl.getAllStories()
        }.liveData.cachedIn(viewModelScope)
    }

    fun searchStories(searchString: String) {
        currentQuery.value = searchString
    }

    init {
        loadTopStories()
    }

    fun loadTopStories(isFromSwipeRefresh: Boolean = false) {

        viewModelScope.launch(Dispatchers.IO) {

            if (InternetService.instance.isOnline()) {

                if (storiesLocalRepositoryImpl.storySize() <= 0 || isFromSwipeRefresh) {

                    _viewState.update { StoriesViewState.Loading }

                    when (val storiesEvent = storiesRemoteRepositoryImpl.onStoriesResponse()) {

                        is StoriesEvent.EmptyData -> {

                            _viewState.update { StoriesViewState.None }

                            _uiEvent.send(
                                UiEvent.SnackBarEvent(UiText.StringResource(R.string.stories_not_found))
                            )

                        }

                        is StoriesEvent.Error -> {

                            _viewState.update { StoriesViewState.None }

                            _uiEvent.send(UiEvent.SnackBarEvent(storiesEvent.uiText))

                        }

                        is StoriesEvent.Success -> {

                            // Clearing old local articles before inserting new articles
                            storiesLocalRepositoryImpl.clearAll()

                            // Fetching article data from remote and inserting into local Database
                            storiesEvent.articlesIds.sortedDescending().forEach { articlesId ->

                                when (val articleResponse =
                                    storiesRemoteRepositoryImpl.onArticleResponse(articlesId)) {
                                    is ApiResponse.EmptyData -> {}
                                    is ApiResponse.Failure -> {}
                                    is ApiResponse.Success -> {
                                        articleResponse.data?.let { notNullArticleResponse ->
                                            storiesLocalRepositoryImpl.saveStory(
                                                notNullArticleResponse.toStoryEntity()
                                            )
                                        }
                                    }
                                }

                            }

                            _viewState.update { StoriesViewState.Success }

                        }

                    }

                }

            } else {

                _viewState.update { StoriesViewState.Success }

                _uiEvent.send(
                    UiEvent.SnackBarEvent(UiText.StringResource(R.string.no_internet))
                )

            }

        }

    }

}
