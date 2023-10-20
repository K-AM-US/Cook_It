package com.kamus.cookit.data.db.model

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import androidx.room.TypeConverters
import com.kamus.cookit.utils.Constants

@Entity(tableName = Constants.DATABASE_USER_DATA_TABLE)
data class UserDataEntity(
    @PrimaryKey(autoGenerate = false)
    @ColumnInfo(name = "user_id")
    val id: Long = 0,
    @ColumnInfo(name = "username")
    var username: String,
    @ColumnInfo(name = "user_password")
    var password: String,
    @ColumnInfo(name = "user_first_name")
    var firstName: String,
    @ColumnInfo(name = "user_last_name")
    var lastName: String,
    @ColumnInfo(name = "user_email")
    var email: String,
    @ColumnInfo(name = "user_phone")
    var phone: String,
    @TypeConverters(ArrayListConverter::class)
    @ColumnInfo(name = "friends")
    var friends: ArrayList<String>
)


