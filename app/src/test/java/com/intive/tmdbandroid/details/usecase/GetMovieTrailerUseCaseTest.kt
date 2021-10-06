package com.intive.tmdbandroid.details.usecase

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import app.cash.turbine.test
import com.intive.tmdbandroid.common.MainCoroutineRule
import com.intive.tmdbandroid.repository.CatalogRepository
import com.intive.tmdbandroid.usecase.GetMovieTrailer
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
class GetMovieTrailerUseCaseTest {

    @get:Rule
    var mainCoroutineRule = MainCoroutineRule()

    @get:Rule
    var instantTaskExecutorRule = InstantTaskExecutorRule()

    private val trailerKey = "This is a trailer key"

    private lateinit var getMovieTrailerUseCase: GetMovieTrailer
    @Mock
    private lateinit var catalogRepository: CatalogRepository

    @Before
    fun setup(){
        getMovieTrailerUseCase = GetMovieTrailer(catalogRepository)
    }

    @Test
    @ExperimentalTime
    fun invokeTest() {
        mainCoroutineRule.runBlockingTest {
            BDDMockito.given(catalogRepository.getMovieTrailer(anyInt())).willReturn(flowOf(trailerKey))

            val expected = getMovieTrailerUseCase(2)

            expected.test {
                Assert.assertEquals(awaitItem(), trailerKey)
                awaitComplete()
            }
        }
    }
}