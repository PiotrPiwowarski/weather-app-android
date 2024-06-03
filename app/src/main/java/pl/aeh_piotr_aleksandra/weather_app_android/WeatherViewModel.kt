package pl.aeh_piotr_aleksandra.weather_app_android

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.launch
import pl.aeh_piotr_aleksandra.weather_app_android.api.Constant
import pl.aeh_piotr_aleksandra.weather_app_android.api.RetrofitInstance

class WeatherViewModel: ViewModel() {


    private val weatherApi = RetrofitInstance.weatherApi

    fun getData(city: String) {

        viewModelScope.launch {
            val response = weatherApi.getWeather(Constant.apiKey, city)
            if(response.isSuccessful) {
                Log.i("Response: ", response.body().toString())
            } else {
                Log.i("Error: ", response.message())
            }
        }
    }
}