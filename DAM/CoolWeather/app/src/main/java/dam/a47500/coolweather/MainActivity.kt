package  dam.a47500.coolweather

import android.content.pm.PackageManager
import android.location.Location
import android.os.Build
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.app.AppCompatDelegate
import androidx.core.app.ActivityCompat
import androidx.databinding.DataBindingUtil
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices
import com.google.android.gms.location.Priority.PRIORITY_HIGH_ACCURACY
import com.google.android.gms.tasks.Task
import dam.a47500.coolweather.databinding.ActivityMainBinding
import java.util.Calendar


class MainActivity : AppCompatActivity() {

    // Theme
    private var day:Boolean = (Calendar.getInstance().get(Calendar.HOUR_OF_DAY) in 8..19)

    // Location
    private lateinit var fusedLocationClient: FusedLocationProviderClient
    private var latitude   : Double = 38.74
    private var longitude  : Double = -9.14

    // ViewModel
    private lateinit var myViewModel: MyViewModel
    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {

        super.onCreate(savedInstanceState)
        if(day)
            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)
        else
            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES)

        setContentView(R.layout.activity_main)

        //location
        fusedLocationClient = LocationServices.getFusedLocationProviderClient(this)
        getLocation()

        //Binding
        binding = DataBindingUtil.setContentView(this, R.layout.activity_main)
        binding.lifecycleOwner = this

        //ViewModel
        myViewModel = MyViewModel()
        binding.setVariable(1, myViewModel)

        myViewModel.fetchWeatherData(latitude.toFloat(), longitude.toFloat(), this, day, packageName)
        myViewModel.updateTheme(day, this)

        //Button action
        clickButton()
    }

    private fun clickButton(){

        val latitudeInput: EditText = findViewById(R.id.latitudeInput)
        val longitudeInput: EditText = findViewById(R.id.longitudeInput)

        val myButton: Button = findViewById(R.id.updateButton)
        myButton.setOnClickListener {
            val lat = latitudeInput.text.toString().toFloatOrNull()
            val log = longitudeInput.text.toString().toFloatOrNull()
            if(lat != null && log != null)
                myViewModel.fetchWeatherData(lat, log, this, day, packageName)
            else
                myViewModel.fetchWeatherData(latitude.toFloat(), longitude.toFloat(), this, day, packageName)
        }
    }

    private fun getLocation(){

        if (Build.MODEL.startsWith("sdk")
            || "google_sdk".equals(Build.MODEL.toString())
            || Build.MODEL.contains("Emulator")
            || Build.MODEL.contains("Android SDK")) {
            return
        }

        val task:Task<Location> = fusedLocationClient.getCurrentLocation(PRIORITY_HIGH_ACCURACY, null)

        if(ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_FINE_LOCATION)
            != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED)
        {
            ActivityCompat.requestPermissions(this, arrayOf(android.Manifest.permission.ACCESS_FINE_LOCATION), 101)
            return
        }

        task.addOnSuccessListener {
            if (it != null) {
                latitude  = it.latitude
                longitude = it.longitude
            }
        }
    }






}