package dam.a47500.whereto.ui

import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.databinding.ViewDataBinding
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import dam.a47500.whereto.R


    abstract class BottomNavActivity : AppCompatActivity() {
        lateinit var navigationView: BottomNavigationView
        open lateinit var binding: ViewDataBinding
        override fun onCreate(savedInstanceState: Bundle?) {
            super.onCreate(savedInstanceState)
            enableEdgeToEdge()
            this.binding = DataBindingUtil.setContentView(this, contentViewId)

            //setContentView(contentViewId)
            navigationView = findViewById(R.id.navigation)
            navigationView.itemIconTintList = null
            navigationView.setOnItemSelectedListener { item ->
                when(item.itemId) {
                    R.id.navigation_posts -> {
                        // Navigate to PostsActivity
                        startActivity(Intent(this, PostsActivity::class.java))
                        true
                    }
                    R.id.navigation_publish -> {
                        if (Firebase.auth.currentUser != null) {
                            startActivity(Intent(this, PublishActivity::class.java))
                        }
                        else{
                            Toast.makeText(this, "Enter Session", Toast.LENGTH_SHORT).show()
                            startActivity(Intent(this, LoginActivity::class.java))
                        }
                        true
                    }
                    R.id.navigation_profile -> {
                        if (Firebase.auth.currentUser != null) {
                            startActivity(Intent(this, ProfileActivity::class.java))
                        }
                        else{
                            Toast.makeText(this, "Enter Session", Toast.LENGTH_SHORT).show()
                            startActivity(Intent(this, LoginActivity::class.java))
                        }

                        true
                    }
                    else -> false
                }
            }
        }

        override fun onStart() {
            super.onStart()
            updateNavigationBarState()
        }

        // Remove inter-activity transition to avoid screen tossing on tapping bottom navigation items
        public override fun onPause() {
            super.onPause()
            overridePendingTransition(0,0)

        }


        private fun updateNavigationBarState() {
            val actionId = navigationMenuItemId
            selectBottomNavigationBarItem(actionId)
        }

        private fun selectBottomNavigationBarItem(itemId: Int) {
            val item = navigationView.menu.findItem(itemId)
            item.setChecked(true)
        }

        abstract val contentViewId: Int
        abstract val navigationMenuItemId: Int
    }
