package com.intive.tmdbandroid.details.usecase

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import app.cash.turbine.test
import com.intive.tmdbandroid.common.MainCoroutineRule
import com.intive.tmdbandroid.model.Genre
import com.intive.tmdbandroid.model.Network
import com.intive.tmdbandroid.model.Screening
import com.intive.tmdbandroid.repository.WatchlistRepository
import com.intive.tmdbandroid.usecase.InsertInWatchlistUseCase
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runBlockingTest
import org.junit.Assert
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.Mock
import org.mockito.junit.MockitoJUnitRunner
import kotlin.time.ExperimentalTime

@ExperimentalCoroutinesApi
@RunWith(MockitoJUnitRunner::class)
class SaveTVShowInWatchlistUseCaseTest {

    @get:Rule
    var mainCoroutineRule = MainCoroutineRule()

    @get:Rule
    var instantTaskExecutorRule = InstantTaskExecutorRule()

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
        vote_count = 100,
        popularity = 34.0,
        media_type = "tv",
        adult = false,
        genre_ids = null,
        video = false,
        networks = listOf(Network("/netflixlogo.jpg", "netflix", 123, "ARG")),
        my_rate = 3.5,
        my_favorite = true
    )

    private lateinit var saveTVShowInWatchlistUseCase: InsertInWatchlistUseCase
    @Mock
    private lateinit var watchlistRepository: WatchlistRepository

    @Before
    fun setup() {
        saveTVShowInWatchlistUseCase = InsertInWatchlistUseCase(watchlistRepository)
    }

    @Test
    @ExperimentalCoroutinesApi
    @ExperimentalTime
    fun invokeTest() = mainCoroutineRule.runBlockingTest {
        BDDMockito.given(watchlistRepository.insert(screening)).willReturn(
            flowOf(true)
        )

        val expected = saveTVShowInWatchlistUseCase(screening)

        expected.test {
            Assert.assertEquals(awaitItem(), true)
            awaitComplete()
        }
    }
}