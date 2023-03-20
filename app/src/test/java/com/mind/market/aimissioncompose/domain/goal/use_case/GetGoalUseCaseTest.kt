package com.mind.market.aimissioncompose.domain.goal.use_case

import com.mind.market.aimissioncompose.data.goal.FAKE_GOAL
import com.mind.market.aimissioncompose.data.goal.repository.FakeGoalRepository
import com.mind.market.aimissioncompose.domain.goal.GetGoalUseCase
import junit.framework.TestCase.assertEquals
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch
import kotlinx.coroutines.test.UnconfinedTestDispatcher
import kotlinx.coroutines.test.runTest
import org.junit.Before
import org.junit.Test

@OptIn(ExperimentalCoroutinesApi::class)
class GetGoalUseCaseTest {
    private lateinit var fakeRepository: FakeGoalRepository

    @Before
    fun setup() {
        fakeRepository = FakeGoalRepository()
    }

    @Test
    fun `Get goal with valid id returns expected goal`() = runTest {
        val result = GetGoalUseCase(fakeRepository).invoke(1)
        val collectJob = launch(UnconfinedTestDispatcher()) { result.collect() }
        result.collect {
            assertEquals(it.data, FAKE_GOAL)
        }
        collectJob.cancel()
    }

    @Test
    fun `Get goal with invalid id returns error`() = runTest {
        val result = GetGoalUseCase(fakeRepository).invoke(-1)
        val collectJob = launch(UnconfinedTestDispatcher()) { result.collect() }
        result.collect {
            assertEquals(it.data, null)
            assertEquals(it.message, "Id is invalid.")
        }
        collectJob.cancel()
    }
}