package com.mind.market.aimissioncompose.presentation.utils

import androidx.room.TypeConverter
import com.example.aimissionlite.models.domain.Status
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
}