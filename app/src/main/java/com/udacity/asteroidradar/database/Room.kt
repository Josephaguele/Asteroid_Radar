package com.udacity.asteroidradar.database

import android.content.Context
import androidx.lifecycle.LiveData
import androidx.room.*
import com.udacity.asteroidradar.Asteroid
import com.udacity.asteroidradar.PictureOfDay


@Dao
interface  AsteroidDao{

    @Query("select * from databaseasteroid")
    fun getAsteroids(): LiveData<List<DatabaseAsteroid>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
        fun insertAll( asteroids: List<DatabaseAsteroid>)
}

@Database(entities = [DatabaseAsteroid::class], version = 1)
abstract class AsteroidsDatabase : RoomDatabase()
{ // add an abstract asteroidDao variable
   abstract val asteroidDao : AsteroidDao
}

// define an instance variable to store the singleton
private lateinit var INSTANCE : AsteroidsDatabase

// deFINE a getDatabase function to return the AsteroidsDatabase
fun getDatabase(context: Context): AsteroidsDatabase
{
    // Check whether the database has been initialized, if it hasn't then initialize it
    // we make the initialization thread safe by wrapping it up
    synchronized(AsteroidsDatabase::class.java)
    {
        if (!::INSTANCE.isInitialized) {
            INSTANCE = Room.databaseBuilder(context.applicationContext,
                AsteroidsDatabase::class.java,
                "asteroids").build()
        }
    }
    return INSTANCE

}




