package dam.a47500.helloworldoptional

import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.TextView

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        // Get a reference to the MultiLineTextView
        val deviceInfoTextView: TextView = findViewById(R.id.editTextTextMultiLine)

        // Put info in a string
        val deviceInfo = StringBuilder()
        deviceInfo.append("Manufacturer: ").append(Build.MANUFACTURER).append("\n")
        deviceInfo.append("Model: ").append(Build.MODEL).append("\n")
        deviceInfo.append("Brand: ").append(Build.BRAND).append("\n")
        deviceInfo.append("Type: ").append(Build.TYPE).append("\n")
        deviceInfo.append("User: ").append(Build.USER).append("\n")

        deviceInfo.append("Base: ").append(Build.VERSION_CODES.BASE).append("\n")
        deviceInfo.append("Incremental: ").append(Build.VERSION.INCREMENTAL).append("\n")
        deviceInfo.append("SDK: ").append(Build.VERSION.SDK_INT).append("\n")
        deviceInfo.append("Version Code: ").append(Build.VERSION.RELEASE).append("\n")
        deviceInfo.append("Display: ").append(Build.DISPLAY)

        // Add string created to the MultiLineTextView
        deviceInfoTextView.text = deviceInfo.toString()
    }
}