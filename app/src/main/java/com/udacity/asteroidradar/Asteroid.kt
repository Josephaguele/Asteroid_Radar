package com.udacity.asteroidradar

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize
import java.util.*

@Parcelize
data class Asteroid(val id: Long, val codename: String, val closeApproachDate: String,
                    val absoluteMagnitude: Double, val estimatedDiameter: Double,
                    val relativeVelocity: Double, val distanceFromEarth: Double,
                    val isPotentiallyHazardous: Boolean) : Parcelable
{
 /*   // create a today's asteroid, and set its value based on when the user clicks to view the list of
    // asteroids for today

    var currentTime = Calendar.getInstance().time
    var today = currentTime

    val todaysAsteroids
        get() = closeApproachDate == today.toString()*/
}