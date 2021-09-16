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


@Dao
interface PictureDao{
    @Query("select * from databasepictureoftheday")
    fun getDatabasePictureOfTheDay(): LiveData<PictureOfDay>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertAll(pictures: DatabasePictureOfDay)
}


@Database(entities = [DatabasePictureOfDay::class], version = 1)
abstract class PictureDatabase : RoomDatabase()
{ // add an abstract pictureDao variable
    abstract val pictureDao : PictureDao
}

// define an instance variable to store the singleton
private lateinit var INSTANCE2 : PictureDatabase

// deFINE a getDatabase function to return the PictureDatabase
fun getPictureDatabase(context: Context): PictureDatabase
{
    // Check whether the database has been initialized, if it hasn't then initialize it
    // we make the initialization thread safe by wrapping it up
    synchronized(PictureDatabase::class.java)
    {
        if (!::INSTANCE2.isInitialized) {
            INSTANCE2 = Room.databaseBuilder(context.applicationContext,
                PictureDatabase::class.java,
                "pictures").build()
        }
    }
    return INSTANCE2

}