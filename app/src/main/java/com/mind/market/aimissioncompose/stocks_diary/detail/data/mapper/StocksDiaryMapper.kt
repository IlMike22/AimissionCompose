package com.mind.market.aimissioncompose.stocks_diary.detail.data.mapper

import com.mind.market.aimissioncompose.stocks_diary.detail.data.StocksDiaryData
import com.mind.market.aimissioncompose.stocks_diary.detail.domain.models.StocksDiaryDomain
import com.mind.market.aimissioncompose.stocks_diary.detail.presentation.Mood
import java.time.LocalDate
import java.time.format.DateTimeFormatter
import kotlin.random.Random

fun StocksDiaryDomain.toStocksDiaryData(): StocksDiaryData =
    StocksDiaryData(
        id = id,
        title = this.title,
        description = this.description,
        mood = this.mood.toMoodData(),
        createdDate = "${getCorrectDayOfMonth(this.createdDate.dayOfMonth)}.${this.createdDate.monthValue}.${this.createdDate.year}"
    )

private fun getCorrectDayOfMonth(day: Int) =
    if (day < 10) {
        "0$day"
    } else {
        day.toString()
    }

fun StocksDiaryDomain.addUniqueId(): StocksDiaryDomain =
    StocksDiaryDomain(
        id = Random.nextInt(0, 10_000),
        title = title,
        description = description,
        mood = mood
    )

fun StocksDiaryData.toDomain() =
    StocksDiaryDomain(
        id = id,
        title = title,
        description = description,
        mood = mood.toMoodDomain(),
        createdDate = createdDate.toDate()
    )

fun Int.toMoodDomain() =
    when (this) {
        0 -> Mood.GOOD
        1 -> Mood.OKAY
        2 -> Mood.BAD
        else -> Mood.UNDEFINED
    }

fun String.toDate(): LocalDate {
    val pattern = "dd.MM.yyyy"
    val formatter = DateTimeFormatter.ofPattern(pattern)
    return LocalDate.parse(this, formatter)
}

fun Mood.toMoodData(): Int =
    when (this) {
        Mood.GOOD -> 0
        Mood.OKAY -> 1
        Mood.BAD -> 2
        Mood.UNDEFINED -> -1
    }