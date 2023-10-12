package com.kamus.cookit.data.db.model

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import androidx.room.TypeConverters
import com.kamus.cookit.utils.Constants

@Entity(tableName = Constants.DATABASE_FAVOURITE_RECIPE_TABLE)
data class FavouriteRecipeEntity(
    @PrimaryKey(autoGenerate = false)
    @ColumnInfo(name = "recipe_id")
    val id: Long,
    @ColumnInfo(name = "recipe_title")
    var title: String,
    @ColumnInfo(name = "recipe_ingredients")
    @TypeConverters(ArrayListConverter::class)
    var ingredients: ArrayList<String>,
    @ColumnInfo(name = "recipe_process")
    @TypeConverters(ArrayListConverter::class)
    var process: ArrayList<String>,
)
