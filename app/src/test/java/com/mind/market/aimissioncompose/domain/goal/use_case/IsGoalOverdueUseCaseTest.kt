package com.mind.market.aimissioncompose.domain.goal.use_case

import com.google.common.truth.Truth.assertThat
import com.mind.market.aimissioncompose.data.goal.FAKE_GOAL
import com.mind.market.aimissioncompose.data.goal.repository.FakeGoalRepository
import com.mind.market.aimissioncompose.domain.goal.IsGoalOverdueUseCase
import org.junit.Before
import org.junit.Test
import java.time.LocalDateTime

class IsGoalOverdueUseCaseTest {
    private lateinit var fakeRepository: FakeGoalRepository

    @Before
    fun setup() {
        fakeRepository = FakeGoalRepository()
    }

    @Test
    fun `Is goal overdue with finish date in the future returns false`() {
        val result = IsGoalOverdueUseCase().invoke(
            FAKE_GOAL.copy(
                finishDate = LocalDateTime.now().plusDays(2)
            )
        )
        assertThat(result).isFalse()
    }

    @Test
    fun `Is goal overdue with finish date in the past returns true`() {
        val result = IsGoalOverdueUseCase().invoke(
            FAKE_GOAL.copy(
                finishDate = LocalDateTime.now().minusDays(2)
            )
        )
        assertThat(result).isTrue()
    }
}