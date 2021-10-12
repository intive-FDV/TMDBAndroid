package com.intive.tmdbandroid.search.ui.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.paging.PagingDataAdapter
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import androidx.swiperefreshlayout.widget.CircularProgressDrawable
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.intive.tmdbandroid.R
import com.intive.tmdbandroid.databinding.ItemFoundSearchBinding
import com.intive.tmdbandroid.model.Screening
import timber.log.Timber
import java.text.SimpleDateFormat
import java.util.*

class ScreeningSearchAdapter(
    private val clickListener: ((Screening) -> Unit)
) : PagingDataAdapter<Screening, ScreeningSearchAdapter.SearchResultHolder>(COMPARATOR) {
    companion object {
        private val COMPARATOR = object : DiffUtil.ItemCallback<Screening>() {
            override fun areItemsTheSame(oldItem: Screening, newItem: Screening): Boolean = (oldItem == newItem)
            override fun areContentsTheSame(oldItem: Screening, newItem: Screening): Boolean = (oldItem == newItem)
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ScreeningSearchAdapter.SearchResultHolder {
        return SearchResultHolder(
            ItemFoundSearchBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            ), clickListener
        )
    }

    override fun onBindViewHolder(holder: SearchResultHolder, position: Int) {
        getItem(position)?.let { holder.bind(it) }
    }

    inner class SearchResultHolder(
        private val binding: ItemFoundSearchBinding,
        private val clickListener: ((Screening) -> Unit)
    ) : RecyclerView.ViewHolder(binding.root) {

        private val itemTitle = binding.itemTitleSearch
        private val itemYear = binding.itemYearSearch
        private val itemRating = binding.itemRatingSearch
        private val itemMediaType = binding.itemMediaTypeSearch

        fun bind(item: Screening) {
            itemView.setOnClickListener {
                clickListener.invoke(item)
            }

            try {
                if (!item.release_date.isNullOrBlank()) {
                    val formatter = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())
                    val dateStr = item.release_date
                    val date: Date? = formatter.parse(dateStr)
                    val formatter2 = SimpleDateFormat("yyyy", Locale.getDefault())
                    val dateStr2 = date?.let {
                        formatter2.format(it)
                    }
                    itemYear.text = dateStr2
                }
            } catch (e: Exception) {
                Timber.e(e)
            }

            itemTitle.text = item.name
            if (item.vote_average == 0.0) {
                itemRating.rating = item.popularity.toFloat()
            } else {
                itemRating.rating = item.vote_average.toFloat() / 2
            }
            itemMediaType.text = item.media_type.uppercase()

            val circularProgressDrawable = CircularProgressDrawable(itemView.context).apply {
                strokeWidth = 5f
                centerRadius = 25f
            }
            circularProgressDrawable.start()

            val options = RequestOptions()
                .centerCrop()
                .placeholder(circularProgressDrawable)
                .error(R.drawable.ic_image)

            val posterURL =
                binding.root.resources.getString(R.string.base_imageURL) + item.poster_path

            Glide.with(binding.root.context)
                .load(posterURL)
                .apply(options)
                .into(binding.itemPosterSearch)
        }
    }
}
