package com.kamus.cookit.data.remote.model

import com.google.gson.annotations.SerializedName

data class FriendsDto(
    @SerializedName("id")
    var id: String,
    @SerializedName("username")
    var username: String,
    @SerializedName("first_name")
    var firstName: String,
    @SerializedName("last_name")
    var lastName: String,
    @SerializedName("recipes")
    var recipes: ArrayList<RecipeDto>,
    @SerializedName("img")
    var img: String,
    @SerializedName("friends")
    var friends: ArrayList<List<UserDto>>
)
