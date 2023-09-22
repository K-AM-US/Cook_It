package com.kamus.cookit.data.db.model

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.kamus.cookit.utils.Constants

@Entity(tableName = Constants.DATABASE_RECIPE_TABLE)
data class RecipeEntity(
    @PrimaryKey(autoGenerate = false)
    @ColumnInfo(name = "recipe_id")
    var id: Long,

    @ColumnInfo(name = "recipe_title")
    var title: String,

    @ColumnInfo(name = "recipe_ingredients")
    var ingredients: String,

    @ColumnInfo(name = "recipe_process")
    var process: String
)
