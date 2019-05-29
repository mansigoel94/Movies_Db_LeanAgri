package com.example.mansi_goel_test

import com.example.mansi_goel_test.model.detail.DetailMainModel
import com.example.mansi_goel_test.model.main.MoviesMainModel
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

public interface MoviesService {

    @GET("discover/movie")
    fun getMovies(
        @Query("api_key") apiKey: String
    ): Call<MoviesMainModel>

    @GET("movie/{movie_id}")
    fun getMovieDetails(
        @Path("movie_id") id: String,
        @Query("api_key") apiKey: String
    ): Call<DetailMainModel>
}