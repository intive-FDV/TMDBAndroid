package com.intive.tmdbandroid.usecase

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.paging.PagingData
import app.cash.turbine.test
import com.intive.tmdbandroid.common.MainCoroutineRule
import com.intive.tmdbandroid.model.Genre
import com.intive.tmdbandroid.model.Screening
import com.intive.tmdbandroid.repository.CatalogRepository
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.test.runBlockingTest
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.mockito.Mockito.*
import kotlin.time.ExperimentalTime

@ExperimentalCoroutinesApi
class PaginatedPopularMoviesUseCaseTest {

    @get:Rule
    var mainCoroutineRule = MainCoroutineRule()

    @get:Rule
    var instantTaskExecutorRule = InstantTaskExecutorRule()

    private lateinit var repository: CatalogRepository
    private lateinit var paginatedPopularMoviesUseCase: PaginatedPopularMoviesUseCase

    @Before
    fun setup() {
        repository = mock(CatalogRepository::class.java)
        paginatedPopularMoviesUseCase = PaginatedPopularMoviesUseCase(repository)
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
                popularity = 34.0,
                media_type = "movie",
                adult = false,
                genre_ids = null,
                video = false,
                networks = emptyList(),
                my_rate = 3.5,
                my_favorite = true
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
                popularity = 80.0,
                media_type = "movie",
                adult = false,
                genre_ids = null,
                video = false,
                networks = emptyList(),
                my_rate = 3.5,
                my_favorite = true
            )
        )
    )

    @ExperimentalTime
    @Test
    fun getPaginatedPopularMovies() = mainCoroutineRule.runBlockingTest {
        `when`(repository.paginatedPopularMovies()).thenReturn(
            flowOf(screeningPagingData)
        )

        val actual = paginatedPopularMoviesUseCase()

        actual.test {
            assertEquals(awaitItem(), screeningPagingData)
            awaitComplete()
        }
        verify(repository, only()).paginatedPopularMovies()
    }
}