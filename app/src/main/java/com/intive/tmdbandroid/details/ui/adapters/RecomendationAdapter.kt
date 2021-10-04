package com.intive.tmdbandroid.details.ui.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.intive.tmdbandroid.R
import com.intive.tmdbandroid.databinding.ItemNetworkImageBinding
import com.intive.tmdbandroid.databinding.ItemRecomendationBinding
import com.intive.tmdbandroid.model.Network
import com.intive.tmdbandroid.model.Screening

class RecomendationAdapter : ListAdapter<Screening, RecomendationAdapter.ScreeningHolder>(COMPARATOR) {
    companion object {
        private val COMPARATOR = object : DiffUtil.ItemCallback<Screening>() {
            override fun areItemsTheSame(oldItem: Screening, newItem: Screening): Boolean = (oldItem == newItem)
            override fun areContentsTheSame(oldItem: Screening, newItem: Screening): Boolean = (oldItem == newItem)
        }
    }

    override fun onBindViewHolder(holder: ScreeningHolder, position: Int) {
        holder.bind(getItem(position))
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ScreeningHolder = ScreeningHolder(
        ItemRecomendationBinding.inflate(LayoutInflater.from(parent.context), parent, false)
    )

    class ScreeningHolder(binding: ItemRecomendationBinding ) : RecyclerView.ViewHolder(binding.root)  {
        private val image = binding.recomendationImage
        private val title = binding.recomendationTitle

        private val context = binding.root.context
        private val imgUrl = binding.root.resources.getString(R.string.base_imageURL)

        fun bind (screening: Screening) {

            title.text = screening.name

            val options = RequestOptions()
                .placeholder(R.drawable.ic_image)
                .error(R.drawable.ic_image)

            val backdropURL = imgUrl + screening.backdrop_path

            Glide.with(context)
                .load(backdropURL)
                .apply(options)
                .into(image)
        }
    }
}
