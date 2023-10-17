package com.kamus.cookit.data

import com.kamus.cookit.data.db.FavouriteRecipeDao
import com.kamus.cookit.data.db.RecipeDao
import com.kamus.cookit.data.db.model.FavouriteRecipeEntity
import com.kamus.cookit.data.db.model.RecipeEntity
import com.kamus.cookit.data.remote.model.CategoriesDto
import com.kamus.cookit.data.remote.RecipeApi
import com.kamus.cookit.data.remote.model.RecipeDetailDto
import com.kamus.cookit.data.remote.model.RecipeDto
import com.kamus.cookit.data.remote.model.UserDto
import retrofit2.Call
import retrofit2.Retrofit

/* TODO: add friend Dao */
class AppRepository(private val recipeDao: RecipeDao, private val favouriteRecipeDao: FavouriteRecipeDao, private val retrofit: Retrofit) {

    private val recipeApi: RecipeApi = retrofit.create(RecipeApi::class.java)

    // Room
    suspend fun insertRecipe(recipe: RecipeEntity){
        recipeDao.insertRecipe(recipe)
    }
    suspend fun getRecipes(): List<RecipeEntity> = recipeDao.getRecipes()

    suspend fun getRecipeByID(id: String?): RecipeEntity = recipeDao.getRecipeById(id)

    suspend fun updateRecipe(recipe: RecipeEntity){
        recipeDao.updateRecipe(recipe)
    }

    suspend fun deleteRecipe(recipe: RecipeEntity){
        recipeDao.deleteRecipe(recipe)
    }

    /* Favourite Recipe */
    suspend fun insertFavouriteRecipe(recipe: FavouriteRecipeEntity){
        favouriteRecipeDao.insertFavouriteRecipe(recipe)
    }

    suspend fun getFavouriteRecipes(): List<FavouriteRecipeEntity> =
        favouriteRecipeDao.getFavouriteRecipes()

    suspend fun getFavouriteRecipeById(id: String): FavouriteRecipeEntity =
        favouriteRecipeDao.getFavouriteRecipeById(id)

    suspend fun deleteFavouriteRecipe(recipe: FavouriteRecipeEntity) {
        favouriteRecipeDao.deleteFavouriteRecipe(recipe)
    }

    suspend fun updateFavouriteRecipe(recipe: FavouriteRecipeEntity) {
        favouriteRecipeDao.updateFavouriteRecipe(recipe)
    }

    // Retrofit
    fun getCategories(): Call<List<CategoriesDto>> =
        recipeApi.getCategories()

    fun getHomeRecipes(): Call<List<RecipeDto>> =
        recipeApi.getHomeRecipes()

    fun getUsers(): Call<List<UserDto>> =
        recipeApi.getUsers()

    fun getRecipeDetail(id: String?): Call<RecipeDetailDto> =
        recipeApi.getRecipeDetail(id)
}