package com.udacity.asteroidradar.main

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.udacity.asteroidradar.Asteroid
import com.udacity.asteroidradar.PictureOfDay
import retrofit2.Call
import retrofit2.Callback
import com.udacity.asteroidradar.api.NasaApi
import retrofit2.Response


class MainViewModel : ViewModel() {

    private val _response = MutableLiveData<String>()
    val response :LiveData<String>
    get() = _response

    private val _asResponse = MutableLiveData<String>()
    val asResponse : LiveData<String>
    get() = _asResponse
    /**
     * Call getMarsRealEstateProperties() on init so we can display status immediately.
     */
    init {
        getResponse()
        getAsteroidResponse()

    }

    private fun getResponse()
    {
        NasaApi.retrofitService.getProperties().enqueue(object:Callback<PictureOfDay>{
            override fun onResponse(call: Call<PictureOfDay>, response: Response<PictureOfDay>) {
                _response.value = response.body().toString()
            }

            override fun onFailure(call: Call<PictureOfDay>, t: Throwable) {
                _response.value = "Failure" + t.message
            }

        })
        _response.value = "Set the api response here."
    }

    private fun getAsteroidResponse()
    {
        NasaApi.retrofitService.getAsteroidList().enqueue(object:Callback<Asteroid>{
            override fun onResponse(call: Call<Asteroid>, response: Response<Asteroid>) {
                _asResponse.value =  response.message()
            }

            override fun onFailure(call: Call<Asteroid>, t: Throwable) {
                _asResponse.value = " Failure" + t.message
            }

        })
        _asResponse.value = "Set the Asteroid Response here."
    }


}
