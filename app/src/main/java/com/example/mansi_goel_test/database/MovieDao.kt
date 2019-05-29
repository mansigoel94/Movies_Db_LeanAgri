package com.example.mansi_goel_test.database

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query

@Dao
public interface MovieDao {

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    public fun insertMoviesList(movieEntityList: List<MovieEntity>)

    @Query("Select * from MovieEntity")
    public fun getMoviesList(): List<MovieEntity>

    @Query("Delete from MovieEntity")
    public fun deleteAll()

    @Insert
    public fun insertMovieDetails(movieDetailEntity: MovieDetailEntity)

    @Query("Select * from MovieDetailEntity where movieId=:id")
    public fun getMovieDetails(id: String): MovieDetailEntity

    @Query("Delete from MovieDetailEntity where movieId=:id")
    public fun deleteMovieDetails(id: String)
}