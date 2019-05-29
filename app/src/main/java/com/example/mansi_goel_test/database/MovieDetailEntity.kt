package com.example.mansi_goel_test.database

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
public class MovieDetailEntity(@PrimaryKey var movieId: Integer?,
                         var title: String?,
                         var voteAverage: Double?,
                         var runTime: Integer?,
                         var posterPath: String?,
                         var releaseDate: String?,
                         var overview: String?) {
}