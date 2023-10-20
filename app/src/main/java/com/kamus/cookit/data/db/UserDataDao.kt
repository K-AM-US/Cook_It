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

    @Query("UPDATE user_data_table SET username = :username WHERE user_id = 0")
    suspend fun updateUsername(username: String)

    @Query("UPDATE user_data_table SET user_password = :password WHERE user_id = 0")
    suspend fun updatePassword(password: String)

    @Query("UPDATE user_data_table SET user_email = :email WHERE user_id = 0")
    suspend fun updateEmail(email: String)

    @Query("UPDATE user_data_table SET user_phone = :phone WHERE user_id = 0")
    suspend fun updatePhone(phone: String)

    @Query("UPDATE user_data_table SET friends = :friends where user_id = 0")
    suspend fun updateFriends(friends: ArrayList<String>)
}