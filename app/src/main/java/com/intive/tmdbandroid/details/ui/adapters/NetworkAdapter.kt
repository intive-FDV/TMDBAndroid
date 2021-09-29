package com.intive.tmdbandroid.details.ui.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.intive.tmdbandroid.R
import com.intive.tmdbandroid.databinding.ItemNetworkImageBinding
import com.intive.tmdbandroid.model.Network

class NetworkAdapter : ListAdapter<Network, NetworkAdapter.NetworkHolder>(COMPARATOR) {
    companion object {
        private val COMPARATOR = object : DiffUtil.ItemCallback<Network>() {
            override fun areItemsTheSame(oldItem: Network, newItem: Network): Boolean = (oldItem == newItem)
            override fun areContentsTheSame(oldItem: Network, newItem: Network): Boolean = (oldItem == newItem)
        }
    }

    override fun onBindViewHolder(holder: NetworkHolder, position: Int) {
        holder.bind(getItem(position))
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): NetworkHolder = NetworkHolder(
        ItemNetworkImageBinding.inflate(LayoutInflater.from(parent.context), parent, false)
    )

    class NetworkHolder(binding: ItemNetworkImageBinding ) : RecyclerView.ViewHolder(binding.root)  {
        private val logo = binding.networkLogo

        private val context = binding.root.context
        private val imgUrl = binding.root.resources.getString(R.string.base_imageURL)

        fun bind (network: Network) {

            val options = RequestOptions()
                .placeholder(R.drawable.ic_image)
                .error(R.drawable.ic_image)

            val backdropURL = imgUrl + network.logo_path

            Glide.with(context)
                .load(backdropURL)
                .apply(options)
                .into(logo)
        }
    }
}
