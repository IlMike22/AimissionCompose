package com.mind.market.aimissioncompose.statistics.data.dto

import androidx.room.Entity
import androidx.room.PrimaryKey


@Entity(tableName = "statistics_table")
data class StatisticsEntityDto(
    @PrimaryKey val id: String = "",
    val title: String = "",
    val amountGoalsCompleted: Int = -1,
    val amountGoalsCreated: Int = -1,
    val amountGoalsStarted: Int = -1,
    val amountGoalsNotCompleted: Int = -1,
    val grade: Grade = Grade.UNDEFINED,
    val month: Int = -1,
    val year: Int = -1,
    val lastUpdated: String = "",
    val created: String = ""
) {

    override fun equals(other: Any?): Boolean {
        if (other is StatisticsEntityDto) {
            return this.id == other.id &&
                    this.title == other.title &&
                    this.amountGoalsCreated == other.amountGoalsCreated &&
                    this.amountGoalsCompleted == other.amountGoalsCompleted &&
                    this.amountGoalsStarted == other.amountGoalsStarted &&
                    this.amountGoalsNotCompleted == other.amountGoalsNotCompleted &&
                    this.grade == other.grade &&
                    this.month == other.month &&
                    this.year == other.year &&
                    this.lastUpdated == other.lastUpdated &&
                    this.created == other.created
        }
        return super.equals(other)
    }
}

enum class Grade {
    ALL_GOALS_COMPLETED,
    NEARLY_ALL_GOALS_COMPLETED,
    SOME_GOALS_COMPLETED,
    FEW_GOALS_COMPLETED,
    NO_GOALS_COMPLETED_YET,
    NO_GOALS_ADDED_YET,
    DEPRECATED_GOALS_EXIST,
    UNDEFINED
}
