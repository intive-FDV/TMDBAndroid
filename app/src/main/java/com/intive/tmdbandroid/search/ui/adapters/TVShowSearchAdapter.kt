package com.intive.tmdbandroid.search.ui.adapters

import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.paging.PagingData
import androidx.paging.PagingDataAdapter
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.intive.tmdbandroid.R
import com.intive.tmdbandroid.databinding.ItemFoundSearchBinding
import com.intive.tmdbandroid.databinding.SearchResultsHeaderBinding
import com.intive.tmdbandroid.model.TVShow
import timber.log.Timber
import java.lang.Exception
import java.text.SimpleDateFormat
import java.util.*

class TVShowSearchAdapter : PagingDataAdapter<TVShow, RecyclerView.ViewHolder>(REPO_COMPARATOR) {
    companion object {
    private val REPO_COMPARATOR = object : DiffUtil.ItemCallback<TVShow>() {
        override fun areItemsTheSame(oldItem: TVShow, newItem: TVShow): Boolean = (oldItem == newItem)
        override fun areContentsTheSame(oldItem: TVShow, newItem: TVShow): Boolean = (oldItem == newItem)
    }
}
    private val TYPE_HEADER : Int = 0
    private val TYPE_LIST : Int = 1

    var clickListener: ((TVShow) -> Unit)? = null

    override fun getItemViewType(position: Int): Int {
        return when(position){
            0 -> TYPE_HEADER
            else -> TYPE_LIST
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return when(viewType){
            TYPE_HEADER -> {
                val header = SearchResultsHeaderBinding.inflate(LayoutInflater.from(parent.context), parent, false)
                val holder = SearchResultsHeaderHolder(header)
                holder.headerText.text = "Results"
                return holder
            } else -> {
                val header = ItemFoundSearchBinding.inflate(LayoutInflater.from(parent.context), parent, false)
                SearchResultHolder(header)
            }
        }

    }

    override fun getItemCount(): Int {
        val count = super.getItemCount() + 1
        println(count)
        return count
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        when(holder){
            is SearchResultsHeaderHolder -> holder.bind()
            is SearchResultHolder -> {
                val tvShowItem = getItem(position - 1) as TVShow
                holder.bind(tvShowItem)
            }
        }
    }

    inner class SearchResultHolder (val binding: ItemFoundSearchBinding) : RecyclerView.ViewHolder(binding.root){

        private val itemTitle = binding.itemTitleSearch
        private val itemYear = binding.itemYearSearch
        private val itemSeasons = binding.itemSeasonsSearch
        private val itemRating = binding.itemRatingSearch

        init {
            itemView.setOnClickListener {
                getItem(absoluteAdapterPosition - 1)?.let { it1 -> clickListener?.invoke(it1) }
            }
        }

        fun bind(item: TVShow){
            try {
                if(item.first_air_date.toString().isNotEmpty()){
                    val date = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()).parse(item.first_air_date)
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

    inner class SearchResultsHeaderHolder(binding: SearchResultsHeaderBinding) : RecyclerView.ViewHolder(binding.root)
    {
        val headerText = binding.searchHeader

        fun bind(){
            headerText.text = "Results for:"
        }
    }

}
