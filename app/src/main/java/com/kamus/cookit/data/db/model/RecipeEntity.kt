package com.kamus.cookit.data.db.model

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import androidx.room.TypeConverter
import androidx.room.TypeConverters
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import com.kamus.cookit.utils.Constants

@Entity(tableName = Constants.DATABASE_RECIPE_TABLE)
data class RecipeEntity(
    @PrimaryKey(autoGenerate = false)
    @ColumnInfo(name = "recipe_id")
    var id: Long,
    @ColumnInfo(name = "recipe_title")
    var title: String,
    @ColumnInfo(name = "recipe_ingredients")
    @TypeConverters(ArrayListConverter::class)
    var ingredients: ArrayList<String>,
    @ColumnInfo(name = "recipe_process")
    @TypeConverters(ArrayListConverter::class)
    var process: ArrayList<String>,
    @ColumnInfo(name = "img")
    var img: String
    /*@ColumnInfo(name = "type")
    var type: String,
    @ColumnInfo(name = "tags")
    var tags: ArrayList<String>,

    @ColumnInfo(name = "favourite")
    var favourite: Boolean*/
)

class ArrayListConverter{
    @TypeConverter
    fun fromString(value: String): ArrayList<String>{
        val listType = object:TypeToken<ArrayList<String>>(){}.type
        return Gson().fromJson(value, listType)
    }

    @TypeConverter
    fun fromArrayList(list: ArrayList<String>): String = Gson().toJson(list)
}