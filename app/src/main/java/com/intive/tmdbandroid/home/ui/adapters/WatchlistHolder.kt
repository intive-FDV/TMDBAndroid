package com.intive.tmdbandroid.home.ui.adapters

import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.intive.tmdbandroid.R
import com.intive.tmdbandroid.databinding.ItemScreeningWatchlistBinding
import com.intive.tmdbandroid.model.TVShow

class WatchlistHolder(binding: ItemScreeningWatchlistBinding, private val clickListener: ((TVShow) -> Unit)) : RecyclerView.ViewHolder(binding.root)  {
    private val title = binding.titleItemWatchlist
    private val backdrop = binding.backdropItemWatchlist

    private val context = binding.root.context
    private val imgUrl = binding.root.resources.getString(R.string.base_imageURL)

    fun bind (item: TVShow) {
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