package com.mind.market.aimissioncompose.data

import android.content.Context
import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import com.mind.market.aimissioncompose.data.SettingsLocalDataSource.PreferencesKeys.DELETE_GOALS_ON_STARTUP
import com.mind.market.aimissioncompose.data.SettingsLocalDataSource.PreferencesKeys.HIDE_DONE_GOALS
import com.mind.market.aimissioncompose.data.SettingsLocalDataSource.PreferencesKeys.SHOW_GOAL_OVERDUE_DIALOG_ON_STARTUP
import com.mind.market.aimissioncompose.data.SettingsLocalDataSource.PreferencesKeys.SORTING_MODE
import com.mind.market.aimissioncompose.data.settings.repository.SettingEntries
import com.mind.market.aimissioncompose.presentation.utils.SortingMode
import kotlinx.coroutines.flow.map

data class SettingsLocalDataSource(
    val context: Context,
    private val goalDao: IGoalDao
) {
    private val Context.dataStore by preferencesDataStore(
        name = USER_SETTINGS_NAME
    )

    suspend fun duplicateGoals(): Boolean {
        try {
            val goals = goalDao.getGoals()
            goals.forEach { goalDto ->
                goalDao.insert(goalDto)
            }
        } catch (exception: Exception) {
            println("!! some crash while duplicating the goal")
            return false
        }
        return true
    }

    suspend fun setDeleteGoalsOnStartup(enabled: Boolean) {
        context.dataStore.edit { preferences ->
            preferences[DELETE_GOALS_ON_STARTUP] = enabled
            println("!!! value was successfully set in datastore.")
        }
    }

    suspend fun setHideDoneGoals(isHide: Boolean) {
        context.dataStore.edit { preferences ->
            preferences[HIDE_DONE_GOALS] = isHide
        }
    }

    suspend fun setSortingMode(mode: SortingMode) {
        context.dataStore.edit { preferences ->
            preferences[SORTING_MODE] = mode.toData()
        }
    }

    suspend fun showGoalOverdueDialog(show: Boolean) {
        context.dataStore.edit { preferences ->
            preferences[SHOW_GOAL_OVERDUE_DIALOG_ON_STARTUP] = show
        }
    }

    fun getUserSettings() = context.dataStore.data.map { preferences ->
        SettingEntries(
            isHideDoneGoals = preferences[HIDE_DONE_GOALS] ?: false,
            showGoalOverdueDialog = preferences[SHOW_GOAL_OVERDUE_DIALOG_ON_STARTUP] ?: false,
            selectedSortingMode = preferences[SORTING_MODE]?.toSortingMode()
        )
    }

    private object PreferencesKeys {
        val DELETE_GOALS_ON_STARTUP = booleanPreferencesKey("delete_goals_on_startup")
        val HIDE_DONE_GOALS = booleanPreferencesKey("hide_done_goals")
        val SHOW_GOAL_OVERDUE_DIALOG_ON_STARTUP = booleanPreferencesKey("show_goal_overdue_dialog")
        val SORTING_MODE = stringPreferencesKey("sort_mode")
    }

    private fun SortingMode.toData(): String =
        when (this) {
            SortingMode.SORT_BY_GOALS_IN_PROGRESS -> "sort_by_in_progress"
            SortingMode.SORT_BY_GOALS_IN_TODO -> "sort_by_in_todo"
            SortingMode.SORT_BY_GOALS_COMPLETED -> "sort_by_completed"
            SortingMode.SORT_BY_GOALS_DEPRECATED -> "sort_by_deprecated"
        }

    private fun String.toSortingMode(): SortingMode? =
        when (this) {
            "sort_by_in_progress" -> SortingMode.SORT_BY_GOALS_IN_PROGRESS
            "sort_by_in_todo" -> SortingMode.SORT_BY_GOALS_IN_TODO
            "sort_by_completed" -> SortingMode.SORT_BY_GOALS_COMPLETED
            "sort_by_deprecated" -> SortingMode.SORT_BY_GOALS_DEPRECATED
            else -> null
        }

    companion object {
        private const val USER_SETTINGS_NAME = "user_settings"
    }
}
