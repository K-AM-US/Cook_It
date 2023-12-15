package com.kamus.cookit.data

import com.kamus.cookit.data.db.FavouriteRecipeDao
import com.kamus.cookit.data.db.FriendsDao
import com.kamus.cookit.data.db.RecipeDao
import com.kamus.cookit.data.db.UserDataDao
import com.kamus.cookit.data.db.model.FavouriteRecipeEntity
import com.kamus.cookit.data.db.model.FriendsEntity
import com.kamus.cookit.data.db.model.RecipeEntity
import com.kamus.cookit.data.db.model.UserDataEntity
import com.kamus.cookit.data.remote.model.CategoriesDto
import com.kamus.cookit.data.remote.RecipeApi
import com.kamus.cookit.data.remote.model.RecipeDetailDto
import com.kamus.cookit.data.remote.model.RecipeDto
import com.kamus.cookit.data.remote.model.UserDto
import retrofit2.Call
import retrofit2.Retrofit

class AppRepository(private val recipeDao: RecipeDao, private val favouriteRecipeDao: FavouriteRecipeDao, private val retrofit: Retrofit, private val friendsDao: FriendsDao, private val userDataDao: UserDataDao) {

    private val recipeApi: RecipeApi = retrofit.create(RecipeApi::class.java)

    // Room
    /* Recipes */
    suspend fun insertRecipe(recipe: RecipeEntity){
        recipeDao.insertRecipe(recipe)
    }
    /* ------------------------------------------------------------------------------------------ */
    suspend fun getUserRecipes(uid: String): List<RecipeEntity> =
        recipeDao.getUserRecipes(uid)
    /* ------------------------------------------------------------------------------------------ */
    suspend fun getRecipeByID(id: String?): RecipeEntity =
        recipeDao.getRecipeById(id)

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
    /* ------------------------------------------------------------------------------------------ */
    suspend fun getUserFavourites(uid: String): List<FavouriteRecipeEntity> =
        favouriteRecipeDao.getUserFavourites(uid)
    /* ------------------------------------------------------------------------------------------ */
    /*suspend fun getFavouriteRecipes(): List<FavouriteRecipeEntity> =
        favouriteRecipeDao.getFavouriteRecipes()*/

    suspend fun getFavouriteRecipeById(id: String): FavouriteRecipeEntity =
        favouriteRecipeDao.getFavouriteRecipeById(id)

    suspend fun deleteFavouriteRecipe(recipe: FavouriteRecipeEntity) {
        favouriteRecipeDao.deleteFavouriteRecipe(recipe)
    }

    suspend fun updateFavouriteRecipe(recipe: FavouriteRecipeEntity) {
        favouriteRecipeDao.updateFavouriteRecipe(recipe)
    }




    /* Friend */
    suspend fun insertFriend(friend: FriendsEntity){
        friendsDao.insertFriend(friend)
    }
    suspend fun deleteFriend(friend: FriendsEntity){
        friendsDao.deleteFriend(friend)
    }
    /* ------------------------------------------------------------------------------------------ */
    suspend fun getUserFriend(friend: String, user: String) =
        friendsDao.getUserFriends(friend, user)
    /* ------------------------------------------------------------------------------------------ */
    suspend fun getFriends() =
        friendsDao.getFriends()




    /* USER */
    suspend fun insertUser(user: UserDataEntity) =
        userDataDao.insertUser(user)
    suspend fun getUser(uid: String) =
        userDataDao.getUser(uid)
    suspend fun updateUser(user: UserDataEntity) =
        userDataDao.updateUser(user)



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