package dam.a47500.coolweather

data class WeatherData (
    var latitude : String,
    var longitude : String,
    var timezone : String ,
    var current_weather : CurrentWeather,
    var hourly : Hourly,
    var daily : Daily
)
data class CurrentWeather (
    var temperature : Float ,
    var windspeed : Float ,
    var winddirection : Int ,
    var weathercode : Int ,
    var time : String
)

data class Hourly (
    var time : ArrayList<String>,
    var temperature_2m : ArrayList<Float>,
    var relativehumidity_2m : ArrayList<Int>,
    var apparent_temperature : ArrayList<Float>,
    var windspeed_10m : ArrayList<Float>,
    var weathercode : ArrayList<Int>,
    var pressure_msl : ArrayList<Double>,
    var uv_index : ArrayList<Double>
)
data class Daily (
    var weathercode : ArrayList<Int> ,
    var temperature_2m_max : ArrayList<Float> ,
    var temperature_2m_min : ArrayList<Float> ,
    var precipitation_probability_max : ArrayList<Int> ,
    var time : ArrayList<String>
)

enum class WMO_WeatherCode (var code : Int , var image : String ) {
    CLEAR_SKY (0,"clear_"),
    MAINLY_CLEAR (1, "mostly_clear_"),
    PARTLY_CLOUDY (2,"partly_cloudy_"),
    CLOUDY (3,"cloudy"),
    FOG (45 ,"fog"),
    DEPOSITING_RIME_FOG (48 ,"fog"),
    DRIZZLE_LIGHT (51 , "drizzle"),
    DRIZZLE_MODERATE (53 , "drizzle"),
    DRIZZLE_DENSE (55 ,"drizzle"),
    FREEZING_DRIZZLE_LIGHT (56 ,"freezing_drizzle"),
    FREEZING_DRIZZLE_DENSE (57 ,"freezing_drizzle"),
    RAIN_SLIGHT (61 ,"rain_light"),
    RAIN_MODERATE (63 ,"rain"),
    RAIN_HEAVY (65 ,"rain_heavy"),
    FREEZING_RAIN_LIGHT (66 ,"freezing_rain_light"),
    FREEZING_RAIN_HEAVY (67 ,"freezing_rain_heavy"),
    SNOW_FALL_SLIGHT (71 ,"snow_light"),
    SNOW_FALL_MODERATE (73 ,"snow"),
    SNOW_FALL_HEAVY (75 ,"snow_heavy"),
    SNOW_GRAINS (77 , "snow"),
    RAIN_SHOWERS_SLIGHT (80 , "rain_light"),
    RAIN_SHOWERS_MODERATE (81 , "rain_light"),
    RAIN_SHOWERS_VIOLENT (82 , "rain_heavy"),
    SNOW_SHOWERS_SLIGHT (85 , "snow_light"),
    SNOW_SHOWERS_HEAVY (86 , "snow_heavy"),
    THUNDERSTORM_SLIGHT_MODERATE (95 , "tstorm"),
    THUNDERSTORM_HAIL_SLIGHT (96 ,"tstorm"),
    THUNDERSTORM_HAIL_HEAVY (99 ,"tstorm")
}

fun getWeatherCodeMap () : Map <Int, WMO_WeatherCode> {
    var weatherMap = HashMap<Int,WMO_WeatherCode>()
    WMO_WeatherCode.values().forEach {
        weatherMap.put( it.code, it )
    }
    return weatherMap
}