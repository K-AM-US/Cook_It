package com.kamus.cookit.data.remote.model

import com.google.gson.annotations.SerializedName

data class RecipeDto(
    @SerializedName("recipe_id")
    val id: String,
    @SerializedName("title")
    var title: String,
    /*@SerializedName("creator_id")
    val creatorId: String*/
    @SerializedName("type")
    var type: String,
    @SerializedName("tags")
    var tags: ArrayList<String>,
    @SerializedName("img")
    var img: String
)