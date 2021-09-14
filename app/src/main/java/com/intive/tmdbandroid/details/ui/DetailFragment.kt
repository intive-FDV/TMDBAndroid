package com.intive.tmdbandroid.details.ui

import android.content.res.ColorStateList
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.content.res.AppCompatResources
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.setupWithNavController
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.google.android.material.appbar.AppBarLayout
import com.intive.tmdbandroid.R
import com.intive.tmdbandroid.common.State
import com.intive.tmdbandroid.databinding.FragmentDetailBinding
import com.intive.tmdbandroid.details.viewmodel.DetailsViewModel
import com.intive.tmdbandroid.model.TVShow
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.util.*

@AndroidEntryPoint
class DetailFragment : Fragment() {

    private var isSaveOnWatchlist: Boolean = false
    private var tvShowId: Int? = null

    private val viewModel: DetailsViewModel by viewModels()
    private val args: DetailFragmentArgs by navArgs()

    private lateinit var binding: FragmentDetailBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        tvShowId = args.screeningID
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View {
        binding = FragmentDetailBinding.inflate(inflater, container, false)
        collectTVShowDetailFromViewModel()
        collectWatchlistDataFromViewModel()
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        tvShowId?.let {
            viewModel.tVShows(it)
        }
    }

    private fun collectTVShowDetailFromViewModel() {
        binding.coordinatorContainerDetail.visibility = View.INVISIBLE
        lifecycleScope.launchWhenCreated {
            viewModel.uiState.collect { state ->
                when (state) {
                    is State.Success -> {
                        binding.layoutErrorDetail.errorContainer.visibility = View.GONE
                        binding.layoutLoadingDetail.progressBar.visibility = View.GONE
                        setupTVShowDetailUI(state.data)
                    }
                    is State.Error -> {
                        binding.layoutLoadingDetail.progressBar.visibility = View.GONE
                        binding.layoutErrorDetail.errorContainer.visibility = View.VISIBLE
                        binding.coordinatorContainerDetail.visibility = View.VISIBLE
                    }
                    is State.Loading -> {
                        binding.layoutErrorDetail.errorContainer.visibility = View.GONE
                        binding.layoutLoadingDetail.progressBar.visibility = View.VISIBLE
                    }
                }
            }
        }
    }

    private fun collectWatchlistDataFromViewModel() {
        lifecycleScope.launch {
            viewModel.watchlistUIState.collectLatest {
                when (it) {
                    is State.Success -> {
                        binding.layoutErrorDetail.errorContainer.visibility = View.GONE
                        binding.layoutLoadingDetail.progressBar.visibility = View.GONE
                        selectOrUnselectWatchlistFav(it.data)
                        isSaveOnWatchlist = it.data
                    }
                    State.Error -> {
                        binding.layoutLoadingDetail.progressBar.visibility = View.GONE
                        binding.layoutErrorDetail.errorContainer.visibility = View.VISIBLE
                        binding.coordinatorContainerDetail.visibility = View.VISIBLE
                    }
                    State.Loading -> {
                        binding.layoutErrorDetail.errorContainer.visibility = View.GONE
                        binding.layoutLoadingDetail.progressBar.visibility = View.VISIBLE
                    }
                }
            }
        }
    }

    private fun setupTVShowDetailUI(tvShow: TVShow) {

        setImages(tvShow)

        setDate(tvShow.first_air_date!!)

        setPercentageToCircularPercentage(tvShow.vote_average)

        setupToolbar(tvShow)

        binding.toolbar.title = tvShow.name

        binding.statusDetailTextView.text = tvShow.status

        val genresListText = tvShow.genres.map {
            it.name
        }.toString()
        val genresTextWithoutCharacters = genresListText.subSequence(1, genresListText.lastIndex)
        binding.genresDetailTextView.text = genresTextWithoutCharacters

        binding.numberOfSeasonsDetailTextView.text =
            tvShow.number_of_seasons?.let {
                resources.getQuantityString(
                    R.plurals.numberOfSeasons,
                    it,
                    it
                )
            }
        binding.numberOfEpisodesDetailTextView.text =
            tvShow.number_of_episodes?.let {
                resources.getQuantityString(
                    R.plurals.numberOfEpisodes,
                    it,
                    it
                )
            }

        binding.overviewDetailTextView.text = tvShow.overview
        binding.coordinatorContainerDetail.visibility = View.VISIBLE

        viewModel.existAsFavorite(tvShowId.toString())
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

    private fun setupToolbar(tvShow: TVShow) {
        val navController = findNavController()
        val appBarConfiguration = AppBarConfiguration(navController.graph)
        val toolbar = binding.toolbar
        toolbar.inflateMenu(R.menu.watchlist_favorite_detail_fragment)
        toolbar.setOnMenuItemClickListener {
            when (it.itemId) {
                R.id.ic_heart_watchlist -> {
                    if (!isSaveOnWatchlist) {
                        viewModel.addToWatchlist(tvShowId.toString(), tvShow.toTVShowORMEntity())
                    } else {
                        viewModel.deleteFromWatchlist(tvShowId.toString())
                    }
                    true
                }
                else -> false
            }
        }
        binding.collapsingToolbarLayout.setupWithNavController(
            toolbar,
            navController,
            appBarConfiguration
        )
        binding.appBarLayoutDetail.addOnOffsetChangedListener(AppBarLayout.OnOffsetChangedListener { _, verticalOffset ->
            if (verticalOffset < -500) {
                binding.popularityCard.visibility = View.INVISIBLE
            } else binding.popularityCard.visibility = View.VISIBLE
        })
    }

    private fun selectOrUnselectWatchlistFav(isFav: Boolean) {
        val watchlistItem = binding.toolbar.menu.findItem(R.id.ic_heart_watchlist)
        if (isFav) {
            watchlistItem.icon =
                AppCompatResources.getDrawable(requireContext(), R.drawable.ic_heart_selected)
        } else watchlistItem.icon =
            AppCompatResources.getDrawable(requireContext(), R.drawable.ic_heart_unselected)

    }

}