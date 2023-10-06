package com.kamus.cookit.data.db

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.kamus.cookit.data.db.model.ArrayListConverter
import com.kamus.cookit.data.db.model.RecipeEntity
import com.kamus.cookit.utils.Constants

@Database(
    entities = [RecipeEntity::class],
    version = 1,
    exportSchema = true
)
@TypeConverters(
    ArrayListConverter::class
)
abstract class RecipeDatabase: RoomDatabase() {

    abstract fun recipeDao(): RecipeDao

    companion object{
        @Volatile
        private var INSTANCE: RecipeDatabase? = null

        fun getDatabase(context: Context): RecipeDatabase{
            return INSTANCE?: synchronized(this){
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    RecipeDatabase::class.java,
                    Constants.DATABASE_NAME
                ).fallbackToDestructiveMigration()
                    .build()
                INSTANCE = instance
                instance
            }
        }
    }
}