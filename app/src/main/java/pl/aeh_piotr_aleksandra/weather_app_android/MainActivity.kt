package pl.aeh_piotr_aleksandra.weather_app_android

import android.Manifest
import android.annotation.SuppressLint
import android.content.pm.PackageManager
import android.location.Geocoder
import android.location.Location
import android.os.Bundle
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.ui.Modifier
import androidx.core.content.ContextCompat
import androidx.lifecycle.ViewModelProvider
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices
import com.google.android.gms.tasks.OnCompleteListener
import pl.aeh_piotr_aleksandra.weather_app_android.ui.theme.WeatherappandroidTheme

class MainActivity : ComponentActivity() {

    private lateinit var fusedLocationClient: FusedLocationProviderClient
    private var cityName: String? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val weatherViewModel = ViewModelProvider(this)[WeatherViewModel::class.java]
        fusedLocationClient = LocationServices.getFusedLocationProviderClient(this)


        checkLocationPermission { city ->
            cityName = city

            setContent {
                WeatherappandroidTheme {
                    Surface(
                        modifier = Modifier.fillMaxSize(),
                        color = MaterialTheme.colorScheme.background
                    ) {
                        cityName?.let {
                            WeatherPage(weatherViewModel, it)
                        }
                    }
                }
            }
        }
    }

    private fun checkLocationPermission(onLocationFound: (String) -> Unit) {
        if (ContextCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_FINE_LOCATION)
            != PackageManager.PERMISSION_GRANTED) {

            requestPermissionLauncher.launch(Manifest.permission.ACCESS_FINE_LOCATION)
        } else {
            getCurrentLocation(onLocationFound)
        }
    }

    private val requestPermissionLauncher =
        registerForActivityResult(ActivityResultContracts.RequestPermission()) { isGranted: Boolean ->
            if (isGranted) {
                getCurrentLocation { city ->
                    cityName = city
                }
            } else {
                Toast.makeText(this, "Permission denied", Toast.LENGTH_SHORT).show()
            }
        }

    @SuppressLint("MissingPermission")
    private fun getCurrentLocation(onLocationFound: (String) -> Unit) {
        fusedLocationClient.lastLocation
            .addOnCompleteListener(this, OnCompleteListener<Location> { task ->
                if (task.isSuccessful && task.result != null) {
                    val location: Location? = task.result
                    location?.let {
                        val city = getCityName(it.latitude, it.longitude)
                        city?.let(onLocationFound)
                    }
                } else {
                    Toast.makeText(this, "Failed to get location", Toast.LENGTH_SHORT).show()
                }
            })
    }

    private fun getCityName(lat: Double, lon: Double): String? {
        val geocoder = Geocoder(this, java.util.Locale.getDefault())
        return try {
            val addresses = geocoder.getFromLocation(lat, lon, 1)
            if (addresses!!.isNotEmpty()) {
                addresses[0].locality
            } else {
                null
            }
        } catch (e: Exception) {
            e.printStackTrace()
            null
        }
    }
}
