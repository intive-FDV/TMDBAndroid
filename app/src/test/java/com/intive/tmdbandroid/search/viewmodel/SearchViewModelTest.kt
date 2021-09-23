package com.intive.tmdbandroid.search.viewmodel

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.paging.PagingData
import app.cash.turbine.test
import com.google.common.truth.Truth
import com.intive.tmdbandroid.common.MainCoroutineRule
import com.intive.tmdbandroid.common.State
import com.intive.tmdbandroid.entity.ResultTVShowOrMovie
import com.intive.tmdbandroid.usecase.SearchUseCase
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.test.runBlockingTest
import org.junit.Before
import org.junit.Ignore
import org.junit.Rule
import org.junit.Test
import org.mockito.BDDMockito
import org.mockito.Mockito.*
import kotlin.time.ExperimentalTime

@ExperimentalCoroutinesApi
class SearchViewModelTest{

    @get:Rule
    var mainCoroutineRule = MainCoroutineRule()

    @get:Rule
    var instantTaskExecutorRule = InstantTaskExecutorRule()

    private val testResultTVShowOrMoviePagingData = PagingData.from(
        listOf(
            ResultTVShowOrMovie(
                backdrop_path = "BACKDROP_PATH",
                first_air_date = "1983-10-20",
                genre_ids = listOf(1,2),
                id = 1,
                name = "Simona la Cacarisa",
                original_name = "El cochiloco",
                overview = "Simona la cacarisa, el cochiloco",
                poster_path = "POSTER_PATH",
                vote_average = 10.5,
                vote_count = 100,
                media_type = "tv",
                adult = false,
                original_language = "en-US",
                original_title = "Simona la Cacarisa",
                popularity = 56.0,
                release_date = "1983-10-20",
                title = "Simona la Cacarisa",
                video = false
            )
        )
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
    @ExperimentalCoroutinesApi
    @Ignore("There's a problem in how the cachedIn ext func from paging data works (it's using a flow to handle the cache which makes the content of the success not to be a paging data but actually a new flow). Ignoring this test for now, until we get a better way to test the paging library.")
    fun tvShowsTest() = mainCoroutineRule.runBlockingTest {
        `when`(searchUseCase.invoke(anyString())).thenReturn(
            flow {
                emit(
                    testResultTVShowOrMoviePagingData
                )
            }
        )

        searchViewModel.search("Simona la Cacarisa")

        searchViewModel.uiState.test {
            Truth.assertThat(awaitItem()).isEqualTo(State.Success(testResultTVShowOrMoviePagingData))
        }

    }

    @ExperimentalTime
    @ExperimentalCoroutinesApi
    @Test
    fun searchTvShowError() {
        mainCoroutineRule.runBlockingTest {
            val runtimeException = RuntimeException()
            BDDMockito.given(searchUseCase.invoke(anyString())).willReturn(flow {
                throw runtimeException
            })

            searchViewModel.search("alberto fernandez")

            searchViewModel.uiState.test {
                Truth.assertThat(awaitItem()).isEqualTo(State.Error)
            }
        }

    }

}