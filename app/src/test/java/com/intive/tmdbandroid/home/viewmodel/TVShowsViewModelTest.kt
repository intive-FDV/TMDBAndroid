package com.intive.tmdbandroid.home.viewmodel

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.paging.PagingData
import app.cash.turbine.test
import com.google.common.truth.Truth
import com.intive.tmdbandroid.common.MainCoroutineRule
import com.intive.tmdbandroid.common.State
import com.intive.tmdbandroid.model.Genre
import com.intive.tmdbandroid.model.Network
import com.intive.tmdbandroid.model.Screening
import com.intive.tmdbandroid.usecase.PaginatedPopularTVShowsUseCase
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.test.runBlockingTest
import org.junit.Before
import org.junit.Ignore
import org.junit.Rule
import org.junit.Test
import org.mockito.BDDMockito
import org.mockito.Mockito.`when`
import org.mockito.Mockito.mock
import kotlin.time.ExperimentalTime

@ExperimentalCoroutinesApi
class TVShowsViewModelTest {

    @get:Rule
    var mainCoroutineRule = MainCoroutineRule()

    @get:Rule
    var instantExecutorRule = InstantTaskExecutorRule()

    private lateinit var tvShowsViewModel: TVShowsViewModel
    private lateinit var paginatedPopularTVShowsUseCase: PaginatedPopularTVShowsUseCase

    @Before
    fun setup() {
        paginatedPopularTVShowsUseCase = mock(PaginatedPopularTVShowsUseCase::class.java)
        tvShowsViewModel = TVShowsViewModel(paginatedPopularTVShowsUseCase)
    }

    private val screeningPagingData = PagingData.from(
        listOf(
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
                vote_count = 100,
                popularity = 34.0,
                media_type = "tv",
                adult = false,
                genre_ids = null,
                video = false,
                networks = listOf(Network("/netflixlogo.jpg", "netflix", 123, "ARG"))
            ),
            Screening(
                backdrop_path = "BACKDROP_PATH_2",
                release_date = "1983-10-25",
                genres = listOf(Genre(4, "genre4"), Genre(6, "genre6")),
                id = 2,
                name = "Friends",
                number_of_episodes = 56,
                number_of_seasons = 6,
                overview = "Amigos que viven una amistad muy profunda",
                poster_path = "POSTER_PATH_2",
                status = "Online",
                vote_average = 15.5,
                vote_count = 100,
                popularity = 80.0,
                media_type = "tv",
                adult = false,
                genre_ids = null,
                video = false,
                networks = listOf(Network("/netflixlogo.jpg", "netflix", 123, "ARG"))
            )
        )
    )


    @ExperimentalTime
    @Ignore("This test failed because PagingData does not perform very well in the test")
    @Test
    fun getPopularTVShows_Success() = mainCoroutineRule.runBlockingTest {
        `when`(paginatedPopularTVShowsUseCase()).thenReturn(
            flowOf(screeningPagingData)
        )

        tvShowsViewModel.popularTVShows()

        tvShowsViewModel.uiState.test {
            Truth.assertThat(awaitItem()).isEqualTo(State.Success(screeningPagingData))
        }
    }

    @ExperimentalTime
    @Test
    fun getPopularTVShows_Error() {
        mainCoroutineRule.runBlockingTest {
            val runtimeException = RuntimeException()
            BDDMockito.given(paginatedPopularTVShowsUseCase()).willReturn(flow {
                throw runtimeException
            })

            tvShowsViewModel.popularTVShows()

            tvShowsViewModel.uiState.test {
                Truth.assertThat(awaitItem()).isEqualTo(State.Error)
            }

        }
    }

}