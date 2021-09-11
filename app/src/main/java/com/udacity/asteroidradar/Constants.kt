package com.udacity.asteroidradar

import org.joda.time.LocalDateTime
import java.text.SimpleDateFormat
import java.util.*

object Constants {
    const val API_QUERY_DATE_FORMAT = "YYYY-MM-dd"
    const val DEFAULT_END_DATE_DAYS = 7
    const val BASE_URL = "https://api.nasa.gov/"
    const val API_KEY = "NsCl1Qk3r4AR4jJJRhoxeedCshoxZX3062B4B2lI"


    val currentTime = Calendar.getInstance().time

    //get seven days from the current date
    val calendar = Calendar.getInstance().time//this would default to now

    val today = LocalDateTime.now()
    val nextSevenDays = today.plusDays(7)
    val sevenDaysFromToday = nextSevenDays.toString().substring(0, 10)

    //puts date in your local time in the format stated in the Constants object which is:YYYY-MM-dd
    val dateFormat = SimpleDateFormat(API_QUERY_DATE_FORMAT, Locale.getDefault())

}