package dam.a47500.whereto.ui

import android.os.Bundle
import dam.a47500.whereto.R

class AboutActivity : BottomNavActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override val contentViewId: Int
        get() = R.layout.activity_about

    override val navigationMenuItemId: Int
        get() = R.id.navigation_profile
}