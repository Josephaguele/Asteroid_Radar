package com.udacity.asteroidradar.api
import androidx.lifecycle.LiveData
import com.squareup.moshi.Moshi
import com.squareup.moshi.kotlin.reflect.KotlinJsonAdapterFactory
import com.udacity.asteroidradar.Asteroid
import com.udacity.asteroidradar.Constants
import com.udacity.asteroidradar.Constants.BASE_URL
import com.udacity.asteroidradar.PictureOfDay
import org.json.JSONObject
import retrofit2.Call
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory
import retrofit2.converter.scalars.ScalarsConverterFactory
import retrofit2.http.GET
import retrofit2.http.Query

private val moshi = Moshi.Builder()
    .add(KotlinJsonAdapterFactory())
    .build()

private val retrofit = Retrofit.Builder()
    .addConverterFactory(MoshiConverterFactory.create(moshi))
    .baseUrl(BASE_URL)
    .build()

private val retrofitAsteroid = Retrofit.Builder()
    .addConverterFactory(ScalarsConverterFactory.create())
    .baseUrl(BASE_URL)
    .build()

// We define an interface that explains how Retrofit talks to our webserver using the HTTP request
// Retrofit will create an object that implements our interface with all of the methods that talk to
// the server
interface NasaApiService
{
    @GET("planetary/apod?api_key=NsCl1Qk3r4AR4jJJRhoxeedCshoxZX3062B4B2lI")
    suspend fun getProperties(): PictureOfDay // The call object is used to start the request

      @GET("neo/rest/v1/feed")
    suspend fun getAsteroidList(@Query("start_date")startDate:String,
                        @Query("end_date")endDate:String,
                        @Query("api_key")apiKey:String): String
}

// To create a retrofit service, we call retrofit.create passing in the service api we just defined.
object NasaApi
{
    val retrofitService: NasaApiService by lazy { retrofit.create(NasaApiService::class.java) }
}

// Different object created for Asteroids because it uses Scalars Converter
object NasaAsteroidsApi
{
    val retrofitService:NasaApiService by lazy { retrofitAsteroid.create(NasaApiService::class.java)}
}
