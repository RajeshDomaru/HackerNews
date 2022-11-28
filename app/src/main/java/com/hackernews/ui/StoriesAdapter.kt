package com.hackernews.ui

import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.Toast
import androidx.paging.PagingDataAdapter
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.hackernews.data.cache.entities.StoryEntity
import com.hackernews.databinding.ItemStoryBinding
import javax.inject.Inject

class StoriesAdapter @Inject constructor() :
    PagingDataAdapter<StoryEntity, StoriesAdapter.StoriesViewHolder>(StoriesDiffUtils) {

    class StoriesViewHolder(private val binding: ItemStoryBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(storyEntity: StoryEntity) {

            with(storyEntity) {

                with(binding) {

                    tvTitle.text = title

                    root.setOnClickListener {

                        Toast.makeText(root.context, "Clicked", Toast.LENGTH_LONG).show()

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