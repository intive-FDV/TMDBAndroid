package com.intive.tmdbandroid.details.usecase

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import app.cash.turbine.test
import com.intive.tmdbandroid.common.MainCoroutineRule
import com.intive.tmdbandroid.repository.WatchlistRepository
import com.intive.tmdbandroid.usecase.ExistUseCase
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
class GetIfExistsUseCaseTest {

    @get:Rule
    var mainCoroutineRule = MainCoroutineRule()

    @get:Rule
    var instantTaskExecutorRule = InstantTaskExecutorRule()

    private lateinit var getIfExistsUseCase: ExistUseCase
    @Mock
    private lateinit var watchlistRepository: WatchlistRepository

    @Before
    fun setup() {
        getIfExistsUseCase = ExistUseCase(watchlistRepository)
    }

    @Test
    @ExperimentalCoroutinesApi
    @ExperimentalTime
    fun invokeTestEmpty() = mainCoroutineRule.runBlockingTest {
        BDDMockito.given(watchlistRepository.exist(anyInt())).willReturn(flowOf(false))

        val expected = getIfExistsUseCase(2)

        expected.test {
            Assert.assertEquals(awaitItem(), false)
            awaitComplete()
        }
    }

    @Test
    @ExperimentalCoroutinesApi
    @ExperimentalTime
    fun invokeTestNotEmpty() = mainCoroutineRule.runBlockingTest {
        BDDMockito.given(watchlistRepository.exist(anyInt())).willReturn(flowOf(true))

        val expected = getIfExistsUseCase(2)

        expected.test {
            Assert.assertEquals(awaitItem(), true)
            awaitComplete()
        }
    }
}