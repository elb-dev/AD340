package com.example.ad340

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import kotlin.random.Random

class ForecastRepository {
    private val _weeklyForecast = MutableLiveData<List<DailyForecast>>()
    val weeklyForecast: LiveData<List<DailyForecast>> = _weeklyForecast

    fun loadForecast(zipcode: String){
        val randomValues = List(7){ Random.nextFloat().rem(100) * 100 }
        val forecastItems = randomValues.map{ temp ->
            DailyForecast(temp, getTempDescription(temp))
        }

        _weeklyForecast.setValue(forecastItems)
    }

    private fun getTempDescription(temp: Float) : String {
        return when(temp) {
            in Float.MIN_VALUE.rangeTo(0f) -> "Anything below 0 doesn't make sense"
            in 0f.rangeTo(32f) -> "I hope there's snow"
            in 32f.rangeTo(55f) -> "Colder than I would prefer"
            in 55f.rangeTo(65f) -> "This is pretty nice!"
            in 65f.rangeTo(80f) -> "Hopefully it doesn't get muggy"
            in 80f.rangeTo(90f) -> "Need to take a swim"
            in 90f.rangeTo(100f) -> "Just leave the fridge open"
            in 100f.rangeTo(Float.MAX_VALUE) -> "How do you like it in the fire?"
            else -> "Does not compute"
        }
    }
}