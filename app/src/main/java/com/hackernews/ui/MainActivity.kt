package com.hackernews.ui

import android.os.Bundle
import android.util.Log
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import com.hackernews.databinding.ActivityMainBinding
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

@AndroidEntryPoint
class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding

    private val mainActivityViewModel: MainActivityViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {

        super.onCreate(savedInstanceState)

        binding = ActivityMainBinding.inflate(layoutInflater)

        setContentView(binding.root)

        initialization()

    }

    private fun initialization() {

        lifecycleScope.launch {

            repeatOnLifecycle(Lifecycle.State.STARTED) {

                mainActivityViewModel.viewState.collectLatest {


                }

            }

        }

        lifecycleScope.launch {

            repeatOnLifecycle(Lifecycle.State.STARTED) {

                mainActivityViewModel.getAllStories.observeForever {

                    Log.e("initialization", "${it.size}")

                }

            }

        }

    }

}