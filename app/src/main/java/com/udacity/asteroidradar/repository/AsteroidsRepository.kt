package com.udacity.asteroidradar.repository

import android.util.Log
import com.udacity.asteroidradar.Constants
import com.udacity.asteroidradar.Constants.API_KEY
import com.udacity.asteroidradar.Constants.currentTime
import com.udacity.asteroidradar.Constants.dateFormat
import com.udacity.asteroidradar.Constants.sevenDaysFromToday
import com.udacity.asteroidradar.api.NasaAsteroidsApi
import com.udacity.asteroidradar.asDatabaseModel
import com.udacity.asteroidradar.database.AsteroidsDatabase
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

/*A repository is a just simple class responsible for providing an interface to our
* data classes
* Repository for fetching asteroids from the network and storing them on the disk*/

class AsteroidsRepository (private val database: AsteroidsDatabase)
{
    suspend fun refreshVideos()
    {// Get the data from the network and put it in the database
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