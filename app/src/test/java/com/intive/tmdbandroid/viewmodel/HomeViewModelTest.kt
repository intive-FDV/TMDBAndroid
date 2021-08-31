package com.intive.tmdbandroid.viewmodel

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.paging.PagingData
import app.cash.turbine.test
import com.google.common.truth.Truth
import com.intive.tmdbandroid.common.MainCoroutineRule
import com.intive.tmdbandroid.home.viewmodel.HomeViewModel
import com.intive.tmdbandroid.home.viewmodel.State
import com.intive.tmdbandroid.model.TVShow
import com.intive.tmdbandroid.usecase.PaginatedPopularTVShowsUseCase
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
 * Unit tests for the implementation of [HomeViewModel].
 */
@RunWith(MockitoJUnitRunner::class)
class HomeViewModelTest {

    private val testTVShowPagingData = PagingData.from(
        listOf(
            TVShow(
                backdrop_path = "BACKDROP_PATH",
                first_air_date = "1983-10-20",
                genre_ids = listOf(1, 2),
                id = 1,
                name = "Simona la Cacarisa",
                origin_country = listOf("AR"),
                original_language = "es",
                original_name = "El cochiloco",
                overview = "Simona la cacarisa, el cochiloco",
                popularity = 20.2,
                poster_path = "POSTER_PATH",
                vote_average = 10.5,
                vote_count = 100
            )
        )
    )

    private lateinit var viewModel: HomeViewModel

    @Mock
    private lateinit var popularTVShowsUseCase: PaginatedPopularTVShowsUseCase

    // Set the main coroutines dispatcher for unit testing.
    @ExperimentalCoroutinesApi
    @get:Rule
    var mainCoroutineRule = MainCoroutineRule()

    // Executes each task synchronously using Architecture Components.
    @get:Rule
    var instantExecutorRule = InstantTaskExecutorRule()

    @Before
    fun setupViewModel() {
        viewModel = HomeViewModel(popularTVShowsUseCase)
    }

    @ExperimentalTime
    @ExperimentalCoroutinesApi
    @Test
    fun fetchRestaurantSuccess() {
        mainCoroutineRule.runBlockingTest {
            BDDMockito.given(popularTVShowsUseCase()).willReturn(flow {
                emit(testTVShowPagingData)
            })

            viewModel.popularTVShows()

            viewModel.uiState.test {
                Truth.assertThat(awaitItem()).isEqualTo(State.Success(testTVShowPagingData))
            }
        }

    }

    @ExperimentalTime
    @ExperimentalCoroutinesApi
    @Test
    fun fetchRestaurantError() {
        mainCoroutineRule.runBlockingTest {
            val runtimeException = RuntimeException()
            BDDMockito.given(popularTVShowsUseCase()).willReturn(flow {
                throw runtimeException
            })

            viewModel.popularTVShows()

            viewModel.uiState.test {
                Truth.assertThat(awaitItem()).isEqualTo(State.Error(runtimeException))
            }
        }

    }

}
