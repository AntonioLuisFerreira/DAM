package dam.a47500.whereto.ui

import android.app.Activity
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Button
import android.widget.CheckBox
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.activity.viewModels
import com.denzcoskun.imageslider.ImageSlider
import com.denzcoskun.imageslider.constants.ScaleTypes
import com.denzcoskun.imageslider.models.SlideModel
import com.google.android.gms.tasks.Tasks
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.FirebaseStorage
import dam.a47500.whereto.R
import dam.a47500.whereto.data.Post
import dam.a47500.whereto.database.DBModule
import dam.a47500.whereto.viewmodel.PostViewModel
import dam.a47500.whereto.viewmodel.UserViewModel
import java.text.SimpleDateFormat
import java.util.ArrayList
import java.util.Locale
import java.util.UUID


private const val PICK_IMAGES_REQUEST = 1
class PublishActivity : BottomNavActivity() {


    val userviewModel: UserViewModel by viewModels()
    val postviewModel: PostViewModel by viewModels()

    //butoes
    private lateinit var imagebutton: Button
    private lateinit var publishbutton: Button

    //texto
    private lateinit var usernameEditText: TextView
    private lateinit var locationEditText: EditText
    private lateinit var capacityEditText: EditText
    private lateinit var dataEditText: EditText
    private lateinit var hourEditText: EditText
    private lateinit var descriptionEditText: EditText
    private lateinit var entryEditText: EditText
    private lateinit var slider: ImageSlider
    //checkbox
    private lateinit var checkBox: CheckBox


    private var imageUris: MutableList<Uri> = mutableListOf()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // Initialize UI components
        initializeUIComponents()

        // Initialize view models
        initializeViewModels()

        // Fetch current user information
        fetchCurrentUser()

        // Set up button click listeners
        setupButtonClickListeners()
    }

    private fun initializeUIComponents() {
        val includedLayout = findViewById<View>(R.id.publish)

        imagebutton = includedLayout.findViewById(R.id.buttonAddImage)
        publishbutton = includedLayout.findViewById(R.id.buttonSubmit)
        locationEditText = includedLayout.findViewById(R.id.editTextLocation)
        capacityEditText = includedLayout.findViewById(R.id.editTextCapacity)
        dataEditText = includedLayout.findViewById(R.id.editTextDate)
        hourEditText = includedLayout.findViewById(R.id.editTextHours)
        entryEditText = includedLayout.findViewById(R.id.editTextEntryValue)
        descriptionEditText = includedLayout.findViewById(R.id.editTextDescriptionValue)
        checkBox = includedLayout.findViewById(R.id.checkBox)
        //imagens
        slider = includedLayout.findViewById(R.id.imageSlider)
    }

    private fun initializeViewModels() {
        postviewModel.initViewModel(DBModule.getInstance(this).firebasePostsRepository)
        userviewModel.initViewModel(DBModule.getInstance(this).firebaseAuthRepository)
    }

    private fun fetchCurrentUser() {
        userviewModel.getUserByUID(Firebase.auth.currentUser?.uid.toString())
        userviewModel.user.observe(this) { myUser ->
            if (myUser != null) {
                myUser.username
                myUser.email
            } else {
                Toast.makeText(this, "User not found", Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun setupButtonClickListeners() {
        imagebutton.setOnClickListener {
            openFileChooser()
        }

        publishbutton.setOnClickListener {
            val capacity: Int? = capacityEditText.text.toString().toIntOrNull()
            if (capacity != null) {


                Log.i("capacidade", capacity.toString());
                userviewModel.user.value?.let { myUser ->

                    //createAndPublishPost(myUser.username,myUser.email,capacity, listOf("https://firebasestorage.googleapis.com/v0/b/whereto-ba143.appspot.com/o/2379b3a1-781b-4dc3-b09e-6db65bae2705.jpg?alt=media&token=78a1d6c9-8449-43fd-9419-6fdca16cd720"))

                    if (imageUris.isNotEmpty()) {

                        uploadImagesAndCreatePost(imageUris, myUser.username, myUser.email, capacity)
                    } else {
                        Toast.makeText(this, "No images selected", Toast.LENGTH_SHORT).show()
                    }

                } ?: run {
                    Toast.makeText(this, "User data is not available", Toast.LENGTH_SHORT).show()
                }

            } else {
                Toast.makeText(this, "Invalid capacity", Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun openFileChooser() {
        val intent = Intent()
        intent.type = "image/*"
        intent.putExtra(Intent.EXTRA_ALLOW_MULTIPLE, true)
        intent.action = Intent.ACTION_GET_CONTENT
        startActivityForResult(Intent.createChooser(intent, "Select Picture"), PICK_IMAGES_REQUEST)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == PICK_IMAGES_REQUEST && resultCode == Activity.RESULT_OK) {
            if (data?.clipData != null) {
                // Multiple images selected
                val count = data.clipData!!.itemCount
                for (i in 0 until count) {
                    val imageUri = data.clipData!!.getItemAt(i).uri
                    imageUris.add(imageUri)
                }
            } else if (data?.data != null) {
                // Single image selected
                val imageUri = data.data!!
                imageUris.add(imageUri)
            }
        }
        addImagesToSlider(imageUris)
    }

    private fun addImagesToSlider(uriList: List<Uri>) {
        val slideModels = ArrayList<SlideModel>()

        for (uri in uriList) {
            val imageUrl = uri.toString()
            slideModels.add(SlideModel(imageUrl, ""))
        }

        slider.setImageList(slideModels, ScaleTypes.CENTER_CROP)
    }

    
    private fun uploadImagesAndCreatePost(imageUris: List<Uri>, username: String, email: String, capacity: Int) {

        val storageRef = FirebaseStorage.getInstance().reference
        Log.i("info", "Starting upload tasks")

        val uploadTasks = imageUris.map { imageUri ->
            val imageName = "${UUID.randomUUID()}.jpg"
            val imageRef = storageRef.child(imageName)
            imageRef.putFile(imageUri).continueWithTask { task ->
                if (!task.isSuccessful) {
                    Log.e("error", "Image upload failed for $imageUri", task.exception)
                    task.exception?.let { throw it }
                } else {
                    Log.i("info", "Image upload succeeded for $imageUri")
                }
                imageRef.downloadUrl
            }
        }

        Tasks.whenAllSuccess<Uri>(uploadTasks)
            .addOnSuccessListener { uris ->
                val imageUrls = uris.map { it.toString() }
                Log.i("info", "All image URLs: $imageUrls")
                if (imageUrls.size == imageUris.size) {
                    createAndPublishPost(username, email, capacity, imageUrls)
                } else {
                    Log.e("error", "Mismatch in imageUrls and imageUris size")
                }
            }
            .addOnFailureListener { exception ->
                Toast.makeText(this, "Image upload failed: ${exception.message}", Toast.LENGTH_SHORT).show()
                Log.e("error", "Failed to upload images", exception)
            }

        Log.i("info", "Upload tasks initiated")
    }

    private fun createAndPublishPost(username: String, email: String, capacity: Int, imageUrls: List<String>) {

        val inputDateFormat = SimpleDateFormat("dd:MM:yyyy", Locale.getDefault())
        val outputDateFormat = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())
        val inputDate = dataEditText.text.toString()
        val formattedDate = try {
            val date = inputDateFormat.parse(inputDate)
            date?.let { outputDateFormat.format(it) } ?: ""
        } catch (e: Exception) {
            ""
        }

        val post = Post(
            "0",
            username,
            email,
            imageUrls,
            locationEditText.text.toString(),
            capacity,
            formattedDate,
            hourEditText.text.toString(),
            entryEditText.text.toString(),
            checkBox.isChecked,
            descriptionEditText.text.toString()
        )

        postviewModel.writePost(post)

        goToMainActivity()
    }

    private fun goToMainActivity() {
        val intent = Intent(this, PostsActivity::class.java)
        intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
        startActivity(intent)
        finish()
    }

    override val contentViewId: Int
        get() = R.layout.activity_publish

    override val navigationMenuItemId: Int
        get() = R.id.navigation_publish

}