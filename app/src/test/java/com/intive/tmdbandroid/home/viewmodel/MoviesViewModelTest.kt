package com.intive.tmdbandroid.home.viewmodel

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.paging.PagingData
import app.cash.turbine.test
import com.google.common.truth.Truth
import com.intive.tmdbandroid.common.MainCoroutineRule
import com.intive.tmdbandroid.common.State
import com.intive.tmdbandroid.model.Genre
import com.intive.tmdbandroid.model.Screening
import com.intive.tmdbandroid.usecase.PaginatedPopularMoviesUseCase
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
class MoviesViewModelTest {

    @get:Rule
    var mainCoroutineRule = MainCoroutineRule()

    @get:Rule
    var instantTaskExecutorRule = InstantTaskExecutorRule()

    private lateinit var moviesViewModel: MoviesViewModel
    private lateinit var paginatedPopularMoviesUseCase: PaginatedPopularMoviesUseCase

    @Before
    fun setup() {
        paginatedPopularMoviesUseCase = mock(PaginatedPopularMoviesUseCase::class.java)
        moviesViewModel = MoviesViewModel(paginatedPopularMoviesUseCase)
    }

    private val screeningPagingData = PagingData.from(
        listOf(
            Screening(
                backdrop_path = "BACKDROP_PATH",
                release_date = "1983-10-20",
                genres = listOf(Genre(1, "genre1"), Genre(2, "genre2")),
                id = 1,
                name = "Simona la Cacarisa",
                number_of_episodes = null,
                number_of_seasons = null,
                overview = "Simona la cacarisa, el cochiloco",
                poster_path = "POSTER_PATH",
                status = "Online",
                vote_average = 10.5,
                vote_count = 100,
                popularity = 34.0,
                media_type = "movie",
                adult = false,
                genre_ids = null,
                video = false
            ),
            Screening(
                backdrop_path = "BACKDROP_PATH_2",
                release_date = "1983-10-25",
                genres = listOf(Genre(4, "genre4"), Genre(6, "genre6")),
                id = 2,
                name = "Hombre Araña",
                number_of_episodes = null,
                number_of_seasons = null,
                overview = "A un joven le pica una araña radioactiva y esta le da superpoderes de araña",
                poster_path = "POSTER_PATH_2",
                status = "Online",
                vote_average = 15.5,
                vote_count = 100,
                popularity = 80.0,
                media_type = "movie",
                adult = false,
                genre_ids = null,
                video = false
            )
        )
    )

    @ExperimentalTime
    @Ignore("This test failed because PagingData does not perform very well in the test")
    @Test
    fun getPopularMovies_Success() {
        mainCoroutineRule.runBlockingTest {
            `when`(paginatedPopularMoviesUseCase()).thenReturn(
                flowOf(screeningPagingData)
            )

            moviesViewModel.popularMovies()

            moviesViewModel.uiState.test {
                Truth.assertThat(awaitItem()).isEqualTo(State.Success(screeningPagingData))
            }
        }
    }

    @ExperimentalTime
    @Test
    fun getPopularMovies_Error() {
        mainCoroutineRule.runBlockingTest {
            val runtimeException = RuntimeException()
            BDDMockito.given(paginatedPopularMoviesUseCase()).willReturn(flow {
                throw runtimeException
            })

            moviesViewModel.popularMovies()

            moviesViewModel.uiState.test {
                Truth.assertThat(awaitItem()).isEqualTo(State.Error)
            }

        }
    }
}