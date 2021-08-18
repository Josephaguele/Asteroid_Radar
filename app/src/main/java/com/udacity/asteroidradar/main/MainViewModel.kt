package com.udacity.asteroidradar.main

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.udacity.asteroidradar.Asteroid
import com.udacity.asteroidradar.PictureOfDay
import com.udacity.asteroidradar.api.AsteroidApi
import kotlinx.coroutines.launch


class MainViewModel : ViewModel() {

    // The internal MutableLiveData String that stores the most recent response
    private val _asteroids = MutableLiveData<List<Asteroid>>()
    // The external immutable LiveData for the response String
    val asteroids: LiveData<List<Asteroid>>
        get() = _asteroids

    private val _imageOfDay = MutableLiveData<PictureOfDay>()
    val imageOfDay: LiveData<PictureOfDay>
        get() = _imageOfDay

    private val _response = MutableLiveData<String>()
    val response :LiveData<String>
    get() = _response
    /**
     * Call getMarsRealEstateProperties() on init so we can display status immediately.
     */
    init {
        getAsteroids()
        getTodaysImage()
    }

    /**
     * Sets the value of the response LiveData to the Asteroid API status or the successful number of
     * Asteroid properties retrieved.
     */
    private fun getAsteroids() {

    }

    private fun getTodaysImage() {
        viewModelScope.launch {
            try {
                var listResult = AsteroidApi.retrofitService.getPictureOfDay()
                _response.value = "Success: ${listResult.size} Mars properties retrieved"
                if (listResult.size > 0) {
                    _imageOfDay.value = (listResult[0])
                }
            } catch (e: Exception) {
                _response.value = "Failure: ${e.message}"
            }
        }
    }
}
