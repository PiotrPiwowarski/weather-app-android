package pl.aeh_piotr_aleksandra.weather_app_android.api

import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Query

interface Api {

    @GET("/v1/current.json")
    suspend fun getWeather(@Query("key") apiKey: String, @Query("q") city: String): Response<DataModel>
}