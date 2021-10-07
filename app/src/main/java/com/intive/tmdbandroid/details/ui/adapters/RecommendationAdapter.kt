package com.intive.tmdbandroid.details.ui.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import androidx.swiperefreshlayout.widget.CircularProgressDrawable
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.intive.tmdbandroid.R
import com.intive.tmdbandroid.databinding.ItemRecommendationBinding
import com.intive.tmdbandroid.model.Screening
import timber.log.Timber

class RecommendationAdapter(private val widthSize: Int, private val clickListener: ((Screening) -> Unit)) : ListAdapter<Screening, RecommendationAdapter.ScreeningHolder>(COMPARATOR) {
    companion object {
        private val COMPARATOR = object : DiffUtil.ItemCallback<Screening>() {
            override fun areItemsTheSame(oldItem: Screening, newItem: Screening): Boolean = (oldItem == newItem)
            override fun areContentsTheSame(oldItem: Screening, newItem: Screening): Boolean = (oldItem == newItem)
        }
    }

    override fun onBindViewHolder(holder: ScreeningHolder, position: Int) {
        holder.bind(getItem(position))
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ScreeningHolder {
        val binding =ItemRecommendationBinding.inflate(LayoutInflater.from(parent.context), parent, false)

        Timber.i("MAS - width: $widthSize")
        binding.containerWatchlistScreening.layoutParams.width = widthSize

        return ScreeningHolder(
            binding,
            clickListener
        )
    }

    class ScreeningHolder(binding: ItemRecommendationBinding, private val clickListener: (Screening) -> Unit) : RecyclerView.ViewHolder(binding.root)  {
        private val image = binding.recomendationImage
        private val title = binding.recomendationTitle

        private val context = binding.root.context
        private val imgUrl = binding.root.resources.getString(R.string.base_imageURL)

        fun bind (item: Screening) {
            itemView.setOnClickListener {
                clickListener.invoke(item)
            }

            title.text = item.name

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
                .into(image)
        }
    }
}
