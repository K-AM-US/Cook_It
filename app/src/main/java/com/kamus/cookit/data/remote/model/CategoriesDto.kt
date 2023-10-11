package com.kamus.cookit.data.remote.model

import com.google.gson.annotations.SerializedName

data class CategoriesDto(
    @SerializedName("category")
    val category: String,
    @SerializedName("category_icon")
    val icon: String
)
