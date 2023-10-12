package com.kamus.cookit.data.db

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import com.kamus.cookit.data.db.model.FavouriteRecipeEntity
import com.kamus.cookit.data.db.model.RecipeEntity
import com.kamus.cookit.utils.Constants

@Dao
interface FavouriteRecipeDao {
    @Insert
    suspend fun insertFavouriteRecipe(recipe: FavouriteRecipeEntity)

    @Query("SELECT * FROM ${Constants.DATABASE_FAVOURITE_RECIPE_TABLE}")
    suspend fun getFavouriteRecipes(): List<FavouriteRecipeEntity>
}