package com.udacity.asteroidradar.repository

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.Transformations
import com.udacity.asteroidradar.Asteroid
import com.udacity.asteroidradar.Constants
import com.udacity.asteroidradar.Constants.API_KEY
import com.udacity.asteroidradar.Constants.currentTime
import com.udacity.asteroidradar.Constants.dateFormat
import com.udacity.asteroidradar.Constants.sevenDaysFromToday
import com.udacity.asteroidradar.api.NasaAsteroidsApi
import com.udacity.asteroidradar.asDatabaseModel
import com.udacity.asteroidradar.database.AsteroidsDatabase
import com.udacity.asteroidradar.database.asDomainModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import java.text.SimpleDateFormat
import java.time.LocalDateTime
import java.util.*

/*A repository is a just simple class responsible for providing an interface to our
* data classes
* Repository for fetching asteroids from the network and storing them on the disk*/

class AsteroidsRepository (private val database: AsteroidsDatabase)
{
    // we use Transformation.map to convert our LiveData list of DatabaseAsteroid objects to
    // domain Asteroid objects.
    val asteroids: LiveData<List<Asteroid>> =
        Transformations.map(database.asteroidDao.getAsteroids()){
            it.asDomainModel()
        }


    suspend fun refreshVideos()
    {// Get the data from the network and put it in the database

        val currentTime = Calendar.getInstance().time

        //get seven days from the current date
        val calendar = Calendar.getInstance().time//this would default to now
        Log.i("DATE MATTERS", calendar.toString())//just checking the date format in the Logcat

        val today = LocalDateTime.now()
        val nextSevenDays = today.plusDays(7)
        Log.i("NEXT SEVEN DAYS", nextSevenDays.toString().substring(0, 10))
        val sevenDaysFromToday = nextSevenDays.toString().substring(0, 10)

        //puts date in your local time in the format stated in the Constants object which is:YYYY-MM-dd
        val dateFormat = SimpleDateFormat(Constants.API_QUERY_DATE_FORMAT, Locale.getDefault())

        withContext(Dispatchers.IO)
        {
            try {
                val asteroidList = NasaAsteroidsApi.retrofitService.getAsteroidList(  dateFormat.format(currentTime), sevenDaysFromToday, API_KEY)
                database.asteroidDao.insertAll(*asteroidList.asDatabaseModel())
            } catch (e: Exception) { Log.i("MESSAGE","Updated asteroids not available")
            }
        }

    }

}