package com.udacity.asteroidradar.main


import android.app.Application
import android.os.Build
import androidx.lifecycle.*
import androidx.work.*
import com.udacity.asteroidradar.Asteroid
import com.udacity.asteroidradar.PictureOfDay
import com.udacity.asteroidradar.api.NasaApi
import com.udacity.asteroidradar.database.getDatabase
import com.udacity.asteroidradar.repository.AsteroidsRepository
import com.udacity.asteroidradar.worker.RefreshDataWork
import kotlinx.coroutines.launch
import java.util.concurrent.TimeUnit


class MainViewModel(application: Application) : AndroidViewModel(application)
{
    private val database = getDatabase(application)
    private val asteroidsRepository = AsteroidsRepository(database)


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
        viewModelScope.launch {
            asteroidsRepository.refreshAsteroids()
            setUpRecurringWork()
        }

    }

    // Running Work with WorkManager
    private fun setUpRecurringWork() {
        val constraints = Constraints.Builder()
            .setRequiredNetworkType(NetworkType.UNMETERED)
            .setRequiresBatteryNotLow(true)
            .setRequiresCharging(true)
            .apply {
                if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.M)
                {
                    setRequiresDeviceIdle(true)
                }
            }.build()

        val repeatingRequest = PeriodicWorkRequestBuilder<RefreshDataWork>(1, TimeUnit.DAYS)
            .setConstraints(constraints).build()

        WorkManager.getInstance().enqueueUniquePeriodicWork(
            RefreshDataWork.WORK_NAME,
            ExistingPeriodicWorkPolicy.KEEP,repeatingRequest)
    }

    val asteroid = asteroidsRepository.asteroids

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


}
