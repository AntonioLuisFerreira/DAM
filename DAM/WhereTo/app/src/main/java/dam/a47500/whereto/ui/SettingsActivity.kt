package dam.a47500.whereto.ui

import android.content.Context
import android.content.Intent
import android.content.res.Configuration
import android.os.Bundle
import android.widget.Button
import android.widget.ImageButton
import android.widget.ImageView
import android.widget.Toast
import androidx.annotation.OptIn
import androidx.appcompat.app.AppCompatDelegate
import androidx.core.os.LocaleListCompat
import androidx.media3.common.util.Log
import androidx.media3.common.util.UnstableApi
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import dam.a47500.whereto.R
import java.util.Locale

class SettingsActivity: BottomNavActivity() {

    private lateinit var logoutbutton: Button

    @OptIn(UnstableApi::class)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        logoutbutton = findViewById(R.id.logoutbutton)

        val aboutButton = findViewById<Button>(R.id.button_help)
        aboutButton.setOnClickListener {
            val intent = Intent(this, HelpActivity::class.java)
            startActivity(intent)
        }


        val helpButton = findViewById<Button>(R.id.button_about)
        helpButton.setOnClickListener {
            val intent = Intent(this, AboutActivity::class.java)
            startActivity(intent)
        }
        //mudar a linguagem
        val imageViewFirst = findViewById<ImageView>(R.id.imageViewFirst)
        val imageViewSecond = findViewById<ImageView>(R.id.imageViewSecond)
        val imageViewThird = findViewById<ImageView>(R.id.imageViewThird)

        imageViewFirst.setOnClickListener {
            updateLocale("pt") // Set Portuguese locale
        }

        imageViewSecond.setOnClickListener {
            updateLocale("en") // Set English locale
        }

        imageViewThird.setOnClickListener {
            updateLocale("es") // Set Spanish locale
        }

        logoutbutton.setOnClickListener {
            //android.util.Log.i("quero_queijo","Logout butão")
            signOutUser()
            goToMainActivity()
        }
    }

    private fun signOutUser() {
        val auth = Firebase.auth
        auth.signOut()
        Toast.makeText(this, "Signed out successfully.", Toast.LENGTH_SHORT).show()
        // Add code here to navigate to your sign-in or main activity as needed
    }

    private fun goToMainActivity() {
        val intent = Intent(this, PostsActivity::class.java)
        intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
        startActivity(intent)
        finish()
    }

    private fun updateLocale(language: String) {
        AppCompatDelegate.setApplicationLocales(LocaleListCompat.forLanguageTags(language))
    }

    override val contentViewId: Int
        get() = R.layout.activity_settings

    override val navigationMenuItemId: Int
        get() = R.id.navigation_profile
}