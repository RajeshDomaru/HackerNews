package com.hackernews.ui.home

import android.os.Bundle
import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
import android.view.View
import android.widget.Toast
import androidx.appcompat.widget.SearchView
import androidx.core.view.MenuHost
import androidx.core.view.MenuProvider
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.navigation.fragment.findNavController
import androidx.paging.ExperimentalPagingApi
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.android.material.snackbar.Snackbar
import com.hackernews.R
import com.hackernews.databinding.FragmentStoriesBinding
import com.hackernews.util.events.UiEvent
import com.hackernews.util.extensions.getStringResources
import com.hackernews.util.extensions.isValidString
import com.hackernews.util.extensions.validateString
import com.hackernews.util.states.StoriesViewState
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

@AndroidEntryPoint
@ExperimentalPagingApi
class StoriesFragment : Fragment(R.layout.fragment_stories) {

    private lateinit var binding: FragmentStoriesBinding

    private val storiesViewModel: StoriesViewModel by viewModels()

    private lateinit var storiesAdapter: StoriesAdapter

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {

        super.onViewCreated(view, savedInstanceState)

        binding = FragmentStoriesBinding.bind(view)

        initialization()

        initRecyclerView()

        setOnClickListeners()

        dataCollections()

    }

    private fun dataCollections() {

        with(binding) {

            viewLifecycleOwner.lifecycleScope.launch {

                repeatOnLifecycle(Lifecycle.State.STARTED) {

                    storiesViewModel.viewState.collectLatest { storiesViewState ->

                        when (storiesViewState) {

                            is StoriesViewState.None -> srlStories.isRefreshing = false

                            is StoriesViewState.Loading -> srlStories.isRefreshing = true

                            is StoriesViewState.Success -> srlStories.isRefreshing = false

                        }

                    }

                }

            }

            storiesViewModel.stories.observe(viewLifecycleOwner) { stories ->

                viewLifecycleOwner.lifecycleScope.launch(Dispatchers.Main) {

                    storiesAdapter.submitData(stories)

                }

            }

            viewLifecycleOwner.lifecycleScope.launch {

                repeatOnLifecycle(Lifecycle.State.STARTED) {

                    storiesViewModel.uiEvent.collectLatest { uiEvent ->

                        when (uiEvent) {

                            is UiEvent.SnackBarEvent -> Snackbar.make(
                                root,
                                uiEvent.uiText.asString(requireContext()),
                                Snackbar.LENGTH_LONG
                            ).show()

                            is UiEvent.Toast -> Toast.makeText(
                                requireContext(),
                                uiEvent.uiText.asString(requireContext()),
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

                layoutManager = LinearLayoutManager(requireContext())

                storiesAdapter = StoriesAdapter { url, title ->

                    if (url.isValidString() && title.isValidString()) {
                        findNavController().navigate(
                            StoriesFragmentDirections.actionHomeFragmentToWebViewFragment(
                                url.validateString(), title.validateString()
                            )
                        )
                    } else Snackbar.make(
                        root,
                        requireContext().getStringResources(R.string.page_not_found),
                        Snackbar.LENGTH_LONG
                    ).show()

                }

                adapter = storiesAdapter

            }

        }

    }

    private fun setOnClickListeners() {

        with(binding) {

            srlStories.setOnRefreshListener {

                storiesViewModel.loadTopStories()

            }

            storiesAdapter.addLoadStateListener {

                tvDataNotFound.isVisible = storiesAdapter.itemCount == 0

            }

        }

    }

    private fun initialization() {

        val menuHost: MenuHost = requireActivity()

        menuHost.addMenuProvider(object : MenuProvider {

            override fun onCreateMenu(menu: Menu, menuInflater: MenuInflater) {

                menuInflater.inflate(R.menu.main_activity_menu, menu)

                val searchItem: MenuItem = menu.findItem(R.id.actionStoriesSearch)

                val searchView: SearchView = searchItem.actionView as SearchView

                searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener,

                    android.widget.SearchView.OnQueryTextListener {

                    override fun onQueryTextSubmit(p0: String?): Boolean = false

                    override fun onQueryTextChange(searchQuery: String): Boolean {

                        storiesViewModel.searchStories(searchQuery)

                        return false

                    }

                })

            }

            override fun onMenuItemSelected(menuItem: MenuItem): Boolean {

                return when (menuItem.itemId) {
                    R.id.actionStoriesSearch -> true
                    else -> false
                }

            }

        }, viewLifecycleOwner, Lifecycle.State.RESUMED)

    }

}