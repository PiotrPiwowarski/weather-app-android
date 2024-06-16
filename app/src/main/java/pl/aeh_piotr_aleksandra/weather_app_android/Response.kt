package pl.aeh_piotr_aleksandra.weather_app_android

sealed class Response<out T> {

    data class Ok<out T>(val data: T): Response<T>()
    data class NotOk(val message: String): Response<Nothing>()
    object Waiting: Response<Nothing>()
}