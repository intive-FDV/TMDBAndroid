package com.intive.tmdbandroid.home.ui.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import androidx.swiperefreshlayout.widget.CircularProgressDrawable
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.intive.tmdbandroid.R
import com.intive.tmdbandroid.databinding.ItemScreeningSmallBinding
import com.intive.tmdbandroid.databinding.ItemScreeningWatchlistBinding
import com.intive.tmdbandroid.model.Screening

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
        ItemScreeningSmallBinding.inflate(LayoutInflater.from(parent.context), parent, false),
        clickListener
    )

    class WatchlistHolder(binding: ItemScreeningSmallBinding, private val clickListener: (Screening) -> Unit) : RecyclerView.ViewHolder(binding.root)  {
        private val title = binding.itemTitle
        private val backdrop = binding.itemBackdrop
        private val popularity = binding.popularityCard
        private val releaseDate = binding.itemDate

        private val context = binding.root.context
        private val imgUrl = binding.root.resources.getString(R.string.base_imageURL)

        fun bind (item: Screening) {
            popularity.visibility = View.GONE
            releaseDate.visibility = View.GONE
            itemView.setOnClickListener {
                clickListener.invoke(item)
            }

            val circularProgressDrawable = CircularProgressDrawable(itemView.context).apply {
                strokeWidth = 5f
                centerRadius = 25f
                setColorSchemeColors(ContextCompat.getColor(context, R.color.material_on_background_emphasis_high_type))
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
}
