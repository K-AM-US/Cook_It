package com.kamus.cookit.data.remote.model

import com.google.gson.annotations.SerializedName

data class RecipeDetailDto(
    @SerializedName("title")
    var title: String,
    @SerializedName("ingredients")
    var ingredients: ArrayList<String>,
    @SerializedName("process")
    var process: ArrayList<String>,
    @SerializedName("image")
    var image: String
)
