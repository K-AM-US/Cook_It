package com.kamus.cookit.data.remote.model

import com.google.gson.annotations.SerializedName

data class UserDto(
    @SerializedName("id")
    val id: String,
    @SerializedName("username")
    var userName: String,
    @SerializedName("first_name")
    var firstName: String,
    @SerializedName("last_name")
    var lastName: String,
    /*@SerializedName("recipes")
    var recipes: ArrayList<RecipeDto>,*/
    @SerializedName("image")
    var img: String,
    /*@SerializedName("friends")
    var friends: ArrayList<List<UserDto>>*/
)
