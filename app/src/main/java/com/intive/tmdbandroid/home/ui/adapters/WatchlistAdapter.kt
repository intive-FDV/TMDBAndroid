package com.intive.tmdbandroid.home.ui.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.intive.tmdbandroid.R
import com.intive.tmdbandroid.databinding.ItemScreeningWatchlistBinding
import com.intive.tmdbandroid.model.Screening

class WatchlistAdapter(private val clickListener: ((Screening) -> Unit)) : ListAdapter<Screening, WatchlistAdapter.WatchlistHolder>(COMPARATOR) {
    companion object {
        private val COMPARATOR = object : DiffUtil.ItemCallback<Screening>() {
            override fun areItemsTheSame(oldItem: Screening, newItem: Screening): Boolean = (oldItem == newItem)
            override fun areContentsTheSame(oldItem: Screening, newItem: Screening): Boolean = (oldItem == newItem)
        }
    }

    override fun onBindViewHolder(holder: WatchlistHolder, position: Int) {
        holder.bind(getItem(position))
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): WatchlistHolder = WatchlistHolder(
        ItemScreeningWatchlistBinding.inflate(LayoutInflater.from(parent.context), parent, false),
        clickListener
    )

    class WatchlistHolder(binding: ItemScreeningWatchlistBinding, private val clickListener: (Screening) -> Unit) : RecyclerView.ViewHolder(binding.root)  {
        private val title = binding.titleItemWatchlist
        private val backdrop = binding.backdropItemWatchlist

        private val context = binding.root.context
        private val imgUrl = binding.root.resources.getString(R.string.base_imageURL)

        fun bind (item: Screening) {
            itemView.setOnClickListener {
                clickListener.invoke(item)
            }

            val options = RequestOptions()
                .centerCrop()
                .placeholder(R.drawable.ic_image)
                .error(R.drawable.ic_image)

            val backdropURL = imgUrl + item.backdrop_path

            Glide.with(context)
                .load(backdropURL)
                .apply(options)
                .into(backdrop)

            title.text = item.name
        }
    }
}
