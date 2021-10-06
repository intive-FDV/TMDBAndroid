package com.intive.tmdbandroid.details.viewmodel

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import app.cash.turbine.test
import com.google.common.truth.Truth
import com.intive.tmdbandroid.common.MainCoroutineRule
import com.intive.tmdbandroid.common.State
import com.intive.tmdbandroid.model.*
import com.intive.tmdbandroid.usecase.*
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.consumeAsFlow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.test.runBlockingTest
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.BDDMockito
import org.mockito.Mock
import org.mockito.Mockito.*
import org.mockito.junit.MockitoJUnitRunner
import kotlin.time.ExperimentalTime

@ExperimentalCoroutinesApi
@RunWith(MockitoJUnitRunner::class)
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
        last_air_date = "1990-09-25",
        number_of_episodes = 5,
        number_of_seasons = 2,
        status = "Online",
        networks = listOf(Network("/netflixlogo.jpg"))
    )

    private val movie = Movie(
        backdrop_path = "BACKDROP_PATH",
        release_date = "1983-10-20",
        genres = listOf(Genre(1, "genre1"), Genre(2, "genre2")),
        id = 1,
        title = "Simona la Cacarisa",
        overview = "Simona la cacarisa, el cochiloco",
        poster_path = "POSTER_PATH",
        vote_average = 10.5,
        status = "Online",
        popularity = 34.0
    )

    private val screening = Screening(
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
        networks = listOf(Network("/netflixlogo.jpg"))
    )

    private val videoKey = "VIDEO_KEY"

    private lateinit var detailViewModel: DetailsViewModel
    @Mock
    private lateinit var tVShowUseCase: DetailTVShowUseCase
    @Mock
    private lateinit var saveTVShowInWatchlistUseCase: InsertInWatchlistUseCase
    @Mock
    private lateinit var deleteFromWatchlistUseCase: DeleteFromWatchlistUseCase
    @Mock
    private lateinit var getIfExistsUseCase: ExistUseCase
    @Mock
    private lateinit var movieUseCase: DetailMovieUseCase
    @Mock
    private lateinit var tvShowTrailerUseCase: GetTVShowTrailerUseCase
    @Mock
    private lateinit var movieTrailerUseCase: GetMovieTrailerUseCase
    @Mock
    private lateinit var getTVShowSimilarUseCase: GetTVShowSimilarUseCase
    @Mock
    private lateinit var getMovieSimilarUseCase: GetMovieSimilarUseCase


    @Before
    fun setup() {
        detailViewModel = DetailsViewModel(
            tVShowUseCase,
            movieUseCase,
            saveTVShowInWatchlistUseCase,
            deleteFromWatchlistUseCase,
            getIfExistsUseCase,
            tvShowTrailerUseCase,
            movieTrailerUseCase,
            getTVShowSimilarUseCase,
            getMovieSimilarUseCase
        )
    }

    @Test
    @ExperimentalTime
    fun tVShowsTest() = mainCoroutineRule.runBlockingTest {
        `when`(tVShowUseCase(anyInt())).thenReturn(
            flow {
                emit(
                    tvShow
                )
            }
        )

        detailViewModel.tVShows(2)

        detailViewModel.uiState.test {
            Truth.assertThat(awaitItem()).isEqualTo(State.Success(tvShow.toScreening()))
        }
    }

    @Test
    @ExperimentalTime
    fun movieTest() = mainCoroutineRule.runBlockingTest {
        `when`(movieUseCase(anyInt())).thenReturn(
            flow {
                emit(
                    movie
                )
            }
        )

        detailViewModel.movie(2)

        detailViewModel.uiState.test {
            Truth.assertThat(awaitItem()).isEqualTo(State.Success(movie.toScreening()))
        }
    }

    @Test
    @ExperimentalTime
    fun addToWatchlistTestSuccess() = mainCoroutineRule.runBlockingTest {
        BDDMockito.given(saveTVShowInWatchlistUseCase(screening)).willReturn(
            flow {
                emit(true)
            })

        detailViewModel.addToWatchlist(screening)

        detailViewModel.watchlistUIState.test {
            Truth.assertThat(awaitItem()).isEqualTo(State.Success(true))
        }
    }

    @Test
    @ExperimentalTime
    fun addToWatchlistTestFailure() = mainCoroutineRule.runBlockingTest {
        val runtimeException = RuntimeException()
        BDDMockito.given(saveTVShowInWatchlistUseCase(screening)).willReturn(
            flow {
                throw runtimeException
            })

        detailViewModel.addToWatchlist(screening)

        detailViewModel.watchlistUIState.test {
            Truth.assertThat(awaitItem()).isEqualTo(State.Error)
        }
    }

    @Test
    @ExperimentalTime
    fun deleteFromWatchlistTestSuccess() = mainCoroutineRule.runBlockingTest {
        BDDMockito.given(deleteFromWatchlistUseCase(screening)).willReturn(
            flow {
                emit(false)
            })

        detailViewModel.deleteFromWatchlist(screening)

        detailViewModel.watchlistUIState.test {
            Truth.assertThat(awaitItem()).isEqualTo(State.Success(false))
        }
    }

    @Test
    @ExperimentalTime
    fun deleteFromWatchlistTestFailure() = mainCoroutineRule.runBlockingTest {
        val runtimeException = RuntimeException()
        BDDMockito.given(deleteFromWatchlistUseCase(screening)).willReturn(
            flow {
                throw runtimeException
            })

        detailViewModel.deleteFromWatchlist(screening)

        detailViewModel.watchlistUIState.test {
            Truth.assertThat(awaitItem()).isEqualTo(State.Error)
        }
    }

    @Test
    @ExperimentalTime
    fun existAsFavoriteTestSuccess() = mainCoroutineRule.runBlockingTest {
        BDDMockito.given(getIfExistsUseCase(anyInt())).willReturn(
            flow {
                emit(false)
            })

        detailViewModel.existAsFavorite(2)

        detailViewModel.watchlistUIState.test {
            Truth.assertThat(awaitItem()).isAnyOf(State.Success(true), State.Success(false))
        }
    }

    @Test
    @ExperimentalTime
    fun existAsFavoriteTestFailure() = mainCoroutineRule.runBlockingTest {
        val runtimeException = RuntimeException()
        BDDMockito.given(getIfExistsUseCase(anyInt())).willReturn(
            flow {
                throw runtimeException
            })

        detailViewModel.existAsFavorite(2)

        detailViewModel.watchlistUIState.test {
            Truth.assertThat(awaitItem()).isEqualTo(State.Error)
        }
    }

    @Test
    @ExperimentalTime
    fun getTVShowTrailerTest() = mainCoroutineRule.runBlockingTest {
        `when`(tvShowTrailerUseCase(anyInt())).thenReturn(
            flow {
                emit(
                    videoKey
                )
            }
        )

        detailViewModel.getTVShowTrailer(2)

        detailViewModel.trailerState.consumeAsFlow().test {
            Truth.assertThat(awaitItem()).isEqualTo(State.Success(videoKey))
        }
    }

    @Test
    @ExperimentalTime
    fun getMovieTrailerTest() = mainCoroutineRule.runBlockingTest {
        `when`(movieTrailerUseCase(anyInt())).thenReturn(
            flow {
                emit(
                    videoKey
                )
            }
        )

        detailViewModel.getMovieTrailer(2)

        detailViewModel.trailerState.consumeAsFlow().test {
            Truth.assertThat(awaitItem()).isEqualTo(State.Success(videoKey))
        }
    }

    @Test
    @ExperimentalTime
    fun getTVShowSimilarTest() = mainCoroutineRule.runBlockingTest {
        `when`(getTVShowSimilarUseCase(anyInt())).thenReturn(
            flow {
                emit(
                    listOf(screening)
                )
            }
        )

        detailViewModel.getTVShowSimilar(2)

        detailViewModel.recommendedUIState.test {
            Truth.assertThat(awaitItem()).isEqualTo(State.Success(listOf(screening)))
        }
    }

    @Test
    @ExperimentalTime
    fun getMovieSimilarTest() = mainCoroutineRule.runBlockingTest {
        `when`(getMovieSimilarUseCase(anyInt())).thenReturn(
            flow {
                emit(
                    listOf(screening)
                )
            }
        )

        detailViewModel.getMovieSimilar(2)

        detailViewModel.recommendedUIState.test {
            Truth.assertThat(awaitItem()).isEqualTo(State.Success(listOf(screening)))
        }
    }
}