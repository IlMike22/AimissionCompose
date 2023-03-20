package com.mind.market.aimissioncompose.data.goal.repository

import GoalRepository
import com.google.common.truth.Truth.assertThat
import com.mind.market.aimissioncompose.core.GoalReadWriteOperation
import com.mind.market.aimissioncompose.data.common.repository.IGoalRepository
import com.mind.market.aimissioncompose.data.goal.FAKE_GOAL
import com.mind.market.aimissioncompose.data.goal.source.local.FakeGoalLocalDataSource
import com.mind.market.aimissioncompose.data.goal.source.remote.FakeAuthenticationRemoteDataSource
import com.mind.market.aimissioncompose.data.goal.source.remote.FakeGoalRemoteDataSource
import com.mind.market.aimissioncompose.domain.models.Goal
import junit.framework.TestCase.assertEquals
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runTest
import org.junit.Before
import org.junit.Test

@OptIn(ExperimentalCoroutinesApi::class)
class GoalRepositoryTest {
    private lateinit var repository: IGoalRepository
    lateinit var fakeGoalRemoteDataSource: FakeGoalRemoteDataSource
    private lateinit var fakeAuthRemoteDataSource: FakeAuthenticationRemoteDataSource
    private lateinit var fakeGoalLocalDataSource: FakeGoalLocalDataSource

    @Before
    fun setup() {
        fakeGoalRemoteDataSource = FakeGoalRemoteDataSource()
        fakeGoalLocalDataSource = FakeGoalLocalDataSource()
        fakeAuthRemoteDataSource = FakeAuthenticationRemoteDataSource()
        repository = GoalRepository(
            fakeGoalRemoteDataSource,
            fakeGoalLocalDataSource,
            fakeAuthRemoteDataSource
        )
    }

    @Test
    fun `Get goals gets expected fake goals when userId is not empty`() = runTest {
        repository.getGoals(GoalReadWriteOperation.FIREBASE_DATABASE)
        fakeGoalRemoteDataSource.getGoals(userId = "2") { throwable, goals ->
            assertEquals(goals, listOf(Goal.EMPTY, Goal.EMPTY))
            assertEquals(throwable, null)
        }
    }

    @Test
    fun `Get goals returns error when userId is blank`() = runTest {
        repository.getGoals(GoalReadWriteOperation.FIREBASE_DATABASE)
        fakeGoalRemoteDataSource.getGoals(userId = "") { throwable, goals ->
            assertEquals(goals, emptyList<Goal>())
            assertEquals(throwable?.message, "UserId is blank")
        }
    }

    @Test
    fun `Get goal with valid id returns expected goal`() = runTest {
        val userId = 2
        repository.getGoal(userId, GoalReadWriteOperation.FIREBASE_DATABASE)
        fakeGoalRemoteDataSource.getGoal(userId) { throwable, goal ->
            assertEquals(goal, FAKE_GOAL)
            assertEquals(throwable, null)
        }
    }

    @Test
    fun `Get goal with invalid id fails with error message`() = runTest {
        val id = -1
        repository.getGoal(id, GoalReadWriteOperation.FIREBASE_DATABASE)
        fakeGoalRemoteDataSource.getGoal(id) { throwable, goal ->
            assertEquals(goal, null)
            assertEquals(throwable?.message, "Id is not set")
        }
    }

    @Test
    fun `Update goal returns true if goal is not empty`() = runTest {
        repository.updateGoal(FAKE_GOAL, GoalReadWriteOperation.FIREBASE_DATABASE) { isSuccess ->
            assertThat(isSuccess).isTrue()
        }
    }

    @Test
    fun `Update goal returns error if goal is empty`() = runTest {
        repository.updateGoal(Goal.EMPTY, GoalReadWriteOperation.FIREBASE_DATABASE) { isSuccess ->
            assertThat(isSuccess).isFalse()
        }
    }
}