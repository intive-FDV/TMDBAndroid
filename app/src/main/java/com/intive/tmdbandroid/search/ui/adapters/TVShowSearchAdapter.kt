package com.intive.tmdbandroid.search.ui.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.paging.PagingDataAdapter
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.intive.tmdbandroid.R
import com.intive.tmdbandroid.databinding.ItemFoundSearchBinding
import com.intive.tmdbandroid.model.TVShow
import timber.log.Timber
import java.lang.Exception
import java.time.LocalDate
import java.time.format.DateTimeFormatter

class TVShowSearchAdapter() : PagingDataAdapter<TVShow, TVShowSearchAdapter.SearchResultHolder>(REPO_COMPARATOR) {
    companion object {
    private val REPO_COMPARATOR = object : DiffUtil.ItemCallback<TVShow>() {
        override fun areItemsTheSame(oldItem: TVShow, newItem: TVShow): Boolean = (oldItem == newItem)
        override fun areContentsTheSame(oldItem: TVShow, newItem: TVShow): Boolean = (oldItem == newItem)
    }
}
    var query: String = ""

    var clickListener: ((TVShow) -> Unit)? = null

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TVShowSearchAdapter.SearchResultHolder {
        val resultHolder = ItemFoundSearchBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return SearchResultHolder(resultHolder)

    }

    override fun onBindViewHolder(holder: TVShowSearchAdapter.SearchResultHolder, position: Int) {
        val tvShowItem = getItem(position) as TVShow
        holder.bind(tvShowItem)
    }

    inner class SearchResultHolder (val binding: ItemFoundSearchBinding) : RecyclerView.ViewHolder(binding.root){

        private val itemTitle = binding.itemTitleSearch
        private val itemYear = binding.itemYearSearch
        private val itemSeasons = binding.itemSeasonsSearch
        private val itemRating = binding.itemRatingSearch

        fun bind(item: TVShow){

            itemView.setOnClickListener {
                clickListener?.invoke(item)
            }

            try {
                if(!item.first_air_date.isNullOrBlank()){
                    val formatter: DateTimeFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd")
                    val dateStr = item.first_air_date
                    val date: LocalDate = LocalDate.parse(dateStr, formatter)
                    itemYear.text = date.year.toString()
                }
            } catch (e : Exception){
                Timber.e(e)
            }

            itemTitle.text = item.name
            itemSeasons.text = item.number_of_seasons?.let {
                binding.root.resources.getQuantityString(
                    R.plurals.numberOfSeasons,
                    it,
                    it
                )
            }
            itemRating.rating = item.vote_average.toFloat()/2

            val options = RequestOptions()
                .centerCrop()
                .placeholder(R.drawable.ic_image)
                .error(R.drawable.ic_image)

            val posterURL = binding.root.resources.getString(R.string.base_imageURL) + item.poster_path

            Glide.with(binding.root.context)
                .load(posterURL)
                .apply(options)
                .into(binding.itemPosterSearch)


        }
    }

}
