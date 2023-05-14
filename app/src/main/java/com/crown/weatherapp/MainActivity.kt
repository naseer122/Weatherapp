package com.crown.weatherapp

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.ImageView
import android.widget.TextView
import com.bumptech.glide.Glide
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory

class MainActivity : AppCompatActivity() {
    lateinit var resultTv : TextView
    lateinit var imageIv :ImageView
    lateinit var CityTv : TextView
    lateinit var iconCode : String
    var defaultcity = "Boston"
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        var SearchBtn = findViewById<Button>(R.id.search)
        var getCity = findViewById<EditText>(R.id.getCity)
       resultTv = findViewById(R.id.resultTv)
        imageIv = findViewById(R.id.weathericon)
        CityTv = findViewById(R.id.CityTv)
        fetchWeatherData(defaultcity)
        CityTv.text = defaultcity
        SearchBtn.setOnClickListener(View.OnClickListener {
            var city  = getCity.text.toString()
            fetchWeatherData(city)
            CityTv.text = city
        })

    }

    private fun fetchWeatherData(city: String) {
        val retrofit = Retrofit.Builder()
            .baseUrl("https://api.openweathermap.org/data/2.5/")
            .addConverterFactory(MoshiConverterFactory.create())
            .build()

        val service = retrofit.create(WeatherApiService::class.java)
        CoroutineScope(Dispatchers.IO).launch {
            val response = service.getWeather(city, "d7d166cc159d94ddc224036b084f9df3","metric")
            if (response.isSuccessful) {
                val weatherResponse = response.body()
                val temperature = weatherResponse?.main?.temp ?: 0.0
                iconCode = weatherResponse?.weather?.getOrNull(0)?.icon ?: ""
                val weather = Weather(city, temperature,iconCode)
                displayWeather(weather)
            } else {
                Log.e("WeatherApp", "Error fetching weather data: ${response.message()}")
            }
        }
    }
    private fun displayWeather(weather: Weather) {
     //  val temperatureTextView = findViewById<TextView>(R.id.temperatureTextView)
        runOnUiThread {
            resultTv.text = "Temprature is \t "+"${weather.temperature}°C"
            val iconUrl = "https://openweathermap.org/img/w/$iconCode.png"

            Glide.with(this)
                .load(iconUrl)
                .into(imageIv)
        }

    }
}