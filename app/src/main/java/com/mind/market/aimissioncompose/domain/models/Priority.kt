package com.mind.market.aimissioncompose.domain.models

enum class Priority(var value: String) : IChipGroupValue {
    LOW("LOW"),
    MEDIUM("MEDIUM"),
    HIGH("HIGH"),
    UNKNOWN("UNKNOWN")
}

fun getPriorities(): List<Priority> {
    return listOf(Priority.LOW, Priority.MEDIUM, Priority.HIGH)
}

fun getPriority(value: String): Priority? {
    val map = Priority.values().associateBy(Priority::value)
    return map[value]
}