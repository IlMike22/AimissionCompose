package com.mind.market.aimissioncompose.auth.data

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy.REPLACE
import androidx.room.Query
import com.mind.market.aimissioncompose.auth.data.model.UserDto

@Dao
interface IAuthenticationDao {
    @Insert(onConflict = REPLACE)
    suspend fun insert(user: UserDto)

    @Query("SELECT * FROM user_table WHERE tenantId = :tenantId")
    suspend fun getFirebaseUser(tenantId: String): UserDto
}