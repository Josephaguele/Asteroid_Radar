package com.udacity.asteroidradar.main


import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.udacity.asteroidradar.Asteroid
import com.udacity.asteroidradar.Constants
import com.udacity.asteroidradar.PictureOfDay
import com.udacity.asteroidradar.api.NasaApi
import com.udacity.asteroidradar.api.NasaAsteroidsApi
import com.udacity.asteroidradar.api.parseAsteroidsJsonResult
import kotlinx.coroutines.launch
import org.joda.time.LocalDateTime
import org.json.JSONObject
import java.text.SimpleDateFormat
import java.util.*


class MainViewModel : ViewModel() {

    enum class NasaApiStatus {  ERROR, DONE }
    private val _status = MutableLiveData<NasaApiStatus>()
    val status: LiveData<NasaApiStatus>
        get() = _status

    //
    private val _pictureOfTheDay = MutableLiveData<PictureOfDay>()
    val pictureOfDay: LiveData<PictureOfDay>
        get() = _pictureOfTheDay

    private val _asteroids = MutableLiveData<List<Asteroid>>()
    val asteroids: LiveData<List<Asteroid>>
        get() = _asteroids

    // for navigating to selected asteroid detail screen
    private val _navigateToSelectedAsteroid = MutableLiveData<Asteroid>()
    val navigateToSelectedProperty : LiveData<Asteroid>
    get() = _navigateToSelectedAsteroid

    /**
     * Call methods() on init so we can display status immediately.
     */
    init {
        getPictureOfToday()
        getAsteroids()
    }

    /* function to set _navigateToSelectedAsteroid to asteroid and initiate navigation to
     the detail screen on button click:*/
    fun displayAsteroidDetails(asteroid: Asteroid)
    {
        _navigateToSelectedAsteroid.value = asteroid
    }

    /* to displayAsteroidDetailsComplete, we set _navigateToSelectedAsteroid to false once navigation
     is completed to prevent unwanted extra navigation.*/
    fun displayAsteroidDetailsComplete()
    {
        _navigateToSelectedAsteroid.value = null
    }

    private fun getPictureOfToday() {
        viewModelScope.launch {
            try {
                val result = NasaApi.retrofitService.getProperties()
                _pictureOfTheDay.value = result
                // when there is  internet connection, the user is shown the data generated

            } catch (e: Exception) {
                // when there is no internet connection, the user is shown the error image
                _status.value = NasaApiStatus.ERROR
            }
        }
    }


    private fun getAsteroids() {
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

        viewModelScope.launch {
            try{
                val asteroidList = NasaAsteroidsApi.retrofitService.getAsteroidList(
                    dateFormat.format(currentTime),
                    sevenDaysFromToday,
                    Constants.API_KEY
                )
                if (asteroidList !== null) {
                    val result = JSONObject(asteroidList)
                    _asteroids.value = parseAsteroidsJsonResult(result)
                }
            }catch (e:Exception){ _status.value = NasaApiStatus.ERROR}
        }
    }

}
