package com.intive.tmdbandroid.home.ui.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import com.intive.tmdbandroid.databinding.ItemScreeningWatchlistBinding
import com.intive.tmdbandroid.model.TVShow
import timber.log.Timber

class WatchlistAdapter(private val clickListener: ((TVShow) -> Unit)) : ListAdapter<TVShow, WatchlistHolder>(COMPARATOR) {
    var widthSize: Int = 0

    companion object {
        private val COMPARATOR = object : DiffUtil.ItemCallback<TVShow>() {
            override fun areItemsTheSame(oldItem: TVShow, newItem: TVShow): Boolean = (oldItem == newItem)
            override fun areContentsTheSame(oldItem: TVShow, newItem: TVShow): Boolean = (oldItem == newItem)
        }
    }

    override fun onBindViewHolder(holder: WatchlistHolder, position: Int) {
        holder.bind(getItem(position))
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): WatchlistHolder {
        val binding = ItemScreeningWatchlistBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        Timber.i("MAS - width: $widthSize")
        binding.containerWatchlistScreening.layoutParams.width = widthSize

        return WatchlistHolder(
            binding,
            clickListener
        )
    }
}
