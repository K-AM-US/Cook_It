package com.kamus.cookit.application

import android.app.Application
import com.kamus.cookit.data.AppRepository
import com.kamus.cookit.data.db.RecipeDatabase

class CookItApp: Application() {

    private val database by lazy {
        RecipeDatabase.getDatabase(this@CookItApp)
    }

    val repository by lazy {
        AppRepository(database.recipeDao())
    }
}