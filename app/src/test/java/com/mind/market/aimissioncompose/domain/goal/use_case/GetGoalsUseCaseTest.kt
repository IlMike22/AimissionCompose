package com.mind.market.aimissioncompose.domain.goal.use_case

import com.mind.market.aimissioncompose.data.goal.FAKE_GOAL
import com.mind.market.aimissioncompose.data.goal.repository.FakeGoalRepository
import com.mind.market.aimissioncompose.domain.goal.GetGoalsUseCase
import junit.framework.TestCase.assertEquals
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch
import kotlinx.coroutines.test.UnconfinedTestDispatcher
import kotlinx.coroutines.test.runTest
import org.junit.Before
import org.junit.Test

@OptIn(ExperimentalCoroutinesApi::class)
class GetGoalsUseCaseTest {
    private lateinit var fakeRepository: FakeGoalRepository

    @Before
    fun setup() {
        fakeRepository = FakeGoalRepository()
    }

    @Test
    fun `Get goals returns list of goals`() = runTest {
        val result = GetGoalsUseCase(fakeRepository).invoke()
        val collectJob = launch(UnconfinedTestDispatcher()) { result.collect() }
        result.collect {
            assertEquals(it.data, listOf(FAKE_GOAL, FAKE_GOAL))
        }

        collectJob.cancel()
    }
}