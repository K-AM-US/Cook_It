package com.kamus.cookit.application

import android.app.Application
import com.kamus.cookit.data.AppRepository
import com.kamus.cookit.data.db.RecipeDatabase
import com.kamus.cookit.data.remote.RetrofitHelper

class CookItApp: Application() {

    private val database by lazy {
        RecipeDatabase.getDatabase(this@CookItApp)
    }

    private val retrofit by lazy {
        RetrofitHelper().getRetrofit()
    }

    val repository by lazy {
        AppRepository(database.recipeDao(), database.favouriteDao(), retrofit)
    }
}