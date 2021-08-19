package com.udacity.asteroidradar.main

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import retrofit2.Call
import retrofit2.Callback
import com.udacity.asteroidradar.api.NasaApi
import retrofit2.Response


class MainViewModel : ViewModel() {

    private val _response = MutableLiveData<String>()
    val response :LiveData<String>
    get() = _response

    /**
     * Call getMarsRealEstateProperties() on init so we can display status immediately.
     */
    init {
        getResponse()

    }

    private fun getResponse()
    {
        NasaApi.retrofitService.getProperties().enqueue(object:Callback<String>{
            override fun onResponse(call: Call<String>, response: Response<String>) {
                _response.value = response.body()
            }

            override fun onFailure(call: Call<String>, t: Throwable) {
                _response.value = "Failure" + t.message
            }

        })
        _response.value = "Set the api response here."
    }


}
