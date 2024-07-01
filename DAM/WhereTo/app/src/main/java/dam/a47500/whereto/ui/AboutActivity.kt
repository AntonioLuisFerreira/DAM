package dam.a47500.whereto.ui

import android.content.Intent
import android.os.Bundle
import android.widget.ImageButton
import dam.a47500.whereto.R

class AboutActivity : BottomNavActivity() {
    private lateinit var backButton: ImageButton
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        backButton = findViewById(R.id.backButton)
        backButton.setOnClickListener {
            goToSettingsActivity()
        }
    }
    private fun goToSettingsActivity() {
        val intent = Intent(this, SettingsActivity::class.java)
        intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
        startActivity(intent)
        finish()
    }

    override val contentViewId: Int
        get() = R.layout.activity_about

    override val navigationMenuItemId: Int
        get() = R.id.navigation_profile
}