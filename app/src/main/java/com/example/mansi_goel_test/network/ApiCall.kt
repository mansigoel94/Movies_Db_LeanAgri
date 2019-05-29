package com.example.mansi_goel_test.network

import com.example.mansi_goel_test.MoviesService
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class ApiCall {
    companion object {

        val BASE_URL = "https://api.themoviedb.org/3/"
        val API_KEY = "f71010fbf93ae50fb2113b214447992a"

        public fun getService(): MoviesService {
            val retrofit = Retrofit.Builder()
                .baseUrl(BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build()

            return (retrofit.create<MoviesService>(MoviesService::class.java!!))
        }
    }
}