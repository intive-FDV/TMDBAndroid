package com.intive.tmdbandroid.home.ui.adapters

import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.intive.tmdbandroid.R
import com.intive.tmdbandroid.databinding.ItemScreeningBinding
import com.intive.tmdbandroid.model.Movie

class MovieAdapter (var movies: ArrayList<Movie>) : RecyclerView.Adapter<MovieAdapter.MovieHolder>() {
    fun refresh (moviesList: List<Movie>) {
        movies.clear()
        movies.addAll(moviesList)
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MovieHolder = MovieHolder(
        ItemScreeningBinding.inflate(LayoutInflater.from(parent.context), parent, false)
    )

    override fun onBindViewHolder(holder: MovieHolder, position: Int) {
        with(holder) {
            with(movies[position]) {
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

                binding.screeningPopularity.text = binding.root.resources.getString(R.string.popularity, popularity.toInt())
                binding.screeningTitle.text = original_title
                binding.screeningDate.text = release_date
            }
        }
    }

    override fun getItemCount(): Int = movies.size

    inner class MovieHolder (val binding: ItemScreeningBinding) : RecyclerView.ViewHolder(binding.root)
}