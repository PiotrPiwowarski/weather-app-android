package pl.aeh_piotr_aleksandra.weather_app_android

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.LocationOn
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage

@Composable
fun WeatherPage(weatherViewModel: WeatherViewModel, cityName: String) {

    var city by remember {
        mutableStateOf("")
    }

    var location by remember {
        mutableStateOf(cityName)
    }

    val weatherResult = weatherViewModel.weatherResult.observeAsState()

    val keyboardController = LocalSoftwareKeyboardController.current

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(8.dp),
        horizontalAlignment = Alignment.CenterHorizontally)
    {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(8.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceEvenly)
        {
            OutlinedTextField(
                modifier = Modifier.weight(1f),
                value = city,
                onValueChange = {city = it},
                label = { Text(text = "Podaj nazwę miasta")})
            IconButton(onClick = {
                weatherViewModel.getWeather(city)
                keyboardController?.hide()
            }) {
                Icon(
                    imageVector = Icons.Default.Search,
                    contentDescription = "Szukaj po nazwie miasta")
            }
        }
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(8.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceEvenly) {
            Button(onClick = {
                weatherViewModel.getWeather(location)
                keyboardController?.hide()
            }) {
                Icon(
                    imageVector = Icons.Default.LocationOn,
                    contentDescription = "Szukaj dla aktualnej lokalizacji")
                Text(text = "Szukaj dla aktualnej lokalizacji")
            }
        }

        when(val result = weatherResult.value) {
            is Response.NotOk -> {
                Text(text = result.message)
            }
            Response.Waiting -> {
                CircularProgressIndicator()
            }
            is Response.Ok -> {
                WeatherData(data = result.data)
            }
            null -> {}
        }
    }
}

@Composable
fun WeatherData(data: DataModel) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 8.dp),
        horizontalAlignment = Alignment.CenterHorizontally) {

        Text(text = "city: ${data.location.name}", fontSize = 20.sp)
        Text(text = "country: ${data.location.country}", fontSize = 20.sp, color = Color.Gray)

        Spacer(modifier = Modifier.height(16.dp))
        Text(
            text = "${data.current.temp_c} °C",
            fontSize = 56.sp,
            fontWeight = FontWeight.Bold,
            textAlign = TextAlign.Center
        )

        AsyncImage(
            modifier = Modifier.size(160.dp),
            model = "https:${data.current.condition.icon}".replace("64x64", "128x128"),
            contentDescription = "Condition icon"
        )
        Text(
            text = data.current.condition.text,
            fontSize = 20.sp,
            textAlign = TextAlign.Center,
            color = Color.Gray
        )
        Spacer(modifier = Modifier.height(16.dp))
        Column(
            modifier = Modifier.fillMaxWidth()
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceAround
            ) {
                AdditionalData("Humidity", data.current.humidity)
                AdditionalData("Wind speed", data.current.wind_kph + " km/h")
            }
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceAround
            ) {
                AdditionalData("Local time", data.location.localtime.split(" ")[1])
                AdditionalData("Local date",  data.location.localtime.split(" ")[0])
            }
        }
    }
}

@Composable
fun AdditionalData(description: String, value: String) {
    Column(
        modifier = Modifier.padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(text = value, fontSize = 24.sp, fontWeight = FontWeight.Bold, color = Color.Black)
        Text(text = description, fontWeight = FontWeight.SemiBold, color = Color.Black)
    }
}