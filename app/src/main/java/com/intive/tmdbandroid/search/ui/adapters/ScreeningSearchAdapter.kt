package com.intive.tmdbandroid.search.ui.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.paging.AsyncPagingDataDiffer
import androidx.paging.PagingData
import androidx.recyclerview.widget.AdapterListUpdateCallback
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListUpdateCallback
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
) : RecyclerView.Adapter<ScreeningSearchAdapter.SearchResultHolder>() {

    val adapterCallback = AdapterListUpdateCallback(this)

    val differ = AsyncPagingDataDiffer(
        ScreeningAsyncPagingDataDiffCallback(),
        object : ListUpdateCallback {
            override fun onInserted(position: Int, count: Int) {
                adapterCallback.onInserted(position, count)
            }

            override fun onRemoved(position: Int, count: Int) {
                adapterCallback.onRemoved(position, count)
            }

            override fun onMoved(fromPosition: Int, toPosition: Int) {
                adapterCallback.onMoved(fromPosition, toPosition)
            }

            override fun onChanged(position: Int, count: Int, payload: Any?) {
                adapterCallback.onChanged(position, count, payload)
            }

        }
    )

    suspend fun submitData(screening: PagingData<Screening>) {
        differ.submitData(screening)
    }

    override fun getItemCount(): Int {
        return differ.itemCount
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SearchResultHolder {
        return SearchResultHolder(
            ItemFoundSearchBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            ), clickListener
        )
    }

    override fun onBindViewHolder(holder: SearchResultHolder, position: Int) {
        differ.getItem(position)?.let { holder.bind(it) }
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

    private class ScreeningAsyncPagingDataDiffCallback : DiffUtil.ItemCallback<Screening>() {
        override fun areItemsTheSame(
            oldItem: Screening,
            newItem: Screening
        ): Boolean {
            return oldItem.id == newItem.id
        }

        override fun areContentsTheSame(
            oldItem: Screening,
            newItem: Screening
        ): Boolean {
            return oldItem == newItem
        }
    }

}
