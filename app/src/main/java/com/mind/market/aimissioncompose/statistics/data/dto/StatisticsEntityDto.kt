package com.mind.market.aimissioncompose.statistics.data.dto

import androidx.room.Entity
import androidx.room.PrimaryKey


@Entity(tableName = "statistics_table")
data class StatisticsEntityDto(
    @PrimaryKey val id: String,
    val title: String,
    val amountGoalsCompleted: Int,
    val amountGoalsCreated: Int,
    val amountGoalsStarted: Int,
    val amountGoalsNotCompleted: Int,
    val grade: Grade,
    val month: Int,
    val year: Int,
    val lastUpdated: String,
    val created: String
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

//    override fun hashCode(): Int {
//        var result = id
//        result = 31 * result + title.hashCode()
//        result = 31 * result + amountGoalsCreated.hashCode()
//        result = 31 * result + amountGoalsCompleted.hashCode()
//        result = 31 * result + amountGoalsStarted.hashCode()
//        result = 31 * result + amountGoalsNotCompleted.hashCode()
//        result = 31 * result + grade.hashCode()
//        result = 31 * result + month.hashCode()
//        result = 31 * result + year.hashCode()
//        result = 31 * result + lastUpdated.hashCode()
//        result = 31 * result + created.hashCode()
//
//        return result
//    }
}

enum class Grade {
    ALL_GOALS_COMPLETED,
    NEARLY_ALL_GOALS_COMPLETED,
    SOME_GOALS_COMPLETED,
    FEW_GOALS_COMPLETED,
    NO_GOALS_COMPLEDTED,
    UNDEFINED
}
