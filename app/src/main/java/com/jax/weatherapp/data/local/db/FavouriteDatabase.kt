package com.jax.weatherapp.data.local.db

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.jax.weatherapp.data.local.model.CityDbModel


@Database(entities = [CityDbModel::class], version = 1, exportSchema = false)
abstract class FavouriteDatabase : RoomDatabase() {

    abstract fun favouriteCitiesDao(): FavouriteCitiesDao

    companion object {

        private const val DB_NAME = "FavouriteDatabase"
        private var INSTANCE: FavouriteDatabase? = null
        private val LOCK = Any()

        fun getInstance(context: Context):FavouriteDatabase{
            INSTANCE?.let { return it }

            synchronized(LOCK){
                INSTANCE?.let { return it }
                val db = Room.databaseBuilder(
                    context = context,
                    name = DB_NAME,
                    klass = FavouriteDatabase::class.java
                ).build()
                INSTANCE = db
                return db
            }
        }
    }
}