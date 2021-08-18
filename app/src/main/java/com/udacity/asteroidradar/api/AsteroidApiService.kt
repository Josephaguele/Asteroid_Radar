package com.udacity.asteroidradar.api


import com.udacity.asteroidradar.Asteroid
import com.udacity.asteroidradar.Constants.BASE_URL
import com.udacity.asteroidradar.PictureOfDay
import kotlinx.coroutines.Deferred
import retrofit2.Call
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory
import retrofit2.http.GET

    /**
     * Add an instance of ScalarsConverterFactory and the BASE_URL we provided, then call build()
     * to create the Retrofit object.*/
    private val retrofit = Retrofit.Builder()
        .addConverterFactory(MoshiConverterFactory.create())
        .baseUrl(BASE_URL)
        .build()

    /*Create an AsteroidApiService interface, and define a getProperties() method to request the
     JSON response string. Annotate the method with @GET, specifying the endpoint for the JSON
      response, and create the Retrofit Call object that will start the HTTP request.*/
    interface AsteroidApiService {
        @GET("neo/rest/v1/feed?start_date=2015-09-07&end_date=2015-09-07&api_key=NsCl1Qk3r4AR4jJJRhoxeedCshoxZX3062B4B2lI")
         suspend fun getAsteroid() : List<Asteroid>
        @GET("planetary/apod?api_key=NsCl1Qk3r4AR4jJJRhoxeedCshoxZX3062B4B2lI")
         suspend fun getPictureOfDay() :List<PictureOfDay>
    }

// Passing in the service API you just defined, create a public object called
// AsteroidAPiService to expose the Retrofit service to the rest of the app
object AsteroidApi{
    val retrofitService: AsteroidApiService by lazy { retrofit.create(AsteroidApiService::class.java) }
}