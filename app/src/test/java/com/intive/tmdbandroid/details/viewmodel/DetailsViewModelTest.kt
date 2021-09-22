package com.intive.tmdbandroid.details.viewmodel

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import app.cash.turbine.test
import com.google.common.truth.Truth
import com.intive.tmdbandroid.common.MainCoroutineRule
import com.intive.tmdbandroid.common.State
import com.intive.tmdbandroid.model.Genre
import com.intive.tmdbandroid.model.CreatedBy
import com.intive.tmdbandroid.model.TVShow
import com.intive.tmdbandroid.entity.TVShowORMEntity
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
        vote_count = 100,
        created_by = emptyList(),
        last_air_date = "1990-09-25",
        number_of_episodes = 5,
        number_of_seasons = 2,
        status = "Online"
    )

    private val tvShowEntity = TVShowORMEntity(
        backdrop_path = "BACKDROP_PATH",
        created_by = listOf(CreatedBy("credit1", 1, 1, "name1","PROFILE_PATH_1"), CreatedBy("credit2", 1, 2, "name2","PROFILE_PATH_2")),
        first_air_date = "1983-10-20",
        genres = listOf(Genre(1, "genre1"), Genre(2, "genre2")),
        id = 1,
        last_air_date = "1990-09-25",
        name = "Simona la Cacarisa",
        number_of_episodes = 5,
        number_of_seasons = 2,
        original_name = "El cochiloco",
        overview = "Simona la cacarisa, el cochiloco",
        poster_path = "POSTER_PATH",
        status = "Online",
        vote_average = 10.5,
        vote_count = 100
    )

    private lateinit var detailViewModel: DetailsViewModel
    @Mock
    private lateinit var tVShowUseCase: DetailTVShowUseCase
    @Mock
    private lateinit var saveTVShowInWatchlistUseCase: SaveTVShowInWatchlistUseCase
    @Mock
    private lateinit var removeTVShowFromWatchlistUseCase: RemoveTVShowFromWatchlistUseCase
    @Mock
    private lateinit var getIfExistsUseCase: GetIfExistsUseCase


    @Before
    fun setup() {
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
        `when`(tVShowUseCase(anyInt())).thenReturn(
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

    @Test
    @ExperimentalTime
    fun addToWatchlistTestSuccess() = mainCoroutineRule.runBlockingTest {
        BDDMockito.given(saveTVShowInWatchlistUseCase(tvShowEntity)).willReturn(
            flow {
                emit(true)
            })

        detailViewModel.addToWatchlist(tvShowEntity)

        detailViewModel.watchlistUIState.test {
            Truth.assertThat(awaitItem()).isEqualTo(State.Success(true))
        }
    }

    @Test
    @ExperimentalTime
    fun addToWatchlistTestFailure() = mainCoroutineRule.runBlockingTest {
        val runtimeException = RuntimeException()
        BDDMockito.given(saveTVShowInWatchlistUseCase(tvShowEntity)).willReturn(
            flow {
                throw runtimeException
            })

        detailViewModel.addToWatchlist(tvShowEntity)

        detailViewModel.watchlistUIState.test {
            Truth.assertThat(awaitItem()).isEqualTo(State.Error)
        }
    }

    @Test
    @ExperimentalTime
    fun deleteFromWatchlistTestSuccess() = mainCoroutineRule.runBlockingTest {
        BDDMockito.given(removeTVShowFromWatchlistUseCase(tvShowEntity)).willReturn(
            flow {
                emit(false)
            })

        detailViewModel.deleteFromWatchlist(tvShowEntity)

        detailViewModel.watchlistUIState.test {
            Truth.assertThat(awaitItem()).isEqualTo(State.Success(false))
        }
    }

    @Test
    @ExperimentalTime
    fun deleteFromWatchlistTestFailure() = mainCoroutineRule.runBlockingTest {
        val runtimeException = RuntimeException()
        BDDMockito.given(removeTVShowFromWatchlistUseCase(tvShowEntity)).willReturn(
            flow {
                throw runtimeException
            })

        detailViewModel.deleteFromWatchlist(tvShowEntity)

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
}