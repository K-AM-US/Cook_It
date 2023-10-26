package com.kamus.cookit.data.db

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update
import com.kamus.cookit.data.db.model.UserDataEntity
import com.kamus.cookit.utils.Constants.DATABASE_USER_DATA_TABLE

@Dao
interface UserDataDao {
    @Insert
    suspend fun insertData(user: UserDataEntity)

    @Query("SELECT * FROM $DATABASE_USER_DATA_TABLE")
    suspend fun getData(): UserDataEntity

    @Update
    suspend fun updateData(user: UserDataEntity)

    @Query("UPDATE user_data_table SET username = :username")
    suspend fun updateUsername(username: String)

    @Query("UPDATE user_data_table SET user_password = :password ")
    suspend fun updatePassword(password: String)

    @Query("UPDATE user_data_table SET user_email = :email")
    suspend fun updateEmail(email: String)

    @Query("UPDATE user_data_table SET user_phone = :phone ")
    suspend fun updatePhone(phone: String)
}