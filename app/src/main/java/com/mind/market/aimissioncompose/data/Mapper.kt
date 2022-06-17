package com.mind.market.aimissioncompose.data

import com.example.aimissionlite.models.domain.Status

fun Status.toStatusData(): String {
    return when (this) {
        Status.TODO -> "TODO"
        Status.IN_PROGRESS -> "IN_PROGRESS"
        Status.DONE -> "DONE"
        Status.DEPRECATED -> "DEPRECATED"
        Status.UNKNOWN -> "UNKNOWN"
    }
}