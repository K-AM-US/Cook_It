package com.kamus.cookit.data.db

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update
import com.kamus.cookit.data.db.model.UserDataEntity

@Dao
interface UserDataDao {
    @Insert
    suspend fun insertUser(user: UserDataEntity)

    @Query("SELECT * FROM USER_DATA_TABLE WHERE uid = :uid")
    suspend fun getUser(uid: String): UserDataEntity

    @Update
    suspend fun updateUser(recipe: UserDataEntity)
}