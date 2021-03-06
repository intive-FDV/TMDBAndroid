package com.intive.tmdbandroid.home.viewmodel

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import app.cash.turbine.test
import com.google.common.truth.Truth
import com.intive.tmdbandroid.common.MainCoroutineRule
import com.intive.tmdbandroid.common.State
import com.intive.tmdbandroid.model.Genre
import com.intive.tmdbandroid.model.Network
import com.intive.tmdbandroid.model.Screening
import com.intive.tmdbandroid.usecase.GetAllItemsInWatchlistUseCase
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.test.runBlockingTest
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.BDDMockito
import org.mockito.Mock
import org.mockito.junit.MockitoJUnitRunner
import kotlin.time.ExperimentalTime

/**
 * Unit tests for the implementation of [WatchlistViewModel].
 */
@RunWith(MockitoJUnitRunner::class)
class WatchlistViewModelTest {

    private val screening = listOf(
            Screening(
                backdrop_path = "BACKDROP_PATH",
                release_date = "1983-10-20",
                genres = listOf(Genre(1, "genre1"), Genre(2, "genre2")),
                id = 1,
                name = "Simona la Cacarisa",
                number_of_episodes = 5,
                number_of_seasons = 2,
                overview = "Simona la cacarisa, el cochiloco",
                poster_path = "POSTER_PATH",
                status = "Online",
                vote_average = 10.5,
                popularity = 34.0,
                media_type = "tv",
                adult = false,
                genre_ids = null,
                video = false,
                networks = listOf(Network("/netflixlogo.jpg")),
                my_rate = 3.5,
                my_favorite = true
            )
        )

    private lateinit var viewModel: WatchlistViewModel

    @Mock
    private lateinit var getAllItemsInWatchlistUseCase: GetAllItemsInWatchlistUseCase

    // Set the main coroutines dispatcher for unit testing.
    @ExperimentalCoroutinesApi
    @get:Rule
    var mainCoroutineRule = MainCoroutineRule()

    // Executes each task synchronously using Architecture Components.
    @get:Rule
    var instantExecutorRule = InstantTaskExecutorRule()

    @Before
    fun setupViewModel() {
        viewModel = WatchlistViewModel(getAllItemsInWatchlistUseCase)
    }

    @ExperimentalTime
    @ExperimentalCoroutinesApi
    @Test
    fun getWatchlistSuccess() {
        mainCoroutineRule.runBlockingTest {
            BDDMockito.given(getAllItemsInWatchlistUseCase()).willReturn(flow {
                emit(screening)
            })

            viewModel.watchlistScreening()

            viewModel.uiState.test {
                val item = awaitItem()
                Truth.assertThat(item).isEqualTo(State.Success(screening))
            }
        }
    }

    @ExperimentalTime
    @ExperimentalCoroutinesApi
    @Test
    fun getWatchlistError() {
        mainCoroutineRule.runBlockingTest {
            val runtimeException = RuntimeException()
            BDDMockito.given(getAllItemsInWatchlistUseCase()).willReturn(flow {
                throw runtimeException
            })

            viewModel.watchlistScreening()

            viewModel.uiState.test {
                Truth.assertThat(awaitItem()).isEqualTo(State.Error)
            }
        }
    }
}
