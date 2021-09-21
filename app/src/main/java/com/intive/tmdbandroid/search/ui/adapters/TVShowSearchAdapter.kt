package com.intive.tmdbandroid.search.ui.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.paging.AsyncPagingDataDiffer
import androidx.paging.PagingData
import androidx.recyclerview.widget.AdapterListUpdateCallback
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListUpdateCallback
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.intive.tmdbandroid.R
import com.intive.tmdbandroid.databinding.HeaderResultsSearchBinding
import com.intive.tmdbandroid.databinding.ItemFoundSearchBinding
import com.intive.tmdbandroid.model.TVShow
import timber.log.Timber
import java.lang.Exception
import java.time.LocalDate
import java.time.format.DateTimeFormatter

class TVShowSearchAdapter(private val clickListener: ((TVShow) -> Unit)) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {
    var query: String = ""

    val adapterCallback = AdapterListUpdateCallback(this)

    companion object {
        private const val HEADER = 0
        private const val ITEM = 1
    }

    val differ = AsyncPagingDataDiffer(
            TVShowAsyncPagingDataDiffCallback(),
            object : ListUpdateCallback {
                override fun onInserted(position: Int, count: Int) {
                    adapterCallback.onInserted(position + 1, count)
                }

                override fun onRemoved(position: Int, count: Int) {
                    adapterCallback.onRemoved(position + 1, count)
                }

                override fun onMoved(fromPosition: Int, toPosition: Int) {
                    adapterCallback.onMoved(fromPosition + 1, toPosition + 1)
                }

                override fun onChanged(position: Int, count: Int, payload: Any?) {
                    adapterCallback.onChanged(position + 1, count, payload)
                }

            }
    )

    suspend fun submitData(tvShowPagingData: PagingData<TVShow>) {
        differ.submitData(tvShowPagingData)
    }

    override fun getItemCount(): Int {
        return differ.itemCount + 1
    }

    override fun getItemViewType(position: Int): Int {
        return when (position) {
            0 -> HEADER
            else -> ITEM
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return when (viewType) {
            HEADER -> HeaderHolder(HeaderResultsSearchBinding.inflate(LayoutInflater.from(parent.context), parent, false))
            ITEM -> SearchResultHolder(ItemFoundSearchBinding.inflate(LayoutInflater.from(parent.context), parent, false), clickListener)
            else -> throw Exception("Illegal ViewType")
        }
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        when (holder) {
            is HeaderHolder -> holder.bind(query)
            is SearchResultHolder -> differ.getItem(position - 1)?.let { holder.bind(it) }
        }
    }

    inner class HeaderHolder (private val binding: HeaderResultsSearchBinding): RecyclerView.ViewHolder(binding.root){
        fun bind(query: String){
            binding.searchHeader.text = binding.root.context.getString(R.string.search_result_header, query)
        }
    }

    inner class SearchResultHolder (private val binding: ItemFoundSearchBinding, private val clickListener: ((TVShow) -> Unit)) : RecyclerView.ViewHolder(binding.root){

        private val itemTitle = binding.itemTitleSearch
        private val itemYear = binding.itemYearSearch
        private val itemSeasons = binding.itemSeasonsSearch
        private val itemRating = binding.itemRatingSearch

        fun bind(item: TVShow){

            itemView.setOnClickListener {
                clickListener.invoke(item)
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

    private class TVShowAsyncPagingDataDiffCallback : DiffUtil.ItemCallback<TVShow>() {
        override fun areItemsTheSame(oldItem: TVShow, newItem: TVShow): Boolean {
            return oldItem.id == newItem.id
        }

        override fun areContentsTheSame(oldItem: TVShow, newItem: TVShow): Boolean {
            return oldItem == newItem
        }
    }

}
