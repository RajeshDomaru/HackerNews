package com.hackernews.ui

import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.SearchView
import androidx.core.view.isVisible
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.paging.ExperimentalPagingApi
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.android.material.snackbar.Snackbar
import com.hackernews.R
import com.hackernews.databinding.ActivityMainBinding
import com.hackernews.util.events.UiEvent
import com.hackernews.util.states.StoriesViewState
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.Dispatchers
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

            mainActivityViewModel.stories.observe(this@MainActivity) { stories ->

                lifecycleScope.launch(Dispatchers.Main) {

                    storiesAdapter.submitData(stories)

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

            storiesAdapter.addLoadStateListener {

                tvDataNotFound.isVisible = storiesAdapter.itemCount == 0

            }

        }

    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {

        menuInflater.inflate(R.menu.main_activity_menu, menu)

        val searchItem: MenuItem = menu.findItem(R.id.actionStoriesSearch)

        val searchView: SearchView = searchItem.actionView as SearchView

        searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener,

            android.widget.SearchView.OnQueryTextListener {

            override fun onQueryTextSubmit(p0: String?): Boolean = false

            override fun onQueryTextChange(searchQuery: String): Boolean {

                mainActivityViewModel.searchStories(searchQuery)

                return false

            }

        })

        return true

    }

}