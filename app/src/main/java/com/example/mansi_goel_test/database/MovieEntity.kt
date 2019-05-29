package com.example.mansi_goel_test.database

import androidx.room.Entity
import androidx.room.PrimaryKey


@Entity
public class MovieEntity(@PrimaryKey var movieId: Integer?,
                         var title: String?,
                         var releaseDate: String?,
                         var voteAverage: Double?,
                         var posterPath: String?,
                         var overview: String?) {
}