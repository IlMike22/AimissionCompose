package com.mind.market.aimissioncompose.presentation.utils

import androidx.room.TypeConverter
import com.mind.market.aimissioncompose.R
import com.mind.market.aimissioncompose.domain.models.Status
import com.mind.market.aimissioncompose.domain.models.Genre
import com.mind.market.aimissioncompose.domain.models.Priority

object Converters {

    fun String.toGenre(): Genre =
        when (this) {
            "PARTNERSHIP" -> Genre.PARTNERSHIP
            "BUSINESS" -> Genre.BUSINESS
            "FITNESS" -> Genre.FITNESS
            "MONEY" -> Genre.MONEY
            "HEALTH" -> Genre.HEALTH
            "SOCIALISING" -> Genre.SOCIALISING
            else -> Genre.UNKNOWN
        }

    fun String.toPriority(): Priority =
        when (this) {
            "LOW" -> Priority.LOW
            "MEDIUM" -> Priority.MEDIUM
            "HIGH" -> Priority.HIGH
            else -> Priority.UNKNOWN
        }

    @TypeConverter
    fun String.toStatus(): Status =
        when (this) {
            "TODO" -> Status.TODO
            "IN_PROGRESS" -> Status.IN_PROGRESS
            "DONE" -> Status.DONE
            "DEPRECATED" -> Status.DEPRECATED
            else -> Status.UNKNOWN
        }

    fun getGenreIcon(genre: Genre): Int {
        return when (genre) {
            Genre.BUSINESS -> R.drawable.genre_business
            Genre.FITNESS -> R.drawable.genre_fittness
            Genre.MONEY -> R.drawable.genre_money
            Genre.PARTNERSHIP -> R.drawable.genre_partnership
            Genre.SOCIALISING -> R.drawable.genre_socialising
            Genre.HEALTH -> R.drawable.genre_health
            Genre.UNKNOWN -> R.drawable.genre_unknown
        }
    }

    fun getStatusIcon(status: Status): Int {
        return when (status) {
            Status.IN_PROGRESS -> R.drawable.status_in_progress
            Status.TODO -> R.drawable.status_todo
            Status.DONE -> R.drawable.status_done
            else -> R.drawable.status_overdue
        }
    }



    fun getPriorityIcon(priority: Priority): Int {
        return when (priority) {
            Priority.LOW -> R.drawable.priority_low
            Priority.MEDIUM -> R.drawable.unknown
            Priority.HIGH -> R.drawable.priority_high
            Priority.UNKNOWN -> R.drawable.unknown
        }
    }
}