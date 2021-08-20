package com.udacity.asteroidradar.main
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.udacity.asteroidradar.Asteroid
import com.udacity.asteroidradar.PictureOfDay
import com.udacity.asteroidradar.api.NasaApi
import com.udacity.asteroidradar.api.NasaAsteroidsApi
import com.udacity.asteroidradar.api.parseAsteroidsJsonResult

import org.json.JSONObject
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response


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
        NasaAsteroidsApi.retrofitService.getAsteroidList().enqueue(object : Callback<String>{
            override fun onResponse(call: Call<String>, response: Response<String>) {
                    _asteroids.value = response.body()?.let { parseAsteroidsJsonResult(JSONObject(it)) }
            }
            override fun onFailure(call: Call<String>, t: Throwable) {
                _response.value = "Failure:"+t.message

            }
        })
    }


 /*   private fun getAsteroid()
    {
        NasaApi.retrofitService.getAsteroidList().enqueue(object: Callback<List<Asteroid>>{
            override fun onResponse(call: Call<List<Asteroid>>, response: Response<List<Asteroid>>
            ) {
                _response.value = "There are : ${response.body()?.size} asteroids"
            }
            override fun onFailure(call: Call<List<Asteroid>>, t: Throwable) {
                _response.value = t.message
            }
        })
    }*/
}
