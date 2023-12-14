package com.kamus.cookit.data.db.model

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.kamus.cookit.utils.Constants

@Entity(tableName = Constants.DATABASE_USER_DATA_TABLE)
data class UserDataEntity(
    @PrimaryKey(autoGenerate = false)
    @ColumnInfo(name = "uid")
    val uid: String,
    @ColumnInfo(name = "fullname")
    var fullname: String,
    @ColumnInfo(name = "username")
    var username: String,
)
