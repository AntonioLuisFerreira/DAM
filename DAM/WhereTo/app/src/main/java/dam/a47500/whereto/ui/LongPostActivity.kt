package dam.a47500.whereto.ui

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageButton
import android.widget.TextView
import android.widget.Toast
import androidx.activity.viewModels
import com.denzcoskun.imageslider.ImageSlider
import com.denzcoskun.imageslider.constants.ScaleTypes
import com.denzcoskun.imageslider.models.SlideModel
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.ktx.storage
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
    private lateinit var editTextHours: TextView
    private lateinit var textViewEntranceValue: TextView
    private lateinit var textViewSecurityValue: TextView
    private lateinit var textViewDescriprionValue: TextView
    private lateinit var slider: ImageSlider
    private lateinit var backButton: ImageButton
    private val storage = Firebase.storage

    private lateinit var deleteButton: Button
    val viewModel: PostViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val includedLayout = findViewById<View>(R.id.longPost)


        // Initialize TextViews using the root view of the included layout
        textViewProfile = includedLayout.findViewById(R.id.textViewProfile)
        textViewLocation = includedLayout.findViewById(R.id.textViewLocation)
        textViewCapacity = includedLayout.findViewById(R.id.textViewCapacity)
        textViewDate2 = includedLayout.findViewById(R.id.textViewDate2)
        editTextHours = includedLayout.findViewById(R.id.textViewHours)

        textViewEntranceValue = includedLayout.findViewById(R.id.textViewEntranceValue)
        textViewSecurityValue = includedLayout.findViewById(R.id.textViewSecurityValue)
        textViewDescriprionValue = includedLayout.findViewById(R.id.textViewDescriprionValue)
        deleteButton = includedLayout.findViewById(R.id.btnDeletePost)
        deleteButton.visibility = View.INVISIBLE

        backButton = findViewById(R.id.backButton)

        //imagem
        slider = includedLayout.findViewById(R.id.imageSlider)

        // Set data to TextViews
        textViewProfile.text = intent.getStringExtra("username")
        textViewLocation.text = intent.getStringExtra("location")
        textViewCapacity.text = intent.getStringExtra("capacity")
        textViewDate2.text = intent.getStringExtra("date")
        editTextHours.text = intent.getStringExtra("hour")
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

        // Check if logged-in user can delete the post
        val postAuthorEmail = intent.getStringExtra("author_email")

        viewModel.initViewModel(DBModule.getInstance(this).firebasePostsRepository)

        if (postAuthorEmail != null && postAuthorEmail.toString() == Firebase.auth.currentUser?.email.toString()) {
            deleteButton.visibility = View.VISIBLE // Show delete button
            deleteButton.setOnClickListener {

                val id = intent.getStringExtra("priority").toString()
                viewModel.deletePost(id)
                deleteImages(imageUrls)
                goToMainActivity()
            }
        }

        //go to user
        textViewProfile.setOnClickListener {
            intent.getStringExtra("username")?.let { it1 -> goToUserActivity(it1) }

        }

        //go back
        backButton.setOnClickListener {
            goToMainActivity()
        }

    }

    fun deleteImages(imageUrls: List<String>) {
        val storageRef = storage.reference

        for (url in imageUrls) {
            // Convert URL to a StorageReference
            val imageRef = storageRef.child(getPathFromUrl(url))

            // Delete the file
            imageRef.delete()
                .addOnSuccessListener {
                    // File deleted successfully
                    println("File deleted successfully")
                }
                .addOnFailureListener { exception ->
                    // Handle any errors
                    println("Error deleting file: $exception")
                }
        }
    }

    private fun getPathFromUrl(url: String): String {
        // Extract the path from the URL
        // Example: "https://firebasestorage.googleapis.com/v0/b/your-bucket-name/o/images%2Fimage1.jpg?alt=media&token=xxxxxx"
        // Extract "images/image1.jpg" as the path
        val startIndex = url.indexOf("/o/") + 3 // +3 to skip "/o/"
        val endIndex = url.indexOf("?") // Stop before the "?"
        return url.substring(startIndex, endIndex)
    }

    private fun addImagesToSlider(urlList: List<String>){

        val auxList = ArrayList<SlideModel>()

        for(url in urlList) auxList.add(SlideModel(url, ""))

        slider.setImageList(auxList, ScaleTypes.CENTER_CROP)

    }
    private fun goToMainActivity() {
        val intent = Intent(this, PostsActivity::class.java)
        intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
        startActivity(intent)
        finish()
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
