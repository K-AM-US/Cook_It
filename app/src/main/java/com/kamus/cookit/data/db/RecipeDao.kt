package com.kamus.cookit.data.db

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update
import com.kamus.cookit.data.db.model.RecipeEntity
import com.kamus.cookit.utils.Constants.DATABASE_RECIPE_TABLE

@Dao
interface RecipeDao {
    /* CREATE */
    @Insert
    suspend fun insertRecipe(recipe: RecipeEntity)

    /* READ */
    @Query("SELECT * FROM $DATABASE_RECIPE_TABLE")
    suspend fun getRecipes(): List<RecipeEntity>

    /* UPDATE */
    @Update
    suspend fun updateRecipe(recipe: RecipeEntity)

    /* DELETE */
    @Delete
    suspend fun deleteRecipe(recipe: RecipeEntity)
}