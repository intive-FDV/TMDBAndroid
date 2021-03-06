package com.intive.tmdbandroid.usecase

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import app.cash.turbine.test
import com.intive.tmdbandroid.common.MainCoroutineRule
import com.intive.tmdbandroid.model.Genre
import com.intive.tmdbandroid.model.Network
import com.intive.tmdbandroid.model.TVShow
import com.intive.tmdbandroid.repository.CatalogRepository
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.test.runBlockingTest
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.mockito.Mockito.*
import kotlin.time.ExperimentalTime

@ExperimentalCoroutinesApi
class DetailTVShowUseCaseTest{

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
        overview = "Simona la cacarisa, el cochiloco",
        poster_path = "POSTER_PATH",
        vote_average = 10.5,
        last_air_date = "1990-09-25",
        number_of_episodes = 5,
        number_of_seasons = 2,
        status = "Online",
        networks = listOf(Network("/netflixlogo.jpg")),
        my_rate = 3.5,
        my_favorite = true
    )

    private lateinit var detailUseCase: DetailTVShowUseCase
    private lateinit var catalogRepository: CatalogRepository

    @Before
    fun setup() {
        catalogRepository = mock(CatalogRepository::class.java)
        detailUseCase = DetailTVShowUseCase(catalogRepository)
    }

    @Test
    @ExperimentalCoroutinesApi
    @ExperimentalTime
    fun invockeTest() = mainCoroutineRule.runBlockingTest {
        `when`(catalogRepository.getTVShowByID(anyInt())).thenReturn(
            flow {
                emit(
                    tvShow
                )
            }
        )

        val expected = detailUseCase(2)

        expected.test {
            assertEquals(awaitItem(), tvShow)
            awaitComplete()
        }
    }
}