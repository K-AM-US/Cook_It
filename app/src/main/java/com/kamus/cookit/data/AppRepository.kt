package com.kamus.cookit.data

import com.kamus.cookit.data.db.RecipeDao
import com.kamus.cookit.data.db.model.RecipeEntity

/* TODO: add friend Dao */
class AppRepository(private val recipeDao: RecipeDao) {

    suspend fun insertRecipe(recipe: RecipeEntity){
        recipeDao.insertRecipe(recipe)
    }

    suspend fun getRecipes(): List<RecipeEntity> = recipeDao.getRecipes()

    suspend fun updateRecipe(recipe: RecipeEntity){
        recipeDao.updateRecipe(recipe)
    }

    suspend fun deleteRecipe(recipe: RecipeEntity){
        recipeDao.deleteRecipe(recipe)
    }
}