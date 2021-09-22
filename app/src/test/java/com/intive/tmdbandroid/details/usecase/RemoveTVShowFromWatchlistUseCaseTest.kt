package com.intive.tmdbandroid.details.usecase

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import app.cash.turbine.test
import com.intive.tmdbandroid.common.MainCoroutineRule
import com.intive.tmdbandroid.entity.TVShowORMEntity
import com.intive.tmdbandroid.model.CreatedBy
import com.intive.tmdbandroid.model.Genre
import com.intive.tmdbandroid.repository.WatchlistRepository
import com.intive.tmdbandroid.usecase.RemoveTVShowFromWatchlistUseCase
import com.intive.tmdbandroid.usecase.SaveTVShowInWatchlistUseCase
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
class RemoveTVShowFromWatchlistUseCaseTest {

    @get:Rule
    var mainCoroutineRule = MainCoroutineRule()

    @get:Rule
    var instantTaskExecutorRule = InstantTaskExecutorRule()

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

    private lateinit var removeTVShowFromWatchlistUseCase: RemoveTVShowFromWatchlistUseCase
    @Mock
    private lateinit var watchlistRepository: WatchlistRepository

    @Before
    fun setup() {
        removeTVShowFromWatchlistUseCase = RemoveTVShowFromWatchlistUseCase(watchlistRepository)
    }

    @Test
    @ExperimentalCoroutinesApi
    @ExperimentalTime
    fun invokeTest() = mainCoroutineRule.runBlockingTest {
        val expected = removeTVShowFromWatchlistUseCase(tvShowEntity)

        expected.test {
            Assert.assertEquals(awaitItem(), false)
            awaitComplete()
        }
    }
}