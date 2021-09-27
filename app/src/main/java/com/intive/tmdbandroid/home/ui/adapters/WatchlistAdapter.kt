package com.intive.tmdbandroid.home.ui.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.intive.tmdbandroid.R
import com.intive.tmdbandroid.databinding.ItemScreenengSmallBinding
import com.intive.tmdbandroid.databinding.ItemScreeningWatchlistBinding
import com.intive.tmdbandroid.model.Screening
import timber.log.Timber

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
        ItemScreenengSmallBinding.inflate(LayoutInflater.from(parent.context), parent, false),
        clickListener
    )

    class WatchlistHolder(binding: ItemScreenengSmallBinding, private val clickListener: (Screening) -> Unit) : RecyclerView.ViewHolder(binding.root)  {
        private val title = binding.itemTitle
        private val backdrop = binding.itemBackdrop
        private val date = binding.itemDate
        private val popularity = binding.popularityCard

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

            try {
                //val dateFormat = item.first_air_date?.let { SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()).parse(it) }
                date.text = item.release_date?.let { context.resources.getString(R.string.year, it.substring(0..3)) }
            } catch (e: Exception) {
                date.text = ""
            }

            popularity.visibility = View.GONE
        }
    }
}
