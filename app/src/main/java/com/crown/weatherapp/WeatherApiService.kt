package com.crown.weatherapp

import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Query

interface WeatherApiService {
    @GET("weather")
    suspend fun getWeather(
        @Query("q") city: String,
        @Query("appid") apiKey: String,
        @Query("units") units: String
    ): Response<WeatherResponse>
}

data class WeatherResponse(
    val main: Main,
    val weather: List<WeatherData>
)
data class WeatherData(
    val icon: String
)
data class Main(
    val temp: Double
)
