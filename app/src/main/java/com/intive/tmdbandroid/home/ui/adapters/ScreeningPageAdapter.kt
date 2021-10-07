package com.intive.tmdbandroid.home.ui.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.paging.PagingDataAdapter
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import androidx.swiperefreshlayout.widget.CircularProgressDrawable
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.intive.tmdbandroid.R
import com.intive.tmdbandroid.databinding.ItemScreenengSmallBinding
import com.intive.tmdbandroid.model.Screening
import com.intive.tmdbandroid.model.TVShow

class ScreeningPageAdapter(private val clickListener: (Screening) -> Unit) : PagingDataAdapter<Screening, ScreeningPageAdapter.ScreeningHolder>(COMPARATOR) {
    companion object {
        private val COMPARATOR = object : DiffUtil.ItemCallback<Screening>() {
            override fun areItemsTheSame(oldItem: Screening, newItem: Screening): Boolean = (oldItem == newItem)
            override fun areContentsTheSame(oldItem: Screening, newItem: Screening): Boolean = (oldItem == newItem)
        }
    }

    override fun onBindViewHolder(holder: ScreeningHolder, position: Int) {
        getItem(position)?.let { holder.bind(it) }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ScreeningHolder = ScreeningHolder(
        ItemScreenengSmallBinding.inflate(LayoutInflater.from(parent.context), parent, false),
        clickListener
    )

    class ScreeningHolder(binding: ItemScreenengSmallBinding, private val clickListener: (Screening) -> Unit) : RecyclerView.ViewHolder(binding.root)  {
        private val title = binding.itemTitle
        private val backdrop = binding.itemBackdrop
        private val date = binding.itemDate
        private val popularity = binding.itemPopularity
        private val progress = binding.circularPercentage

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

            try {
                //val dateFormat = item.first_air_date?.let { SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()).parse(it) }
                date.text = item.release_date?.let { context.resources.getString(R.string.year, it.substring(0..3)) }
            } catch (e: Exception) {
                date.text = ""
            }

            val percentage = (item.vote_average * 10).toInt()
            progress.progress = percentage

            when {
                percentage < 25 -> progress.progressTintList = ContextCompat.getColorStateList(context, R.color.red)
                percentage < 45 -> progress.progressTintList = ContextCompat.getColorStateList(context, R.color.orange)
                percentage < 75 -> progress.progressTintList = ContextCompat.getColorStateList(context, R.color.yellow)
                else -> progress.progressTintList = ContextCompat.getColorStateList(context, R.color.green)
            }

            popularity.text = context.resources.getString(R.string.popularity, percentage)
        }
    }
}