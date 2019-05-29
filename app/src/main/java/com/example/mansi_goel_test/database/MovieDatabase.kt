package com.example.mansi_goel_test.database

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase

@Database(entities = [MovieEntity::class, MovieDetailEntity::class], version = 1, exportSchema = false)
public abstract class MovieDatabase : RoomDatabase() {
    abstract fun movieDao(): MovieDao

    companion object {
        private var INSTANCE: MovieDatabase? = null

        public fun getAppDatabase(context: Context): MovieDatabase {
            if (INSTANCE == null) {
                INSTANCE =
                    Room.databaseBuilder(context.applicationContext, MovieDatabase::class.java!!, "movie-database")
                        .build()
            }
            return INSTANCE as MovieDatabase
        }
    }

}

