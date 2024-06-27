package dam.a47500.whereto.ui

import android.os.Bundle
import dam.a47500.whereto.R

class PublishActivity : BottomNavActivity() {




    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override val contentViewId: Int
        get() = R.layout.activity_publish

    override val navigationMenuItemId: Int
        get() = R.id.navigation_publish

}