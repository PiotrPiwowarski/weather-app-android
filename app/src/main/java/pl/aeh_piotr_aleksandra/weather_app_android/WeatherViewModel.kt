package pl.aeh_piotr_aleksandra.weather_app_android

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.launch
import pl.aeh_piotr_aleksandra.weather_app_android.api.Constant
import pl.aeh_piotr_aleksandra.weather_app_android.api.NetworkResponse
import pl.aeh_piotr_aleksandra.weather_app_android.api.RetrofitInstance
import pl.aeh_piotr_aleksandra.weather_app_android.api.WeatherModel

class WeatherViewModel: ViewModel() {


    private val weatherApi = RetrofitInstance.weatherApi
    private val _weatherResult = MutableLiveData<NetworkResponse<WeatherModel>>()
    val weatherResult: LiveData<NetworkResponse<WeatherModel>> = _weatherResult

    fun getData(city: String) {

        viewModelScope.launch {
            _weatherResult.value = NetworkResponse.Loading
            try {
                val response = weatherApi.getWeather(Constant.API_KEY, city)
                if(response.isSuccessful) {
                    response.body()?.let {
                        _weatherResult.value = NetworkResponse.Success(it)
                    }
                } else {
                    _weatherResult.value = NetworkResponse.Error("Fail to load data")
                }
            } catch(e: Exception) {
                _weatherResult.value = NetworkResponse.Error("Fail to load data")
            }
        }
    }
}