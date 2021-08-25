package com.intive.tmdbandroid.home.ui.adapters

import android.content.res.ColorStateList
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.intive.tmdbandroid.R
import com.intive.tmdbandroid.databinding.ItemScreeningBinding
import com.intive.tmdbandroid.model.TVShow
import java.lang.IllegalArgumentException
import java.text.SimpleDateFormat
import java.util.*
import kotlin.collections.ArrayList

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
                    .override(150, 225)
                    .centerCrop()
                    .placeholder(R.drawable.ic_image)
                    .error(R.drawable.ic_image)

                val posterURL = binding.root.resources.getString(R.string.base_imageURL) + poster_path

                val context = binding.root.context

                Glide.with(context)
                    .load(posterURL)
                    .apply(options)
                    .into(binding.screeningPoster)

                val percentage = (vote_average * 10).toInt()

                try {
                    val date = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()).parse(first_air_date)
                    val stringDate = SimpleDateFormat("MMM dd, yyyy", Locale.getDefault()).format(date!!)
                    binding.screeningDate.text = stringDate
                } catch (e: IllegalArgumentException) {
                    binding.screeningDate.text = ""
                }

                binding.circularPercentage.progress = percentage

                when {
                    percentage < 25 -> binding.circularPercentage.progressTintList = ColorStateList.valueOf(
                        ContextCompat.getColor(context, R.color.red))
                    percentage < 45 -> binding.circularPercentage.progressTintList = ColorStateList.valueOf(
                        ContextCompat.getColor(context, R.color.orange))
                    percentage < 75 -> binding.circularPercentage.progressTintList = ColorStateList.valueOf(
                        ContextCompat.getColor(context, R.color.yellow))
                    else -> binding.circularPercentage.progressTintList = ColorStateList.valueOf(
                        ContextCompat.getColor(context, R.color.green))
                }

                binding.screeningPopularity.text = context.resources.getString(R.string.popularity, percentage)
                binding.screeningTitle.text = original_name
            }
        }
    }

    override fun getItemCount(): Int = tvShows.size

    inner class TVShowHolder (val binding: ItemScreeningBinding) : RecyclerView.ViewHolder(binding.root)
}