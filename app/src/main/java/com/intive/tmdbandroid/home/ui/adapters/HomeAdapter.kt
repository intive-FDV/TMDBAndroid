package com.intive.tmdbandroid.home.ui.adapters

import android.content.Context
import android.content.res.Resources
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.paging.PagingData
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.intive.tmdbandroid.R
import com.intive.tmdbandroid.databinding.ItemHorizontalListBinding
import com.intive.tmdbandroid.databinding.ItemTitleBinding
import com.intive.tmdbandroid.databinding.ItemVerticalListBinding
import com.intive.tmdbandroid.model.TVShow
import timber.log.Timber
import kotlin.math.floor
import android.view.MotionEvent
import androidx.recyclerview.widget.RecyclerView.OnItemTouchListener

class HomeAdapter(val context: Context) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {
    companion object {
        private const val HEADER = 0
        private const val WATCHLIST = 1
        private const val POPULAR = 2
    }

    private val watchlistAdapter = WatchlistAdapter()
    private val popularAdapter = TVShowPageAdapter()

    fun refreshWatchlistAdapter(list: List<TVShow>) {
        watchlistAdapter.submitList(list)
    }

    suspend fun refreshPopularAdapter(list: PagingData<TVShow>) {
        popularAdapter.submitData(list)
    }

    override fun getItemViewType(position: Int): Int {
        return when (position) {
            0,2 -> HEADER
            1 -> WATCHLIST
            3 -> POPULAR
            else -> throw IndexOutOfBoundsException("Illegal position")
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return when (viewType) {
            HEADER -> HeaderHolder(ItemTitleBinding.inflate(LayoutInflater.from(parent.context), parent, false))
            WATCHLIST -> WatchlistHolder(ItemHorizontalListBinding.inflate(LayoutInflater.from(parent.context), parent, false))
            POPULAR -> PopularHolder(ItemVerticalListBinding.inflate(LayoutInflater.from(parent.context), parent, false))
            else -> throw Exception("Illegal ViewType")
        }
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        Timber.i("MAS - HomeAdapter.onBindViewHolder(); - position: $position")

        when (holder) {
            is HeaderHolder -> holder.bind(position)
            is WatchlistHolder -> holder.bind()
            is PopularHolder -> holder.bind()
        }
    }

    override fun getItemCount() = 4

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
                addOnItemTouchListener(moveListener)
            }
        }
    }

    inner class PopularHolder(binding: ItemVerticalListBinding) : RecyclerView.ViewHolder(binding.root) {
        private val rvWatchlist = binding.rvWatchlistTVShows

        fun bind() {
            rvWatchlist.apply {
                val displayMetrics = rvWatchlist.context.resources.displayMetrics
                val dpWidth = displayMetrics.widthPixels / displayMetrics.density

                val scaling = 200
                val columnCount = floor(dpWidth / scaling).toInt()
                Timber.i("MAS - columnCount: $columnCount")

                layoutManager = GridLayoutManager(context, columnCount, GridLayoutManager.VERTICAL, false)
                adapter = popularAdapter
                addOnItemTouchListener(moveListener)
            }
        }
    }

    private val moveListener: OnItemTouchListener = object : OnItemTouchListener {
        override fun onInterceptTouchEvent(rv: RecyclerView, e: MotionEvent): Boolean {
            when (e.action) {
                MotionEvent.ACTION_MOVE -> rv.parent.requestDisallowInterceptTouchEvent(true)
            }
            return false
        }

        override fun onTouchEvent(rv: RecyclerView, e: MotionEvent) {}
        override fun onRequestDisallowInterceptTouchEvent(disallowIntercept: Boolean) {}
    }
}