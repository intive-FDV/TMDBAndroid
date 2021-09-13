package com.intive.tmdbandroid.home.ui.adapters

import android.content.res.Resources
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.paging.PagingDataAdapter
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.intive.tmdbandroid.R
import com.intive.tmdbandroid.databinding.ItemHorizontalListBinding
import com.intive.tmdbandroid.databinding.ItemScreeningBinding
import com.intive.tmdbandroid.databinding.ItemTitleBinding
import com.intive.tmdbandroid.model.TVShow

class TVShowPageAdapter(private val clickListener: ((TVShow) -> Unit)) : PagingDataAdapter<TVShow, RecyclerView.ViewHolder>(COMPARATOR) {
    companion object {
        private val COMPARATOR = object : DiffUtil.ItemCallback<TVShow>() {
            override fun areItemsTheSame(oldItem: TVShow, newItem: TVShow): Boolean = (oldItem == newItem)
            override fun areContentsTheSame(oldItem: TVShow, newItem: TVShow): Boolean = (oldItem == newItem)
        }

        private const val HEADER = 0
        private const val WATCHLIST = 1
        private const val POPULAR = 2
    }

    private val watchlistAdapter = WatchlistAdapter(clickListener)

    fun refreshWatchlistAdapter(list: List<TVShow>) {
        watchlistAdapter.submitList(list)
    }

    override fun getItemViewType(position: Int): Int {
        return when (position) {
            0,2 -> HEADER
            1 -> WATCHLIST
            else -> POPULAR
        }
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        when (holder) {
            is HeaderHolder -> holder.bind(position)
            is WatchlistHolder -> holder.bind()
            is TVShowHolder -> getItem(position - 3)?.let { holder.bind(it) }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return when (viewType) {
            HEADER -> HeaderHolder(ItemTitleBinding.inflate(LayoutInflater.from(parent.context), parent, false))
            WATCHLIST -> WatchlistHolder(ItemHorizontalListBinding.inflate(LayoutInflater.from(parent.context), parent, false))
            POPULAR -> TVShowHolder(ItemScreeningBinding.inflate(LayoutInflater.from(parent.context), parent, false), clickListener )
            else -> throw Exception("Illegal ViewType")
        }
    }

    inner class HeaderHolder(binding: ItemTitleBinding) : RecyclerView.ViewHolder(binding.root) {
        private val header = binding.tvTitle

        private val res: Resources = binding.root.resources
        private val watchlist = res.getString(R.string.watchlist)
        private val popularTVShow = res.getString(R.string.popular_tvShows)

        fun bind(position: Int) {
            when (position) {
                0 -> header.text = watchlist
                2 -> header.text = popularTVShow
                else -> throw IndexOutOfBoundsException("Illegal position")
            }
        }
    }

    inner class WatchlistHolder(binding: ItemHorizontalListBinding) : RecyclerView.ViewHolder(binding.root) {
        private val rvWatchlist = binding.rvWatchlistTVShows

        fun bind() {
            rvWatchlist.apply {
                layoutManager = LinearLayoutManager(itemView.context, LinearLayoutManager.HORIZONTAL, false)
                adapter = watchlistAdapter
            }
        }
    }
}