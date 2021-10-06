package com.intive.tmdbandroid.details.ui

import android.app.Dialog
import android.content.Intent
import android.os.Bundle
import android.view.*
import android.widget.Toast
import androidx.annotation.NonNull
import androidx.appcompat.content.res.AppCompatResources
import androidx.core.content.ContextCompat
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.setupWithNavController
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.intive.tmdbandroid.R
import com.intive.tmdbandroid.common.State
import com.intive.tmdbandroid.databinding.FragmentDetailBinding
import com.intive.tmdbandroid.details.ui.adapters.NetworkAdapter
import com.intive.tmdbandroid.details.ui.adapters.RecommendationAdapter
import com.intive.tmdbandroid.details.viewmodel.DetailsViewModel
import com.intive.tmdbandroid.model.Screening
import com.pierfrancescosoffritti.androidyoutubeplayer.core.player.YouTubePlayer
import com.pierfrancescosoffritti.androidyoutubeplayer.core.player.listeners.AbstractYouTubePlayerListener
import com.pierfrancescosoffritti.androidyoutubeplayer.core.player.utils.loadOrCueVideo
import com.pierfrancescosoffritti.androidyoutubeplayer.core.player.views.YouTubePlayerView
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import timber.log.Timber
import java.text.SimpleDateFormat
import java.util.*
import kotlin.math.floor

@AndroidEntryPoint
class DetailFragment : Fragment() {

    private var isSaveOnWatchlist: Boolean = false
    private var screeningItemId: Int? = null
    private var isMovie: Boolean = false

    private val viewModel: DetailsViewModel by viewModels()

    private val networkAdapter = NetworkAdapter()
    private lateinit var recommendationAdapter: RecommendationAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val args: DetailFragmentArgs by navArgs()
        screeningItemId = args.screeningID
        isMovie = args.isMovieBoolean
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View {
        val binding = FragmentDetailBinding.inflate(inflater, container, false)

        collectScreeningDetailFromViewModel(binding)
        collectWatchlistDataFromViewModel(binding)
        collectTrailer()
        collectRecommendations(binding)

        initViews(binding)

        return binding.root
    }

    private fun initViews(binding: FragmentDetailBinding) {
        setupNetworkList(binding)
        setupRecommendedList(binding)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        screeningItemId?.let {
            Timber.i("MAS - screeningID: $it")
            if(savedInstanceState==null){
                if (isMovie) viewModel.movie(it)
                else viewModel.tVShows(it)
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        Glide.get(requireContext()).clearMemory()
    }

    private fun collectScreeningDetailFromViewModel(binding: FragmentDetailBinding) {
        binding.coordinatorContainerDetail.visibility = View.INVISIBLE
        lifecycleScope.launchWhenCreated {
            viewModel.uiState.collect { state ->
                when (state) {
                    is State.Success -> {
                        binding.layoutErrorDetail.root.visibility = View.GONE
                        binding.layoutLoadingDetail.root.visibility = View.GONE
                        setupUI(binding, state.data)
                    }
                    is State.Error -> {
                        binding.layoutLoadingDetail.root.visibility = View.GONE
                        binding.layoutErrorDetail.root.visibility = View.VISIBLE
                        binding.coordinatorContainerDetail.visibility = View.VISIBLE
                    }
                    is State.Loading -> {
                        binding.layoutErrorDetail.root.visibility = View.GONE
                        binding.layoutLoadingDetail.root.visibility = View.VISIBLE
                    }
                    else -> {}
                }
            }
        }
    }

    private fun collectWatchlistDataFromViewModel(binding: FragmentDetailBinding) {
        lifecycleScope.launch {
            viewModel.watchlistUIState.collectLatest {
                when (it) {
                    is State.Success -> {
                        binding.layoutErrorDetail.root.visibility = View.GONE
                        binding.layoutLoadingDetail.root.visibility = View.GONE
                        selectOrUnselectWatchlistFav(binding, it.data)
                        isSaveOnWatchlist = it.data
                    }
                    State.Error -> {
                        binding.layoutLoadingDetail.root.visibility = View.GONE
                        binding.layoutErrorDetail.root.visibility = View.VISIBLE
                        binding.coordinatorContainerDetail.visibility = View.VISIBLE
                    }
                    State.Loading -> {
                        binding.layoutErrorDetail.root.visibility = View.GONE
                        binding.layoutLoadingDetail.root.visibility = View.VISIBLE
                    }
                    else -> {}
                }
            }
        }
    }

    private fun collectTrailer() {
        lifecycleScope.launchWhenStarted {
            for (it in viewModel.trailerState) {
                Timber.i("MAS - collectTrailer.state: $it")
                when (it) {
                    is State.Success<String> -> {
                        if (it.data.isEmpty())
                            Toast.makeText(context, "No trailer found. Sorry!", Toast.LENGTH_LONG).show()
                        else {
                            showDialog(it.data)
                        }
                    }
                    else -> {
                        Toast.makeText(context, "There was an error. Please try again", Toast.LENGTH_LONG).show()
                    }
                }
            }
        }
    }

    private fun collectRecommendations(binding: FragmentDetailBinding) {
        lifecycleScope.launchWhenStarted {
            viewModel.recommendedUIState.collectLatest {
                Timber.i("MAS - collectRecommendations.state: $it")
                when (it) {
                    is State.Success -> {
                        if (it.data.isEmpty()) {
                            binding.recomendedList.isVisible = false
                            binding.recomendedHeader.isVisible = false
                        } else {
                            recommendationAdapter.submitList(it.data)
                            binding.recomendedList.isVisible = true
                            binding.recomendedHeader.isVisible = true
                        }
                    }
                    is State.Error -> {
                        Toast.makeText(context, "Couldn't get recommendations. Sorry!", Toast.LENGTH_LONG).show()
                    }
                    else -> {
                        //DO NOTHING
                    }
                }
            }
        }
    }

    private fun setupUI(binding: FragmentDetailBinding, screening: Screening) {

        setImages(binding, screening)

        setNetworkImages(binding, screening)

        screening.release_date?.let { setDate(binding, it) }

        setPercentage(binding, screening.vote_average)

        setupToolbar(binding, screening)

        binding.toolbar.title = screening.name

        binding.statusDetailTextView.text = screening.status

        val genresListText = screening.genres?.map {
            it.name
        }.toString()
        val genresTextWithoutCharacters = genresListText.subSequence(1, genresListText.lastIndex)
        binding.genresDetailTextView.text = genresTextWithoutCharacters

        binding.numberOfEpisodesDetailTextView.visibility = View.GONE
        binding.numberOfSeasonsDetailTextView.visibility = View.GONE

        binding.numberOfSeasonsDetailTextView.text =
            screening.number_of_seasons?.let {
                binding.numberOfSeasonsDetailTextView.visibility = View.VISIBLE
                resources.getQuantityString(
                    R.plurals.numberOfSeasons,
                    it,
                    it
                )
            }
        binding.numberOfEpisodesDetailTextView.text =
            screening.number_of_episodes?.let {
                binding.numberOfEpisodesDetailTextView.visibility = View.VISIBLE
                resources.getQuantityString(
                    R.plurals.numberOfEpisodes,
                    it,
                    it
                )
            }

        if(screening.overview.isEmpty()) binding.overviewDetailTextView.text = resources.getString(R.string.no_overview)
        else binding.overviewDetailTextView.text = screening.overview
        binding.coordinatorContainerDetail.visibility = View.VISIBLE

        screeningItemId?.let {
            viewModel.existAsFavorite(it)

            if (isMovie)
                viewModel.getMovieSimilar(it)
            else
                viewModel.getTVShowSimilar(it)
        }

        binding.trailerTextView.setOnClickListener {
            screeningItemId?.let {
                if (isMovie)
                    viewModel.getMovieTrailer(it)
                else
                    viewModel.getTVShowTrailer(it)
            }
        }
    }

    private fun setPercentage(
        binding: FragmentDetailBinding,
        voteAverage: Double
    ) {
        val percentage = (voteAverage * 10).toInt()

        val context = binding.root.context

        binding.popularityRatingNumber.text = context.getString(R.string.popularity, percentage)

        when {
            percentage < 25 -> binding.popularityThumbIcon.imageTintList =
                ContextCompat.getColorStateList(context, R.color.red)
            percentage < 45 -> binding.popularityThumbIcon.imageTintList =
                ContextCompat.getColorStateList(context, R.color.orange)
            percentage < 75 -> binding.popularityThumbIcon.imageTintList =
                ContextCompat.getColorStateList(context, R.color.yellow)
            else -> binding.popularityThumbIcon.imageTintList =
                ContextCompat.getColorStateList(context, R.color.green)
        }
    }

    private fun setDate(binding: FragmentDetailBinding, firstAirDate: String) {
        try {
            val date = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()).parse(firstAirDate)
            val stringDate =
                date?.let { SimpleDateFormat("MMM dd, yyyy", Locale.getDefault()).format(it) }
            binding.firstAirDateDetailTextView.text = stringDate
        } catch (e: Exception) {
            binding.firstAirDateDetailTextView.text = ""
        }
    }

    private fun setImages(binding: FragmentDetailBinding, screening: Screening) {
        val options = RequestOptions()
            .placeholder(R.drawable.ic_image)
            .error(R.drawable.ic_image)

        val posterURL = resources.getString(R.string.base_imageURL) + screening.poster_path
        val backdropPosterURL = resources.getString(R.string.base_imageURL) + screening.backdrop_path

        val glide = Glide.with(this)

        glide.load(posterURL)
            .apply(options)
            .into(binding.imageDetailImageView)

        glide.load(backdropPosterURL)
            .apply(options)
            .into(binding.backgroundImageToolbarLayout)
    }

    private fun setupToolbar(binding: FragmentDetailBinding, screening: Screening) {
        val navController = findNavController()
        val appBarConfiguration = AppBarConfiguration(navController.graph)

        val toolbar = binding.toolbar

        toolbar.inflateMenu(R.menu.detail_fragment_menu)
        toolbar.setOnMenuItemClickListener {
            when (it.itemId) {
                R.id.ic_heart_watchlist -> {
                    if (!isSaveOnWatchlist) {
                        viewModel.addToWatchlist(screening)
                    } else {
                        viewModel.deleteFromWatchlist(screening)
                    }
                    true
                }
                R.id.ic_share -> {
                    val mediaType = if (screening.media_type == "tv") "tv show" else screening.media_type
                    val sendIntent = Intent().apply {
                        action = Intent.ACTION_SEND
                        putExtra(
                            Intent.EXTRA_TEXT,
                            "Check out this $mediaType! \n ${resources.getString(R.string.to_watch_url)}/${screening.media_type}/${screening.id}")
                        type = "text/plain"
                    }

                    val shareIntent = Intent.createChooser(sendIntent, null)
                    requireActivity().startActivity(shareIntent)
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

        toolbar.navigationIcon = AppCompatResources.getDrawable(requireContext(), R.drawable.ic_back)
        toolbar.setNavigationOnClickListener {
            if (!navController.navigateUp()) {
                activity?.finish()
            }
        }
    }

    private fun selectOrUnselectWatchlistFav(binding: FragmentDetailBinding, isFav: Boolean) {
        val watchlistItem = binding.toolbar.menu.findItem(R.id.ic_heart_watchlist)
        if (isFav) {
            watchlistItem.icon =
                AppCompatResources.getDrawable(requireContext(), R.drawable.ic_heart_selected)
        } else watchlistItem.icon =
            AppCompatResources.getDrawable(requireContext(), R.drawable.ic_heart_unselected)
    }

    private fun showDialog(videoKey: String) {
        val dialog = activity?.let { Dialog(it) }

        dialog?.apply {
            requestWindowFeature(Window.FEATURE_NO_TITLE)
            setCancelable(true)
            setContentView(R.layout.dialog_trailer)

            val videoView = findViewById<YouTubePlayerView>(R.id.screening_trailer)
            lifecycle.addObserver(videoView)

            videoView.addYouTubePlayerListener(object : AbstractYouTubePlayerListener() {
                override fun onReady(@NonNull youTubePlayer: YouTubePlayer) {
                    youTubePlayer.loadOrCueVideo(lifecycle, videoKey, 0f)
                }
            })

            val metrics = resources.displayMetrics
            val width = metrics.widthPixels
            val density = metrics.density

            window?.setLayout((width - (8 * density).toInt()), WindowManager.LayoutParams.WRAP_CONTENT)

            setOnDismissListener { videoView.release() }

            show()
        }
    }

    private fun setupNetworkList(binding: FragmentDetailBinding) {
        binding.networkList.apply {
            val displayMetrics = context.resources.displayMetrics
            val dpWidth = displayMetrics.widthPixels / displayMetrics.density

            val scaling = resources.getInteger(R.integer.screening_width)
            val columnCount = floor(dpWidth / scaling).toInt() + 1

            layoutManager = GridLayoutManager(context, columnCount)
            adapter = networkAdapter
        }
    }

    private fun setupRecommendedList(binding: FragmentDetailBinding) {
        binding.recomendedList.apply {
            val displayMetrics = context.resources.displayMetrics
            val dpWidth = displayMetrics.widthPixels / displayMetrics.density

            val scaling = resources.getInteger(R.integer.screening_width)
            val columnCount = floor(dpWidth / scaling).toInt()

            val widthSize = (floor(dpWidth / columnCount) * displayMetrics.density).toInt()

            recommendationAdapter = RecommendationAdapter(widthSize) { screening: Screening ->
                val action = DetailFragmentDirections.actionOpenRecommendation(screening.id, isMovie)
                findNavController().navigate(action)
            }

            layoutManager = LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false)
            adapter = recommendationAdapter
        }
    }

    private fun setNetworkImages(binding: FragmentDetailBinding, screening: Screening){
        if (screening.networks.isNotEmpty()){
            networkAdapter.submitList(screening.networks)
        } else {
            binding.networksHeader.visibility = View.GONE
        }
    }
}
