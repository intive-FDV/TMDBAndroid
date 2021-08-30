package com.intive.tmdbandroid.repository

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.paging.PagingData
import androidx.paging.PagingSource
import app.cash.turbine.test
import com.intive.tmdbandroid.common.MainCoroutineRule
import com.intive.tmdbandroid.datasource.TVShowPagingSource
import com.intive.tmdbandroid.datasource.network.Service
import com.intive.tmdbandroid.entity.ResultTVShows
import com.intive.tmdbandroid.model.TVShow
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.test.runBlockingTest
import org.junit.Assert.*
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.mockito.Mockito.*
import kotlin.time.ExperimentalTime

@ExperimentalCoroutinesApi
class CatalogRepositoryTest {

    @get:Rule
    var mainCoroutineRule = MainCoroutineRule()

    private val listTVShows = listOf(
        TVShow(
            backdrop_path = "BACKDROP_PATH",
            first_air_date = "1983-10-20",
            genre_ids = listOf(1, 2),
            id = 1,
            name = "Simona la Cacarisa",
            origin_country = listOf("AR"),
            original_language = "es",
            original_name = "El cochiloco",
            overview = "Simona la cacarisa, el cochiloco",
            popularity = 20.2,
            poster_path = "POSTER_PATH",
            vote_average = 10.5,
            vote_count = 100
        )
    )

    private val testTVShowPagingData = PagingData.from(
        listTVShows
    )

    private val testResultTVShows = ResultTVShows(
        page = 1,
        TVShows = listTVShows,
        total_pages = 2,
        total_results = 1,
    )

    private lateinit var service: Service
    private lateinit var catalogRepository: CatalogRepository

    @Before
    fun setup() {
        service = mock(Service::class.java)
        catalogRepository = CatalogRepository(service)
    }

    @Test
    @ExperimentalTime
    fun paginatedPopularTVShowsTest() = mainCoroutineRule.runBlockingTest {
        `when`(service.getPaginatedPopularTVShows(anyInt()))
            .thenReturn(
                flow {
                    emit(
                        testResultTVShows
                    )
                }
            )

        val flowPagingData = catalogRepository.paginatedPopularTVShows()

        flowPagingData.test {

        }
    }
}