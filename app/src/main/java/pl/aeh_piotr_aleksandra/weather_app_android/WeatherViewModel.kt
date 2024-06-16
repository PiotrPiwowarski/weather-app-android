package pl.aeh_piotr_aleksandra.weather_app_android

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.launch

class WeatherViewModel: ViewModel() {


    private val api = RetrofitObject.api
    private val _weatherResult = MutableLiveData<Response<DataModel>>()
    val weatherResult: LiveData<Response<DataModel>> = _weatherResult

    fun getWeather(city: String) {

        viewModelScope.launch {
            _weatherResult.value = Response.Waiting
            try {
                val response = api.getWeather(ConstValues.API_KEY, city)
                if(response.isSuccessful) {
                    response.body()?.let {
                        _weatherResult.value = Response.Ok(it)
                    }
                } else {
                    _weatherResult.value = Response.NotOk("Nie udało się wczytać danych (upewnij się, że nazwa miasta jest po angielsku)")
                }
            } catch(e: Exception) {
                _weatherResult.value = Response.NotOk("Nie udało się wczytać danych (upewnij się, że nazwa miasta jest po angielsku)")
            }
        }
    }
}