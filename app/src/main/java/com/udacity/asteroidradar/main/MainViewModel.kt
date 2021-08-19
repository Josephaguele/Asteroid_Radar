package com.udacity.asteroidradar.main

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.udacity.asteroidradar.Asteroid
import com.udacity.asteroidradar.PictureOfDay
import com.udacity.asteroidradar.api.NasaApi
import com.udacity.asteroidradar.api.parseAsteroidsJsonResult
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.launch
import org.json.JSONObject
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response


class MainViewModel : ViewModel() {

    private val _response = MutableLiveData<String>()
    val response: LiveData<String>
        get() = _response


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

    private fun getResponse(){
        viewModelScope.launch {
            try{
            var result =    NasaApi.retrofitService.getProperties()
                _response.value = result.toString()
            }catch (e:Exception)
            {
                _response.value = "Failure: ${e.message}"
            }
        }
    }


    private fun getAsteroids(){
        NasaApi.retrofitService.getAsteroidList().enqueue(object : Callback<String>{
            override fun onResponse(call: Call<String>, response: Response<String>) {
                _asteroids.value = response.body()?.
                let { parseAsteroidsJsonResult(JSONObject(it)) }
            }
            override fun onFailure(call: Call<String>, t: Throwable) {
                _response.value = t.message
            }
        })
    }
}
