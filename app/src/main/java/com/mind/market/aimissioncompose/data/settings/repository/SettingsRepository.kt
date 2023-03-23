package com.mind.market.aimissioncompose.data.settings.repository

import com.mind.market.aimissioncompose.core.Resource
import com.mind.market.aimissioncompose.data.IGoalDao
import com.mind.market.aimissioncompose.data.SettingsLocalDataSource
import com.mind.market.aimissioncompose.domain.models.Status
import com.mind.market.aimissioncompose.presentation.utils.SortingMode
import com.mind.market.aimissioncompose.statistics.presentation.SortMode
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow

class SettingsRepository(
    private val localDataSource: SettingsLocalDataSource,
    private val goalDao: IGoalDao
) : ISettingsRepository {
    override suspend fun setDeleteGoalsOnStartup(enabled: Boolean) {
        localDataSource.setDeleteGoalsOnStartup(enabled)
    }

    override suspend fun duplicateGoals(): Boolean {
        return localDataSource.duplicateGoals()
    }

    override suspend fun setHideDoneGoals(isHide: Boolean) {
        localDataSource.setHideDoneGoals(isHide)
    }

    override suspend fun showGoalOverdueDialog(show: Boolean) {
        localDataSource.showGoalOverdueDialog(show)
    }

    override fun getUserSettings(): Flow<SettingEntries> {
        return localDataSource.getUserSettings()
    }

    override suspend fun getAmountGoalsForStatus(status: Status): Flow<Resource<Int>> {
        return flow {
            emit(Resource.Loading(true))
            try {
                val result = goalDao.getAmountGoalsForStatus(status)
                emit(Resource.Success(result))
            } catch (exception: Exception) {
                emit(Resource.Error(message = "failed to load statistics from db"))
            }
        }
    }

    override suspend fun setSortingMode(mode: SortingMode) {
        localDataSource.setSortingMode(mode)
    }
}

data class SettingEntries(
    val isHideDoneGoals: Boolean,
    val showGoalOverdueDialog: Boolean,
    val selectedSortingMode: SortingMode?
)