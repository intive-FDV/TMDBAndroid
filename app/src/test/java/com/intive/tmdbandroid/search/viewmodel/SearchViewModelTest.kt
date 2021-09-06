package com.intive.tmdbandroid.search.viewmodel

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import app.cash.turbine.test
import com.google.common.truth.Truth
import com.intive.tmdbandroid.common.MainCoroutineRule
import com.intive.tmdbandroid.common.State
import com.intive.tmdbandroid.model.Genre
import com.intive.tmdbandroid.model.TVShow
import com.intive.tmdbandroid.usecase.SearchUseCase
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.test.runBlockingTest
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.mockito.Mockito.*
import kotlin.time.ExperimentalTime

@ExperimentalCoroutinesApi
class SearchViewModelTest{

    @get:Rule
    var mainCoroutineRule = MainCoroutineRule()

    @get:Rule
    var instantTaskExecutorRule = InstantTaskExecutorRule()

    private val tvShow =  TVShow(
        backdrop_path = "BACKDROP_PATH",
        first_air_date = "1983-10-20",
        genres = listOf(Genre(1, "genre1"), Genre(2,"genre2")),
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

    private lateinit var searchViewModel: SearchViewModel
    private lateinit var searchUseCase: SearchUseCase


    @Before
    fun setup() {
        searchUseCase = mock(SearchUseCase::class.java)
        searchViewModel = SearchViewModel(searchUseCase)
    }

    @Test
    @ExperimentalTime
    fun tVShowsTest() = mainCoroutineRule.runBlockingTest {
        `when`(searchUseCase.invoke(anyString())).thenReturn(
            flow {
                emit(
                    tvShow
                )
            }
        )

        searchViewModel.search("Simona la Cacarisa")

        searchViewModel.uiState.test {
            Truth.assertThat(awaitItem()).isEqualTo(State.Success(tvShow))
        }
    }

}