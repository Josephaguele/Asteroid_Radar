package com.udacity.asteroidradar.detail

import android.app.Application
import androidx.lifecycle.*
import com.udacity.asteroidradar.Asteroid
import org.joda.time.LocalDateTime
import java.util.*

class DetailViewModel(asteroidProperty: Asteroid, app: Application) : AndroidViewModel(app)
{
    private val _selectedAsteroid = MutableLiveData<Asteroid>()

    val selectedAsteroid: LiveData<Asteroid>
        get() = _selectedAsteroid

    init {
        _selectedAsteroid.value = asteroidProperty
    }

    /*val displayTodayAsteroid = Transformations.map(selectedAsteroid)
    {
        var currentTime = Calendar.getInstance().time
        var today = currentTime

        return@map today


    }*/



}