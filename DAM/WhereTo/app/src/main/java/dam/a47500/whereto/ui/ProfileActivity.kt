package dam.a47500.whereto.ui



import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.EditText

import android.widget.ImageButton
import android.widget.TextView
import android.widget.Toast
import androidx.activity.viewModels
import androidx.core.view.size
import androidx.lifecycle.LiveData
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import dam.a47500.whereto.R
import dam.a47500.whereto.data.Post
import dam.a47500.whereto.database.DBModule
import dam.a47500.whereto.databinding.ActivityPostsBinding
import dam.a47500.whereto.databinding.ActivityProfileBinding
import dam.a47500.whereto.viewmodel.PostViewModel
import dam.a47500.whereto.viewmodel.UserViewModel

class ProfileActivity : BottomNavActivity(){

    private lateinit var textViewProfile: TextView
    private lateinit var textViewEmailValue: TextView

    val viewModel: PostViewModel by viewModels()
    val userviewModel: UserViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        //settings
        val settingsButton = findViewById<ImageButton>(R.id.settings_button)
        settingsButton.setOnClickListener {
            val intent = Intent(this, SettingsActivity::class.java)
            startActivity(intent)
        }
        //myposts
        val profileBinding = binding as ActivityProfileBinding
        var listView = profileBinding.profileRecyclerView


        viewModel.initViewModel(DBModule.getInstance(this).firebasePostsRepository)

        viewModel.posts.observe(this) {

            listView.adapter = it?.let { it1 ->

                PostsAdapter(postList = it1, context = this) { shortpost ->
                    navigateToLongPost(shortpost)
                }
            }
        }

        viewModel.fetchMyPosts()


        //mydata
        val includedLayout = findViewById<View>(R.id.profile)
        textViewProfile = includedLayout.findViewById(R.id.textViewProfile)
        textViewEmailValue = includedLayout.findViewById(R.id.textViewEmailValue)

        userviewModel.initViewModel(DBModule.getInstance(this).firebaseAuthRepository)
        userviewModel.getUserByUID(Firebase.auth.currentUser?.uid.toString())

        userviewModel.user.observe(this) { myUser ->
            if (myUser != null) {
                textViewProfile.text = myUser.username
                textViewEmailValue.text = myUser.email
            } else {
                Toast.makeText(this, "User not found", Toast.LENGTH_SHORT).show()
            }
        }
    }

    override val contentViewId: Int
        get() = R.layout.activity_profile

    override val navigationMenuItemId: Int
        get() = R.id.navigation_profile

    private fun navigateToLongPost(post: Post){
        val intent = Intent(this, LongPostActivity::class.java)
        intent.putExtra("username",post.username)
        intent.putExtra("location",post.location)
        intent.putExtra("capacity",post.capacity.toString())
        intent.putExtra("date",post.date)
        intent.putExtra("entry",post.entry)
        intent.putExtra("security",post.security.toString())
        intent.putExtra("description",post.description)
        intent.putExtra("number_images",post.imageUrls.size)

        for (i in post.imageUrls.indices) {
            intent.putExtra("image_url_$i", post.imageUrls[i])
        }

        startActivity(intent)
    }
}