package com.mind.market.aimissioncompose.data.goal

import com.mind.market.aimissioncompose.domain.models.Genre
import com.mind.market.aimissioncompose.domain.models.Goal
import com.mind.market.aimissioncompose.domain.models.Priority
import com.mind.market.aimissioncompose.domain.models.Status
import java.time.LocalDateTime
import java.time.Month

val FAKE_GOAL = Goal(
    id = 2,
    title = "test goal",
    description = "test description",
    creationDate = LocalDateTime.of(2023, Month.APRIL,22,20,12,23),
    changeDate =  LocalDateTime.of(2023, Month.APRIL,22,20,12,23),
    isRepeated = false,
    genre = Genre.BUSINESS,
    status = Status.IN_PROGRESS,
    priority = Priority.HIGH,
    finishDate = LocalDateTime.of(2023, Month.APRIL,25,20,12,23)
)