package com.intive.tmdbandroid.home.ui.adapters

import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.intive.tmdbandroid.R
import com.intive.tmdbandroid.databinding.ItemScreeningBinding
import com.intive.tmdbandroid.model.TVShow

class TVShowAdapter (var tvShows: ArrayList<TVShow>) : RecyclerView.Adapter<TVShowAdapter.TVShowHolder>() {
    fun refresh (TVShowList: List<TVShow>) {
        tvShows.clear()
        tvShows.addAll(TVShowList)
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TVShowAdapter.TVShowHolder = TVShowHolder(
        ItemScreeningBinding.inflate(LayoutInflater.from(parent.context), parent, false)
    )

    override fun onBindViewHolder(holder: TVShowHolder, position: Int) {
        with(holder) {
            with(tvShows[position]) {
                val options = RequestOptions()
                    .override(300, 450)
                    .centerCrop()
                    .placeholder(R.drawable.ic_image)
                    .error(R.drawable.ic_image)

                val posterURL = binding.root.resources.getString(R.string.base_imageURL) + poster_path

                Glide.with(binding.root)
                    .load(posterURL)
                    .apply(options)
                    .into(binding.screeningPoster)

                binding.screeningPopularity.text = binding.root.resources.getString(R.string.popularity, (vote_average * 10).toInt())
                binding.screeningTitle.text = original_name
                binding.screeningDate.text = first_air_date
            }
        }
    }

    override fun getItemCount(): Int = tvShows.size

    inner class TVShowHolder (val binding: ItemScreeningBinding) : RecyclerView.ViewHolder(binding.root)
}