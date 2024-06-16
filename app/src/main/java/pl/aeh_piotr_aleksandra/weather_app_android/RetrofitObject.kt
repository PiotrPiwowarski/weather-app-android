package pl.aeh_piotr_aleksandra.weather_app_android

import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

object RetrofitObject {

    private fun getObject(): Retrofit {
        return Retrofit.Builder()
            .addConverterFactory(GsonConverterFactory.create())
            .baseUrl(ConstValues.URL)
            .build()
    }

    val api: Api = getObject().create(Api::class.java)
}