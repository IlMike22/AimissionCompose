package com.mind.market.aimissioncompose.data

import androidx.room.TypeConverter
import com.example.aimissionlite.models.domain.Genre
import com.mind.market.aimissioncompose.domain.models.Priority
import com.example.aimissionlite.models.domain.Status

class Converters {
    @TypeConverter
    fun Genre.toGenreData(): String {
        return when (this) {
            Genre.PARTNERSHIP -> "PARTNERSHIP"
            Genre.BUSINESS -> "BUSINESS"
            Genre.FITNESS -> "FITNESS"
            Genre.MONEY -> "MONEY"
            Genre.SOCIALISING -> "SOCIALISING"
            Genre.HEALTH -> "HEALTH"
            Genre.UNKNOWN -> "UNKNOWN"
        }
    }

    @TypeConverter
    fun String.toDomainGenre(): Genre =
        when (this) {
            "PARTNERSHIP" -> Genre.PARTNERSHIP
            "BUSINESS" -> Genre.BUSINESS
            "FITNESS" -> Genre.FITNESS
            "MONEY" -> Genre.MONEY
            "HEALTH" -> Genre.HEALTH
            "SOCIALISING" -> Genre.SOCIALISING
            else -> Genre.UNKNOWN
        }

    @TypeConverter
    fun Status.toStatusData(): String {
        return when (this) {
            Status.TODO -> "TODO"
            Status.IN_PROGRESS -> "IN_PROGRESS"
            Status.DONE -> "DONE"
            Status.DEPRECATED -> "DEPRECATED"
            Status.UNKNOWN -> "UNKNOWN"
        }
    }

    @TypeConverter
    fun String.toDomainStatus(): Status =
        when (this) {
            "TODO" -> Status.TODO
            "IN_PROGRESS" -> Status.IN_PROGRESS
            "DONE" -> Status.DONE
            "DEPRECATED" -> Status.DEPRECATED
            else -> Status.UNKNOWN
        }

    @TypeConverter
    internal fun Priority.toPriorityData(): String {
        return when (this) {
            Priority.HIGH -> "HIGH"
            Priority.NORMAL -> "NORMAL"
            Priority.LOW -> "LOW"
            else -> "UNKNOWN"
        }
    }

    @TypeConverter
    fun String.toDomainPriority(): Priority =
        when (this) {
            "LOW" -> Priority.LOW
            "NORMAL" -> Priority.NORMAL
            "HIGH"-> Priority.HIGH
            else -> Priority.UNKNOWN
        }

    companion object {
        @TypeConverter
        fun Genre.toGenreId():Int {
            return when (this) {
                Genre.UNKNOWN -> -1
                Genre.PARTNERSHIP -> 0
                Genre.BUSINESS -> 1
                Genre.FITNESS -> 2
                Genre.MONEY -> 3
                Genre.SOCIALISING -> 4
                Genre.HEALTH -> 5
            }
        }

        @TypeConverter
        fun Priority.toPriorityId():Int {
            return when (this) {
                Priority.UNKNOWN -> -1
                Priority.HIGH -> 0
                Priority.NORMAL -> 1
                Priority.LOW -> 2
            }
        }
    }
}