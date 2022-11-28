package com.hackernews.ui

import android.os.Bundle
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.isVisible
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.paging.ExperimentalPagingApi
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.android.material.snackbar.Snackbar
import com.hackernews.databinding.ActivityMainBinding
import com.hackernews.util.events.UiEvent
import com.hackernews.util.states.StoriesViewState
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import javax.inject.Inject

@AndroidEntryPoint
@ExperimentalPagingApi
class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding

    private val mainActivityViewModel: MainActivityViewModel by viewModels()

    @Inject
    lateinit var storiesAdapter: StoriesAdapter

    override fun onCreate(savedInstanceState: Bundle?) {

        super.onCreate(savedInstanceState)

        binding = ActivityMainBinding.inflate(layoutInflater)

        setContentView(binding.root)

        initRecyclerView()

        setOnClickListeners()

        dataCollections()

    }

    private fun dataCollections() {

        with(binding) {

            lifecycleScope.launch {

                repeatOnLifecycle(Lifecycle.State.STARTED) {

                    mainActivityViewModel.viewState.collectLatest { storiesViewState ->

                        when (storiesViewState) {

                            is StoriesViewState.None -> srlStories.isRefreshing = false

                            is StoriesViewState.Loading -> srlStories.isRefreshing = true

                            is StoriesViewState.Success -> srlStories.isRefreshing = false

                        }

                    }

                }

            }

            lifecycleScope.launch {

                repeatOnLifecycle(Lifecycle.State.STARTED) {

                    mainActivityViewModel
                        .getStories()
                        .collectLatest { stories ->

                            storiesAdapter.submitData(stories)

                            tvDataNotFound.isVisible = storiesAdapter.itemCount == 0

                        }

                }

            }

            lifecycleScope.launch {

                repeatOnLifecycle(Lifecycle.State.STARTED) {

                    mainActivityViewModel.uiEvent.collectLatest { uiEvent ->

                        when (uiEvent) {

                            is UiEvent.SnackBarEvent -> Snackbar.make(
                                root,
                                uiEvent.uiText.asString(applicationContext),
                                Snackbar.LENGTH_LONG
                            ).show()

                            is UiEvent.Toast -> Toast.makeText(
                                applicationContext,
                                uiEvent.uiText.asString(applicationContext),
                                Toast.LENGTH_LONG
                            ).show()

                        }

                    }

                }

            }

        }

    }

    private fun initRecyclerView() {

        with(binding) {

            rvStories.apply {

                setHasFixedSize(true)

                layoutManager = LinearLayoutManager(this@MainActivity)

                adapter = storiesAdapter

            }

        }

    }

    private fun setOnClickListeners() {

        with(binding) {

            srlStories.setOnRefreshListener {

                mainActivityViewModel.loadTopStories()

            }

        }

    }

}