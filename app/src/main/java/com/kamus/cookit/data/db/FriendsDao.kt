package com.kamus.cookit.data.db

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import com.kamus.cookit.data.db.model.FriendsEntity
import com.kamus.cookit.utils.Constants.DATABASE_FRIENDS

@Dao
interface FriendsDao {
    @Insert
    suspend fun insertFriend(friend: FriendsEntity)

    @Delete
    suspend fun deleteFriend(friend: FriendsEntity)

    @Query("SELECT * FROM $DATABASE_FRIENDS WHERE friend_id = :friend")
    suspend fun getFriend(friend: String): FriendsEntity
}