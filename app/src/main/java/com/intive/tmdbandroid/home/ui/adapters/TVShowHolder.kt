package com.intive.tmdbandroid.home.ui.adapters

import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.intive.tmdbandroid.R
import com.intive.tmdbandroid.databinding.ItemScreeningBinding
import com.intive.tmdbandroid.model.TVShow
import timber.log.Timber
import java.text.SimpleDateFormat
import java.util.*

class TVShowHolder(binding: ItemScreeningBinding) : RecyclerView.ViewHolder(binding.root) {
    private val poster = binding.screeningPoster
    private val popularity = binding.screeningPopularity
    private val average = binding.circularPercentage
    private val title = binding.screeningTitle
    private val date = binding.screeningDate

    private val context = binding.root.context
    private val imgUrl = binding.root.resources.getString(R.string.base_imageURL)

    fun bind (item: TVShow) {
        Timber.i("MAS - TVShowHolder.bind();")


        val options = RequestOptions()
            .centerCrop()
            .placeholder(R.drawable.ic_image)
            .error(R.drawable.ic_image)

        val posterURL = imgUrl + item.poster_path

        Glide.with(context)
            .load(posterURL)
            .apply(options)
            .into(poster)

        val percentage = (item.vote_average * 10).toInt()
        average.progress = percentage

        when {
            percentage < 25 -> average.progressTintList = ContextCompat.getColorStateList(context, R.color.red)
            percentage < 45 -> average.progressTintList = ContextCompat.getColorStateList(context, R.color.orange)
            percentage < 75 -> average.progressTintList = ContextCompat.getColorStateList(context, R.color.yellow)
            else -> average.progressTintList = ContextCompat.getColorStateList(context, R.color.green)
        }

        popularity.text = context.resources.getString(R.string.popularity, percentage)

        title.text = item.name

        try {
            val dateFormat = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()).parse(item.first_air_date)
            val stringDate = SimpleDateFormat("MMM dd, yyyy", Locale.getDefault()).format(dateFormat)
            date.text = stringDate
        } catch (e: Exception) {
            date.text = ""
        }
    }
}