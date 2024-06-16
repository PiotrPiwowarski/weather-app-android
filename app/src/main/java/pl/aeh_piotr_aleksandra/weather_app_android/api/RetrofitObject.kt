package pl.aeh_piotr_aleksandra.weather_app_android.api

import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

object RetrofitObject {

    private const val URL = "https://api.weatherapi.com"

    private fun getObject(): Retrofit {
        return Retrofit.Builder()
            .addConverterFactory(GsonConverterFactory.create())
            .baseUrl(URL)
            .build()
    }

    val api: Api = getObject().create(Api::class.java)
}