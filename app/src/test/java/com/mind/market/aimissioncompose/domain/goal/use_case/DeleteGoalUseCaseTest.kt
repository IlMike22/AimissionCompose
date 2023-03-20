package com.mind.market.aimissioncompose.domain.goal.use_case

import com.google.common.truth.Truth.assertThat
import com.mind.market.aimissioncompose.data.goal.FAKE_GOAL
import com.mind.market.aimissioncompose.data.goal.repository.FakeGoalRepository
import com.mind.market.aimissioncompose.domain.goal.DeleteGoalUseCase
import com.mind.market.aimissioncompose.domain.models.Goal
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runTest
import org.junit.Before
import org.junit.Test

@OptIn(ExperimentalCoroutinesApi::class)
class DeleteGoalUseCaseTest {
    private lateinit var fakeRepository: FakeGoalRepository

    @Before
    fun setup() {
        fakeRepository = FakeGoalRepository()
    }

    @Test
    fun `Delete goal returns true if goal is not empty`() = runTest {
        DeleteGoalUseCase(fakeRepository).invoke(FAKE_GOAL) { isSuccess ->
            assertThat(isSuccess).isTrue()
        }
    }

    @Test
    fun `Delete goal returns false if goal is empty`() = runTest {
        DeleteGoalUseCase(fakeRepository).invoke(Goal.EMPTY) { isSuccess ->
            assertThat(isSuccess.not()).isTrue()
        }
    }
}