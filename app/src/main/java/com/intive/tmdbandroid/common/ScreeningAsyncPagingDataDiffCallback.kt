package com.intive.tmdbandroid.common

import androidx.recyclerview.widget.DiffUtil
import com.intive.tmdbandroid.model.Screening

class ScreeningAsyncPagingDataDiffCallback : DiffUtil.ItemCallback<Screening>() {
    override fun areItemsTheSame(oldItem: Screening, newItem: Screening): Boolean {
        return oldItem.id == newItem.id
    }

    override fun areContentsTheSame(oldItem: Screening, newItem: Screening): Boolean {
        return oldItem == newItem
    }
}