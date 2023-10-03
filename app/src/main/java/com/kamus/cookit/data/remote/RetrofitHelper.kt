package com.kamus.cookit.data.remote

import com.kamus.cookit.utils.Constants.BASE_URL
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class RetrofitHelper {

    fun getRetrofit(): Retrofit {
        return Retrofit.Builder()
            .baseUrl(BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
    }
}