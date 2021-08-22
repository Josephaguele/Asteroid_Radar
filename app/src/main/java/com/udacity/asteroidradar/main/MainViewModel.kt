package com.udacity.asteroidradar.main
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.udacity.asteroidradar.Asteroid
import com.udacity.asteroidradar.Constants
import com.udacity.asteroidradar.PictureOfDay
import com.udacity.asteroidradar.api.NasaApi
import com.udacity.asteroidradar.api.NasaAsteroidsApi
import com.udacity.asteroidradar.api.parseAsteroidsJsonResult
import org.joda.time.LocalDateTime

import org.json.JSONObject
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import timber.log.Timber
import java.text.SimpleDateFormat
import java.util.*


class MainViewModel : ViewModel() {

    private val _response = MutableLiveData<String>()
    val response: LiveData<String>
        get() = _response

    private val _pictureOfTheDay = MutableLiveData<PictureOfDay>()
    val pictureOfDay: LiveData<PictureOfDay>
    get() = _pictureOfTheDay

    private val _asteroids = MutableLiveData<List<Asteroid>>()
    val asteroids: LiveData<List<Asteroid>>
    get() = _asteroids

    /**
     * Call methods() on init so we can display status immediately.
     */
    init {
        getResponse()
        getAsteroids()

    }
    private fun getResponse()
    {
        NasaApi.retrofitService.getProperties().enqueue(object : Callback<PictureOfDay> {
            override fun onResponse(call: Call<PictureOfDay>, response: Response<PictureOfDay>) {
                _pictureOfTheDay.value = response.body()
            }
            override fun onFailure(call: Call<PictureOfDay>, t: Throwable) {
                _response.value =  "Failure" + t.message
            }
        })
        _response.value = "Set the api response here."
    }


    private fun getAsteroids()
    {
        val currentTime=Calendar.getInstance().time

        //get seven days from the current date
        val calendar=Calendar.getInstance().time;//this would default to now
        Log.i("DATE MATTERS",calendar.toString())//just checking the date format in the Logcat

        val today=LocalDateTime.now()
        val nextSevenDays=today.plusDays(7)
        Log.i("NEXT SEVEN DAYS",nextSevenDays.toString().substring(0,10))
        val sevenDaysFromToday=nextSevenDays.toString().substring(0,10)


        //puts date in your local time in the format stated in the Constants object which is:YYYY-MM-dd
        val dateFormat=SimpleDateFormat(Constants.API_QUERY_DATE_FORMAT,Locale.getDefault())

        NasaAsteroidsApi.retrofitService.getAsteroidList(
            dateFormat.format(currentTime),sevenDaysFromToday,Constants.API_KEY).enqueue(object:Callback<String>{
            override fun onResponse(call:Call<String>,response:Response<String>){
                if(response.body()!==null){
                    val result=JSONObject(response.body())
                    _asteroids.value=parseAsteroidsJsonResult(result)
                }
            }
            override fun onFailure(call:Call<String>,t:Throwable){
                _response.value="Failure:"+t.message
            }
        })
    }



}
