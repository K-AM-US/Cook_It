package com.kamus.cookit.data.db.model

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.kamus.cookit.utils.Constants

@Entity(tableName = Constants.DATABASE_FRIENDS)
data class FriendsEntity(
    @PrimaryKey(autoGenerate = false)
    @ColumnInfo(name = "friend_id")
    var friendId: String
)
