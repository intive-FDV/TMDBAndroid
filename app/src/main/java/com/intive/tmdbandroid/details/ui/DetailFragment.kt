package com.intive.tmdbandroid.details.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.google.android.material.appbar.AppBarLayout
import com.intive.tmdbandroid.databinding.FragmentDetailBinding
import com.intive.tmdbandroid.home.ui.HomeActivity

class DetailFragment : Fragment() {
    private var tvShowId: Int? = null
    private lateinit var binding: FragmentDetailBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            tvShowId = it.getInt("id")
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentDetailBinding.inflate(inflater, container, false)
        with(activity as HomeActivity) {
            setSupportActionBar(binding.toolbar)
            with(supportActionBar) {
                this?.setDisplayHomeAsUpEnabled(true)
            }
        }
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.appBarLayoutDetail.addOnOffsetChangedListener(AppBarLayout.OnOffsetChangedListener { _, verticalOffset ->
            if (verticalOffset < -500) {
                binding.popularityCard.visibility = View.INVISIBLE
            } else binding.popularityCard.visibility = View.VISIBLE
        })
    }
}