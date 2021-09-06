package com.intive.tmdbandroid.search.ui.adapters

import android.content.res.ColorStateList
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.core.content.ContextCompat
import androidx.core.os.bundleOf
import androidx.navigation.findNavController
import androidx.paging.PagingDataAdapter
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.intive.tmdbandroid.R
import com.intive.tmdbandroid.databinding.ItemFoundSearchBinding
import com.intive.tmdbandroid.databinding.ItemScreeningBinding
import com.intive.tmdbandroid.home.ui.adapters.TVShowPageAdapter
import com.intive.tmdbandroid.model.TVShow
import java.text.SimpleDateFormat
import java.util.*

private const val TYPE_HEADER : Int = 0
private const val TYPE_LIST : Int = 1


class TVShowSearchAdapter(private val searchResults : PagingDataAdapter<TVShow, TVShowPageAdapter.TVShowHolder>) : RecyclerView.Adapter<RecyclerView.ViewHolder>()
{
    private val TYPE_HEADER : Int = 0
    private val TYPE_LIST : Int = 1

    override fun getItemViewType(position: Int): Int {
        return when(position){
            0 -> TYPE_HEADER
            else -> TYPE_LIST
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return when(viewType){
            TYPE_HEADER -> {
                val header = LayoutInflater.from(parent.context).inflate(R.layout.search_results_header,parent,false)
                SearchResultsHeaderHolder(header)
            } else -> {
                val header = ItemFoundSearchBinding.inflate(LayoutInflater.from(parent.context), parent, false)
                SearchResultHolder(header)
            }
        }

    }

    override fun getItemCount(): Int {
        return searchResults.itemCount + 1
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        if(holder is SearchResultsHeaderHolder)
        {
            holder.headerText.text = "Results:"
        }
    }

    inner class SearchResultHolder (val binding: ItemFoundSearchBinding) : RecyclerView.ViewHolder(binding.root)

    class SearchResultsHeaderHolder(itemView : View) : RecyclerView.ViewHolder(itemView)
    {
        val headerText = R.layout.search_results_header as TextView
    }

}
