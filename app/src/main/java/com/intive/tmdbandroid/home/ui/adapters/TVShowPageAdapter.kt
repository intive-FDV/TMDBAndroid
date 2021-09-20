package com.intive.tmdbandroid.home.ui.adapters

import android.content.res.Resources
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.paging.AsyncPagingDataDiffer
import androidx.paging.PagingData
import androidx.recyclerview.widget.*
import com.intive.tmdbandroid.R
import com.intive.tmdbandroid.databinding.ItemHorizontalListBinding
import com.intive.tmdbandroid.databinding.ItemScreeningBinding
import com.intive.tmdbandroid.databinding.ItemTitleBinding
import com.intive.tmdbandroid.model.TVShow

class TVShowPageAdapter(private val clickListener: ((TVShow) -> Unit)) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    val adapterCallback = AdapterListUpdateCallback(this)

    companion object {
        private const val HEADER = 0
        private const val WATCHLIST = 1
        private const val POPULAR = 2
    }

    private val differ = AsyncPagingDataDiffer(
        TVShowAsyncPagingDataDiffCallback(),
        object : ListUpdateCallback {
            override fun onInserted(position: Int, count: Int) {
                adapterCallback.onInserted(position + 1, count);
            }

            override fun onRemoved(position: Int, count: Int) {
                adapterCallback.onRemoved(position + 1, count);
            }

            override fun onMoved(fromPosition: Int, toPosition: Int) {
                adapterCallback.onMoved(fromPosition + 1, toPosition + 1);
            }

            override fun onChanged(position: Int, count: Int, payload: Any?) {
                adapterCallback.onChanged(position + 1, count, payload);
            }

        }
    )

    suspend fun submitData(tvShowPagingData: PagingData<TVShow>) {
        differ.submitData(tvShowPagingData)
    }

    private val watchlistAdapter = WatchlistAdapter(clickListener)

    override fun getItemCount(): Int {
        return differ.itemCount + 3
    }

    fun refreshWatchlistAdapter(list: List<TVShow>) {
        watchlistAdapter.submitList(list)
    }

    override fun getItemViewType(position: Int): Int {
        return when (position) {
            0, 2 -> HEADER
            1 -> WATCHLIST
            else -> POPULAR
        }
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        when (holder) {
            is HeaderHolder -> holder.bind(position)
            is WatchlistHolder -> holder.bind()
            is TVShowHolder -> differ.getItem(position - 3)?.let { holder.bind(it) }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return when (viewType) {
            HEADER -> HeaderHolder(ItemTitleBinding.inflate(LayoutInflater.from(parent.context), parent, false))
            WATCHLIST -> WatchlistHolder(ItemHorizontalListBinding.inflate(LayoutInflater.from(parent.context), parent, false))
            POPULAR -> TVShowHolder(ItemScreeningBinding.inflate(LayoutInflater.from(parent.context), parent, false), clickListener)
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


private class TVShowAsyncPagingDataDiffCallback : DiffUtil.ItemCallback<TVShow>() {
    override fun areItemsTheSame(oldItem: TVShow, newItem: TVShow): Boolean {
        return oldItem.id == newItem.id
    }

    override fun areContentsTheSame(oldItem: TVShow, newItem: TVShow): Boolean {
        return oldItem == newItem
    }
}