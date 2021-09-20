package com.intive.tmdbandroid.common

import androidx.recyclerview.widget.DiffUtil
import com.intive.tmdbandroid.model.TVShow

class TVShowAsyncPagingDataDiffCallback : DiffUtil.ItemCallback<TVShow>() {
    override fun areItemsTheSame(oldItem: TVShow, newItem: TVShow): Boolean {
        return oldItem.id == newItem.id
    }

    override fun areContentsTheSame(oldItem: TVShow, newItem: TVShow): Boolean {
        return oldItem == newItem
    }
}