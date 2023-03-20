package com.mind.market.aimissioncompose.domain.goal.use_case

import com.google.common.truth.Truth.assertThat
import com.mind.market.aimissioncompose.data.goal.repository.FakeGoalRepository
import com.mind.market.aimissioncompose.domain.goal.UpdateGoalStatusUseCase
import com.mind.market.aimissioncompose.domain.models.Status
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runTest
import org.junit.Before
import org.junit.Test

@OptIn(ExperimentalCoroutinesApi::class)
class UpdateGoalStatusUseCaseTest {
    private lateinit var fakeRepository: FakeGoalRepository

    @Before
    fun setup() {
        fakeRepository = FakeGoalRepository()
    }

    @Test
    fun`Update goal status returns updated status`() = runTest {
        UpdateGoalStatusUseCase(fakeRepository).invoke(2, Status.TODO) {
            assertThat(it).isEqualTo(Status.IN_PROGRESS)
        }
    }

    @Test
    fun`Update goal status updates nothing when status is deprecated`() = runTest {
        UpdateGoalStatusUseCase(fakeRepository).invoke(2, Status.DEPRECATED) {
            assertThat(it).isEqualTo(Status.DEPRECATED)
        }
    }
}