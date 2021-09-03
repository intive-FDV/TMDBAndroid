package com.intive.tmdbandroid.details.ui

import android.content.res.ColorStateList
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.setupWithNavController
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.google.android.material.appbar.AppBarLayout
import com.intive.tmdbandroid.R
import com.intive.tmdbandroid.databinding.FragmentDetailBinding
import com.intive.tmdbandroid.details.viewmodel.DetailsViewModel
import com.intive.tmdbandroid.details.viewmodel.State
import com.intive.tmdbandroid.model.TVShow
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collect
import java.text.SimpleDateFormat
import java.util.*

@AndroidEntryPoint
class DetailFragment : Fragment() {
    private var tvShowId: Int? = null
    private var tvShowName: String? = null

    private val viewModel: DetailsViewModel by viewModels()

    private lateinit var binding: FragmentDetailBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            tvShowId = it.getInt("id")
            tvShowName = it.getString("screeningTitle")
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentDetailBinding.inflate(inflater, container, false)
        collectDataFromViewModel()
        return binding.root
    }

    private fun collectDataFromViewModel() {
        lifecycleScope.launchWhenCreated {
            viewModel.uiState.collect {
                when(it){
                    is State.Success -> {
                        binding.layoutErrorDetail.errorContainer.visibility = View.GONE
                        binding.layoutLoadingDetail.progressBar.visibility = View.GONE
                        setupUI(it.data)
                    }
                    is State.Error -> {
                        binding.layoutLoadingDetail.progressBar.visibility = View.GONE
                        binding.layoutErrorDetail.errorContainer.visibility = View.VISIBLE
                    }
                    is State.Loading -> {
                        binding.layoutErrorDetail.errorContainer.visibility = View.GONE
                        binding.layoutLoadingDetail.progressBar.visibility = View.VISIBLE
                    }
                }
            }
        }
    }

    private fun setupUI(tvShow: TVShow) {

        setImages(tvShow)

        setDate(tvShow.first_air_date!!)

        setPercentageToCircularPercentage(tvShow.vote_average)

        binding.statusDetailTextView.text = tvShow.status

        val genresListText = tvShow.genres.map {
            it.name
        }.toString()
        val genresTextWithoutCharacters = genresListText.subSequence(1,genresListText.lastIndex)
        binding.genresDetailTextView.text = genresTextWithoutCharacters

        binding.numberOfSeasonsDetailTextView.text = String.format("%s %s",tvShow.number_of_seasons?.toString(),resources.getString(R.string.seasons))
        binding.numberOfEpisodesDetailTextView.text = String.format("%s %s",tvShow.number_of_episodes?.toString(),resources.getString(R.string.episodes))

        binding.overviewDetailTextView.text = tvShow.overview
    }

    private fun setPercentageToCircularPercentage(voteAverage: Double) {
        val percentage = (voteAverage * 10).toInt()

        binding.circularPercentage.progress = percentage

        val context = binding.root.context

        when {
            percentage < 25 -> binding.circularPercentage.progressTintList = ColorStateList.valueOf(
                ContextCompat.getColor(context, R.color.red)
            )
            percentage < 45 -> binding.circularPercentage.progressTintList = ColorStateList.valueOf(
                ContextCompat.getColor(context, R.color.orange)
            )
            percentage < 75 -> binding.circularPercentage.progressTintList = ColorStateList.valueOf(
                ContextCompat.getColor(context, R.color.yellow)
            )
            else -> binding.circularPercentage.progressTintList = ColorStateList.valueOf(
                ContextCompat.getColor(context, R.color.green)
            )
        }
        binding.screeningPopularity.text = resources.getString(R.string.popularity, percentage)
    }

    private fun setDate(firstAirDate: String) {
        try {
            val date =
                SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()).parse(firstAirDate)
            val stringDate = SimpleDateFormat("MMM dd, yyyy", Locale.getDefault()).format(date!!)
            binding.firstAirDateDetailTextView.text = stringDate
        } catch (e: Exception) {
            binding.firstAirDateDetailTextView.text = ""
        }
    }

    private fun setImages(tvShow: TVShow) {
        val options = RequestOptions()
            .placeholder(R.drawable.ic_image)
            .error(R.drawable.ic_image)

        val posterURL = resources.getString(R.string.base_imageURL) + tvShow.poster_path
        val backdropPosterURL = resources.getString(R.string.base_imageURL) + tvShow.backdrop_path

        Glide.with(this)
            .load(posterURL)
            .apply(options)
            .into(binding.imageDetailImageView)

        Glide.with(this)
            .load(backdropPosterURL)
            .apply(options)
            .into(binding.backgroundImageToolbarLayout)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupToolbar()
        tvShowId?.let {
            viewModel.tVShows(it)
        }
    }

    private fun setupToolbar() {
        val navController = findNavController()
        val appBarConfiguration = AppBarConfiguration(navController.graph)
        val toolbar = binding.toolbar
        binding.collapsingToolbarLayout.setupWithNavController(
            toolbar,
            navController,
            appBarConfiguration
        )
        toolbar.title = tvShowName
        binding.appBarLayoutDetail.addOnOffsetChangedListener(AppBarLayout.OnOffsetChangedListener { _, verticalOffset ->
            if (verticalOffset < -500) {
                binding.popularityCard.visibility = View.INVISIBLE
            } else binding.popularityCard.visibility = View.VISIBLE
        })
    }
}