package com.mind.market.aimissioncompose.data

import android.content.Context
import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.preferencesDataStore
import com.mind.market.aimissioncompose.data.SettingsLocalDataSource.PreferencesKeys.DELETE_GOALS_ON_STARTUP
import com.mind.market.aimissioncompose.data.SettingsLocalDataSource.PreferencesKeys.HIDE_DONE_GOALS
import com.mind.market.aimissioncompose.data.SettingsLocalDataSource.PreferencesKeys.SHOW_GOAL_OVERDUE_DIALOG_ON_STARTUP
import com.mind.market.aimissioncompose.data.settings.repository.SettingEntries
import kotlinx.coroutines.flow.Flow
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

    suspend fun showGoalOverdueDialog(show: Boolean) {
        context.dataStore.edit { preferences ->
            preferences[SHOW_GOAL_OVERDUE_DIALOG_ON_STARTUP] = show
        }
    }

    fun getUserSettings() = context.dataStore.data.map { preferences ->
        SettingEntries(
            isHideDoneGoals = preferences[HIDE_DONE_GOALS] ?: false,
            showGoalOverdueDialog = preferences[SHOW_GOAL_OVERDUE_DIALOG_ON_STARTUP] ?: false,
        )
    }

//    fun getDeleteGoalsOnStartup(): Flow<Boolean> =
//        context.dataStore.data.map { preferences ->
//            preferences[DELETE_GOALS_ON_STARTUP] ?: false
//        }

    private object PreferencesKeys {
        val DELETE_GOALS_ON_STARTUP = booleanPreferencesKey("delete_goals_on_startup")
        val HIDE_DONE_GOALS = booleanPreferencesKey("hide_done_goals")
        val SHOW_GOAL_OVERDUE_DIALOG_ON_STARTUP = booleanPreferencesKey("show_goal_overdue_dialog")
    }

    companion object {
        private const val USER_SETTINGS_NAME = "user_settings"
    }
}
