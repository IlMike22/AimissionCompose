package com.mind.market.aimissioncompose.auth.data.model

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "user_table")
data class UserDto(
    @PrimaryKey(autoGenerate = true) val id: Int?,
    val tenantId: String = "",
    val name: String = "",
    val email: String = ""
)
