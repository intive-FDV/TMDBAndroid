package com.intive.tmdbandroid.details.usecase

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import app.cash.turbine.test
import com.intive.tmdbandroid.common.MainCoroutineRule
import com.intive.tmdbandroid.model.Genre
import com.intive.tmdbandroid.model.Network
import com.intive.tmdbandroid.model.Screening
import com.intive.tmdbandroid.repository.CatalogRepository
import com.intive.tmdbandroid.usecase.GetMovieSimilarUseCase
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.test.runBlockingTest
import org.junit.Assert
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.ArgumentMatchers.anyInt
import org.mockito.BDDMockito
import org.mockito.Mock
import org.mockito.junit.MockitoJUnitRunner
import kotlin.time.ExperimentalTime

@ExperimentalCoroutinesApi
@RunWith(MockitoJUnitRunner::class)
class GetMovieSimilarUseCaseTest {

    @get:Rule
    var mainCoroutineRule = MainCoroutineRule()

    @get:Rule
    var instantTaskExecutorRule = InstantTaskExecutorRule()

    private val screenings = listOf(
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
            )
        )

    private lateinit var getMovieSimilarUseCase: GetMovieSimilarUseCase
    @Mock
    private lateinit var catalogRepository: CatalogRepository

    @Before
    fun setup(){
        getMovieSimilarUseCase = GetMovieSimilarUseCase(catalogRepository)
    }

    @Test
    @ExperimentalTime
    fun invokeTest() {
        mainCoroutineRule.runBlockingTest {
            BDDMockito.given(catalogRepository.getMovieSimilar(anyInt())).willReturn(flowOf(screenings))

            val expected = getMovieSimilarUseCase(2)

            expected.test {
                Assert.assertEquals(awaitItem(), screenings)
                awaitComplete()
            }
        }
    }
}