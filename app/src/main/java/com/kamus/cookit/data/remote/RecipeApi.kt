package com.kamus.cookit.data.remote

import com.kamus.cookit.data.remote.model.CategoriesDto
import com.kamus.cookit.data.remote.model.FriendsDto
import com.kamus.cookit.data.remote.model.RecipeDetailDto
import com.kamus.cookit.data.remote.model.RecipeDto
import com.kamus.cookit.data.remote.model.UserDto
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Path

interface RecipeApi {
    @GET("/categories")
    fun getCategories(): Call<List<CategoriesDto>>

    @GET("/recipes")
    fun getHomeRecipes(): Call<List<RecipeDto>>

    @GET("/users")
    fun getUsers(): Call<List<UserDto>>

    @GET("/friends")
    fun getFriends(): Call<List<FriendsDto>>

    @GET("/recipe/{id}")
    fun getRecipeDetail(
        @Path("id") id: String?
    ): Call<RecipeDetailDto>
}