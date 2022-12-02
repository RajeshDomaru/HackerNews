package com.hackernews.ui.home

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.paging.PagingDataAdapter
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.snackbar.Snackbar
import com.hackernews.R
import com.hackernews.data.api.interceptors.InternetService
import com.hackernews.data.cache.entities.StoryEntity
import com.hackernews.databinding.ItemStoryBinding
import com.hackernews.util.extensions.getStringResources
import com.hackernews.util.extensions.validateString

class StoriesAdapter(private val itemClicked: (url: String?, title: String?) -> Unit) :
    PagingDataAdapter<StoryEntity, StoriesAdapter.StoriesViewHolder>(StoriesDiffUtils) {

    inner class StoriesViewHolder(private val binding: ItemStoryBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(storyEntity: StoryEntity) {

            with(storyEntity) {

                with(binding) {

                    tvTitle.text = title.validateString()

                    tvBy.text = by.validateString()

                    root.setOnClickListener {

                        if (InternetService.instance.isOnline()) itemClicked.invoke(url, title)
                        else Snackbar.make(
                            root,
                            root.context.getStringResources(R.string.no_internet),
                            Snackbar.LENGTH_LONG
                        ).show()

                    }

                }

            }

        }

    }

    object StoriesDiffUtils : DiffUtil.ItemCallback<StoryEntity>() {

        override fun areItemsTheSame(oldItem: StoryEntity, newItem: StoryEntity): Boolean {
            return oldItem.id == newItem.id
        }

        override fun areContentsTheSame(oldItem: StoryEntity, newItem: StoryEntity): Boolean {
            return oldItem == newItem
        }

    }

    override fun onBindViewHolder(holder: StoriesViewHolder, position: Int) {

        getItem(position)?.let { storyEntity ->

            holder.bind(storyEntity)

        }

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): StoriesViewHolder {

        return StoriesViewHolder(
            ItemStoryBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        )

    }

}