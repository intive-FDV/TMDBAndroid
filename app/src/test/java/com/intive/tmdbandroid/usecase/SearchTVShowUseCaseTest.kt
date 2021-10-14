package com.intive.tmdbandroid.usecase

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.paging.PagingData
import app.cash.turbine.test
import com.intive.tmdbandroid.common.MainCoroutineRule
import com.intive.tmdbandroid.model.Genre
import com.intive.tmdbandroid.model.Network
import com.intive.tmdbandroid.model.Screening
import com.intive.tmdbandroid.repository.CatalogRepository
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.test.runBlockingTest
import org.junit.Assert
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.mockito.Mockito.*
import kotlin.time.ExperimentalTime

@ExperimentalCoroutinesApi
class SearchTVShowUseCaseTest {

    @get:Rule
    var mainCoroutineRule = MainCoroutineRule()

    @get:Rule
    var instantTaskExecutorRule = InstantTaskExecutorRule()

    private val screening = PagingData.from(
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
    )

    private lateinit var searchUseCase: SearchUseCase
    private lateinit var catalogRepository: CatalogRepository

    @Before
    fun setup(){
        catalogRepository = mock(CatalogRepository::class.java)
        searchUseCase = SearchUseCase(catalogRepository)
    }

    @Test
    @ExperimentalTime
    fun invokeTest() {
        mainCoroutineRule.runBlockingTest {
            `when`(catalogRepository.searchByName(anyString(), eq("All")))
                .thenReturn(
                    flow {
                        emit(
                            screening
                        )
                    }
                )

            val actual = searchUseCase("cristina kirchner", "All")
            actual.test {
                Assert.assertEquals(awaitItem(), screening)
                awaitComplete()
            }
            verify(catalogRepository, only()).searchByName("cristina kirchner", "All")

        }
    }
}