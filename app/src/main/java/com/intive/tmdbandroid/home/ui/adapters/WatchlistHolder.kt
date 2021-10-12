package com.intive.tmdbandroid.home.ui.adapters

import androidx.recyclerview.widget.RecyclerView
import androidx.swiperefreshlayout.widget.CircularProgressDrawable
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.intive.tmdbandroid.R
import com.intive.tmdbandroid.databinding.ItemScreeningWatchlistBinding
import com.intive.tmdbandroid.model.Screening

class WatchlistHolder(binding: ItemScreeningWatchlistBinding, private val clickListener: ((Screening) -> Unit)) : RecyclerView.ViewHolder(binding.root)  {
    private val title = binding.titleItemWatchlist
    private val backdrop = binding.backdropItemWatchlist

    private val context = binding.root.context
    private val imgUrl = binding.root.resources.getString(R.string.base_imageURL)

    fun bind (item: Screening) {
        itemView.setOnClickListener {
            clickListener.invoke(item)
        }

        val circularProgressDrawable = CircularProgressDrawable(itemView.context).apply {
            strokeWidth = 5f
            centerRadius = 25f
        }
        circularProgressDrawable.start()

        val options = RequestOptions()
            .centerCrop()
            .placeholder(circularProgressDrawable)
            .error(R.drawable.ic_image)

        val backdropURL = imgUrl + item.backdrop_path

        Glide.with(context)
            .load(backdropURL)
            .apply(options)
            .into(backdrop)

        title.text = item.name

    }
}