package com.intive.tmdbandroid.details.usecase

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import app.cash.turbine.test
import com.intive.tmdbandroid.common.MainCoroutineRule
import com.intive.tmdbandroid.repository.CatalogRepository
import com.intive.tmdbandroid.usecase.GetTVShowTrailer
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
class GetTVShowTrailerUseCaseTest {

    @get:Rule
    var mainCoroutineRule = MainCoroutineRule()

    @get:Rule
    var instantTaskExecutorRule = InstantTaskExecutorRule()

    private val trailerKey = "This is a trailer key"

    private lateinit var getTVShowTrailer: GetTVShowTrailer
    @Mock
    private lateinit var catalogRepository: CatalogRepository

    @Before
    fun setup(){
        getTVShowTrailer = GetTVShowTrailer(catalogRepository)
    }

    @Test
    @ExperimentalTime
    fun invokeTest() {
        mainCoroutineRule.runBlockingTest {
            BDDMockito.given(catalogRepository.getTVShowTrailer(anyInt())).willReturn(flowOf(trailerKey))

            val expected = getTVShowTrailer(2)

            expected.test {
                Assert.assertEquals(awaitItem(), trailerKey)
                awaitComplete()
            }
        }
    }
}