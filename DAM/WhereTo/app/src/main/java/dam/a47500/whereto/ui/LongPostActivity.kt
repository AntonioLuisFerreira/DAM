package dam.a47500.whereto.ui

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import android.widget.Toast
import androidx.activity.viewModels
import com.denzcoskun.imageslider.ImageSlider
import com.denzcoskun.imageslider.constants.ScaleTypes
import com.denzcoskun.imageslider.models.SlideModel
import dam.a47500.whereto.R
import dam.a47500.whereto.data.Post
import dam.a47500.whereto.database.DBModule
import dam.a47500.whereto.databinding.ActivityPostsBinding
import dam.a47500.whereto.viewmodel.PostViewModel
import java.util.ArrayList
import kotlin.math.log

class LongPostActivity: BottomNavActivity() {

    private var urlList = ArrayList<Uri>()

    private lateinit var textViewProfile: TextView
    private lateinit var textViewLocation: TextView
    private lateinit var textViewCapacity: TextView
    private lateinit var textViewDate2: TextView
    private lateinit var textViewEntranceValue: TextView
    private lateinit var textViewSecurityValue: TextView
    private lateinit var textViewDescriprionValue: TextView
    private lateinit var slider: ImageSlider


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val includedLayout = findViewById<View>(R.id.longPost)


        // Initialize TextViews using the root view of the included layout
        textViewProfile = includedLayout.findViewById(R.id.textViewProfile)
        textViewLocation = includedLayout.findViewById(R.id.textViewLocation)
        textViewCapacity = includedLayout.findViewById(R.id.textViewCapacity)
        textViewDate2 = includedLayout.findViewById(R.id.textViewDate2)
        textViewEntranceValue = includedLayout.findViewById(R.id.textViewEntranceValue)
        textViewSecurityValue = includedLayout.findViewById(R.id.textViewSecurityValue)
        textViewDescriprionValue = includedLayout.findViewById(R.id.textViewDescriprionValue)
        //imagem

        slider = includedLayout.findViewById(R.id.imageSlider)

        // Set data to TextViews
        textViewProfile.text = intent.getStringExtra("username")
        textViewLocation.text = intent.getStringExtra("location")
        textViewCapacity.text = intent.getStringExtra("capacity")
        textViewDate2.text = intent.getStringExtra("date")
        textViewEntranceValue.text = intent.getStringExtra("entry")
        textViewSecurityValue.text = intent.getStringExtra("security")
        textViewDescriprionValue.text = intent.getStringExtra("description")


        val numberImages = intent.getIntExtra("number_images", 0)

        // Create a list to hold all image URLs
        val imageUrls = mutableListOf<String>()


        // Loop through and add each image URL to the list
        for (i in 0..numberImages) {
            val imageUrl = intent.getStringExtra("image_url_$i")
            if (imageUrl != null) {
                imageUrls.add(imageUrl)
            }
        }

        // Add images to the slider
        addImagesToSlider(imageUrls)

        //go to user
        textViewProfile.setOnClickListener {
            Log.i("eu","n")
            intent.getStringExtra("username")?.let { it1 -> goToUserActivity(it1) }
            //Toast.makeText(this, "Profile clicked: ${textViewProfile.text}", Toast.LENGTH_SHORT).show()
        }

    }

    private fun addImagesToSlider(urlList: List<String>){

        val auxList = ArrayList<SlideModel>()

        for(url in urlList) auxList.add(SlideModel(url, ""))

        slider.setImageList(auxList, ScaleTypes.CENTER_CROP)

    }

    private fun goToUserActivity(username: String) {

        val intent =Intent(this, UserActivity::class.java)
        intent.putExtra("username",username)
        startActivity(intent)
        finish()
    }

    override val contentViewId: Int
        get() = R.layout.activty_long_posts

    override val navigationMenuItemId: Int
        get() = R.id.navigation_posts
}
