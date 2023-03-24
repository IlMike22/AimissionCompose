package com.mind.market.aimissioncompose.presentation.utils

import android.content.Context
import androidx.room.TypeConverter
import com.mind.market.aimissioncompose.R
import com.mind.market.aimissioncompose.domain.models.Genre
import com.mind.market.aimissioncompose.domain.models.Priority
import com.mind.market.aimissioncompose.domain.models.Status
import com.mind.market.aimissioncompose.domain.models.ValidationStatusCode
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter

object Converters {
    fun String.toGenre(): Genre =
        when (this) {
            "PARTNERSHIP" -> Genre.PARTNERSHIP
            "BUSINESS" -> Genre.BUSINESS
            "FITNESS" -> Genre.FITNESS
            "MONEY" -> Genre.MONEY
            "HEALTH" -> Genre.HEALTH
            "SOCIALISING" -> Genre.SOCIALISING
            "NOT_SPECIFIED" -> Genre.NOT_SPECIFIED
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
            Genre.NOT_SPECIFIED -> R.drawable.genre_unknown
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

    fun SortingMode.toText(context: Context): String =
        when (this) {
            SortingMode.SORT_BY_GOALS_IN_PROGRESS -> context.getString(R.string.sort_mode_by_goals_in_progress)
            SortingMode.SORT_BY_GOALS_IN_TODO -> context.getString(R.string.sort_mode_by_goals_in_todo)
            SortingMode.SORT_BY_GOALS_COMPLETED -> context.getString(R.string.sort_mode_by_goals_completed)
            SortingMode.SORT_BY_GOALS_DEPRECATED -> context.getString(R.string.sort_mode_by_goals_deprecated)
        }

    fun ValidationStatusCode.toText(context: Context) =
        when (this) {
            ValidationStatusCode.NO_TITLE -> context.getString(R.string.detail_validation_error_message_no_title)
            ValidationStatusCode.NO_DESCRIPTION -> context.getString(R.string.detail_validation_error_message_no_description)
            ValidationStatusCode.NO_GENRE -> context.getString(R.string.detail_validation_error_message_no_genre)
            ValidationStatusCode.NO_PRIORITY -> context.getString(R.string.detail_validation_error_message_no_priority)
            ValidationStatusCode.DUE_DATE_IS_IN_PAST -> context.getString(R.string.detail_validation_error_message_goal_date_invalid)
            else -> context.getString(R.string.detail_validation_error_message_unknown)
        }

    fun LocalDateTime.toText():String {
        val pattern = "dd.MM.yyyy"
        val formatter = DateTimeFormatter.ofPattern(pattern)
        return this.format(formatter)
    }
}