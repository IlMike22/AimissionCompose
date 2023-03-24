package com.mind.market.aimissioncompose.domain.models

enum class Genre(var value: String) : IChipGroupValue {
    BUSINESS("BUSINESS"),
    FITNESS("FITNESS"),
    MONEY("MONEY"),
    PARTNERSHIP("PARTNERSHIP"),
    SOCIALISING("SOCIALISING"),
    HEALTH("HEALTH"),
    NOT_SPECIFIED("NOT_SPECIFIED"),
    UNKNOWN("UNKNOWN")
}

fun getGenres(): List<Genre> {
    return listOf(
        Genre.BUSINESS,
        Genre.FITNESS,
        Genre.MONEY,
        Genre.PARTNERSHIP,
        Genre.SOCIALISING,
        Genre.HEALTH,
        Genre.NOT_SPECIFIED
    )
}

fun getGenre(value: String): Genre? {
    val map = Genre.values().associateBy(Genre::value)
    return map[value]
}