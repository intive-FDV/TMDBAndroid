package com.intive.tmdbandroid.details.viewmodel

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import app.cash.turbine.test
import com.google.common.truth.Truth
import com.intive.tmdbandroid.common.MainCoroutineRule
import com.intive.tmdbandroid.common.State
import com.intive.tmdbandroid.model.Genre
import com.intive.tmdbandroid.model.TVShow
import com.intive.tmdbandroid.usecase.DetailTVShowUseCase
import com.intive.tmdbandroid.usecase.GetIfExistsUseCase
import com.intive.tmdbandroid.usecase.RemoveTVShowFromWatchlistUseCase
import com.intive.tmdbandroid.usecase.SaveTVShowInWatchlistUseCase
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.test.runBlockingTest
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.mockito.Mockito.*
import kotlin.time.ExperimentalTime

@ExperimentalCoroutinesApi
class DetailsViewModelTest {

    @get:Rule
    var mainCoroutineRule = MainCoroutineRule()

    @get:Rule
    var instantTaskExecutorRule = InstantTaskExecutorRule()

    private val tvShow = TVShow(
        backdrop_path = "BACKDROP_PATH",
        first_air_date = "1983-10-20",
        genres = listOf(Genre(1, "genre1"), Genre(2, "genre2")),
        id = 1,
        name = "Simona la Cacarisa",
        original_name = "El cochiloco",
        overview = "Simona la cacarisa, el cochiloco",
        poster_path = "POSTER_PATH",
        vote_average = 10.5,
        vote_count = 100,
        created_by = emptyList(),
        last_air_date = "1990-09-25",
        number_of_episodes = 5,
        number_of_seasons = 2,
        status = "Online"
    )

    private lateinit var detailViewModel: DetailsViewModel
    private lateinit var tVShowUseCase: DetailTVShowUseCase
    private lateinit var saveTVShowInWatchlistUseCase: SaveTVShowInWatchlistUseCase
    private lateinit var removeTVShowFromWatchlistUseCase: RemoveTVShowFromWatchlistUseCase
    private lateinit var getIfExistsUseCase: GetIfExistsUseCase


    @Before
    fun setup() {
        tVShowUseCase = mock(DetailTVShowUseCase::class.java)
        saveTVShowInWatchlistUseCase = mock(SaveTVShowInWatchlistUseCase::class.java)
        removeTVShowFromWatchlistUseCase = mock(RemoveTVShowFromWatchlistUseCase::class.java)
        getIfExistsUseCase = mock(GetIfExistsUseCase::class.java)
        detailViewModel = DetailsViewModel(
            tVShowUseCase,
            saveTVShowInWatchlistUseCase,
            removeTVShowFromWatchlistUseCase,
            getIfExistsUseCase
        )
    }

    @Test
    @ExperimentalTime
    fun tVShowsTest() = mainCoroutineRule.runBlockingTest {
        `when`(tVShowUseCase.invoke(anyInt())).thenReturn(
            flow {
                emit(
                    tvShow
                )
            }
        )

        detailViewModel.tVShows(2)

        detailViewModel.uiState.test {
            Truth.assertThat(awaitItem()).isEqualTo(State.Success(tvShow))
        }
    }

}