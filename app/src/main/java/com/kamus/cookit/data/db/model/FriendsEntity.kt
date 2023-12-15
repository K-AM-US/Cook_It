package com.kamus.cookit.data.db.model

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.kamus.cookit.utils.Constants

@Entity(tableName = Constants.DATABASE_FRIENDS, primaryKeys = ["friend_id", "user_id"])
data class FriendsEntity(
    @ColumnInfo(name = "friend_id")
    val friendId: String,
    @ColumnInfo(name = "user_id")
    val userId: String
)
