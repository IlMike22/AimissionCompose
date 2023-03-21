package com.mind.market.aimissioncompose.domain.goal.use_case

import com.google.common.truth.Truth.assertThat
import com.mind.market.aimissioncompose.data.goal.FAKE_GOAL
import com.mind.market.aimissioncompose.data.goal.repository.FakeGoalRepository
import com.mind.market.aimissioncompose.domain.goal.InsertGoalUseCase
import com.mind.market.aimissioncompose.domain.models.Goal
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runTest
import org.junit.Before
import org.junit.Test

@OptIn(ExperimentalCoroutinesApi::class)
class InsertGoalUseCaseTest {
    private lateinit var fakeRepository: FakeGoalRepository

    @Before
    fun setup() {
        fakeRepository = FakeGoalRepository()
    }

    @Test
    fun `Insert goal returns no error`() = runTest {
        InsertGoalUseCase(fakeRepository).invoke(FAKE_GOAL, ::validateResultSuccess)
    }

    @Test
    fun `Insert goal returns error`() = runTest {
        InsertGoalUseCase(fakeRepository).invoke(Goal.EMPTY, ::validateResultFailure)
    }

    private fun validateResultSuccess(error: Throwable?) {
        assertThat(error).isNull()
    }

    private fun validateResultFailure(error: Throwable?) {
        assertThat(error?.message).isEqualTo("Goal is not set. Cannot add an empty goal.")
    }
}