package dam.a47500.coolweather


import android.annotation.SuppressLint
import android.content.Context
import android.graphics.drawable.Drawable
import android.util.Log
import androidx.core.content.ContextCompat
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.google.gson.Gson
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import java.io.InputStreamReader
import java.net.URL
import java.util.*


class MyViewModel : ViewModel() {

    var slp            = MutableLiveData<String>()
    var wind_direction = MutableLiveData<String>()
    var wind_speed     = MutableLiveData<String>()
    var temperature    = MutableLiveData<String>()
    var time_zone      = MutableLiveData<String>()
    var location       = MutableLiveData<String>()
    var description    = MutableLiveData<String>()
    var icon = MutableLiveData<Drawable>()
    var icon_name = ""

    @SuppressLint("DiscouragedApi")
    fun fetchWeatherData(lat: Float, long: Float, context: Context, day: Boolean, packageName: String) {
        GlobalScope.launch(Dispatchers.IO) {

            val weather = WeatherAPI_Call(lat, long)
            location.postValue("Latitude: $lat Longitude $long")
            temperature.postValue(weather.current_weather.temperature.toString() + "ºC")
            slp.postValue(weather.hourly.pressure_msl[12].toString() + " hPa")
            wind_direction.postValue(weather.current_weather.winddirection.toString())
            wind_speed.postValue(weather.current_weather.windspeed.toString() + "Km/h")
            time_zone.postValue(weather.current_weather.time)

            description.postValue(getWeatherCodeMap()[weather.current_weather.weathercode].toString().replace('_', ' '))

            icon_name = getWeatherCodeMap()[weather.current_weather.weathercode]?.image.toString()

            val resourceId = context.resources.getIdentifier(modifyIconName(icon_name,day), "drawable", context.packageName)

            icon.postValue(ContextCompat.getDrawable(context, resourceId))
            wallpaper.postValue(if(day) ContextCompat.getDrawable(context, R.drawable.sunny_bg) else ContextCompat.getDrawable(context, R.drawable.night_bg))

        }
    }

    /*fun getCurrentHour(latitude: Float, longitude: Float): Any? {
        var resposta = "";
        val reqString = buildString {
            append(" https://api.wheretheiss.at/v1/coordinates/")
            append("${latitude},${longitude}")
        }
        val url = URL(reqString)
        url.openStream().use {
            resposta = Gson().fromJson(InputStreamReader(it, "UTF-8"), WeatherData::class.java).toString()
        }

        Log.i("Resposta", resposta)
        return null;
    }*/

    private fun modifyIconName(icon_name: String, day:Boolean): String {

        if (icon_name.endsWith("_")) {

            return    if(day) icon_name + "day" else icon_name + "night"
        } else {
            Log.i("testetes","modifyIconName(icon_name,day)")
            return icon_name
        }
    }

    private fun WeatherAPI_Call(lat: Float, long: Float) : WeatherData {
        val reqString = buildString {
            append("https://api.open-meteo.com/v1/forecast?")
            append("latitude=${lat}&longitude=${long}&")
            append("current_weather=true&")
            append("hourly=temperature_2m,relativehumidity_2m,pressure_msl,apparent_temperature,windspeed_10m,temperature_80m,uv_index&daily=weathercode,temperature_2m_max,temperature_2m_min,precipitation_probability_max&timezone=Europe%2FLondon")
        }
        val url = URL(reqString)
        url.openStream().use {
            return Gson().fromJson(InputStreamReader(it, "UTF-8"), WeatherData::class.java)
        }
    }

    var wallpaper = MutableLiveData<Drawable>()


    fun updateTheme(day: Boolean, context: Context) {


    }

}