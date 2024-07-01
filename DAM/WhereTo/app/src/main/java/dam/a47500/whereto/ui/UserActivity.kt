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
import dam.a47500.whereto.databinding.ActivityUserBinding
import dam.a47500.whereto.viewmodel.PostViewModel
import dam.a47500.whereto.viewmodel.UserViewModel

class UserActivity : BottomNavActivity(){

    private lateinit var textViewProfile: TextView
    private lateinit var textViewEmailValue: TextView
    private lateinit var backButton: ImageButton

    val viewModel: PostViewModel by viewModels()
    val userviewModel: UserViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        backButton = findViewById(R.id.backButton)
        //myposts
        val profileBinding = binding as ActivityUserBinding
        var listView = profileBinding.userRecyclerView


        viewModel.initViewModel(DBModule.getInstance(this).firebasePostsRepository)

        viewModel.posts.observe(this) {

            listView.adapter = it?.let { it1 ->

                PostsAdapter(postList = it1, context = this) { shortpost ->
                    navigateToLongPost(shortpost)
                }
            }
        }
        intent.getStringExtra("username")?.let { viewModel.fetchUserPosts(it) }


        val includedLayout = findViewById<View>(R.id.userProfile)
        textViewProfile = includedLayout.findViewById(R.id.textViewProfile)
        textViewEmailValue = includedLayout.findViewById(R.id.textViewEmailValue)

        userviewModel.initViewModel(DBModule.getInstance(this).firebaseAuthRepository)
        intent.getStringExtra("username")?.let { userviewModel.getUserByName(it) }

        userviewModel.user.observe(this) { myUser ->
            if (myUser != null) {
                textViewProfile.text = myUser.username
                textViewEmailValue.text = myUser.email
            } else {
                Toast.makeText(this, "User not found", Toast.LENGTH_SHORT).show()
            }
        }
        //go back
        backButton.setOnClickListener {
            goToMainActivity()
        }
    }

    override val contentViewId: Int
        get() = R.layout.activity_user

    override val navigationMenuItemId: Int
        get() = R.id.navigation_posts
    private fun goToMainActivity() {
        val intent = Intent(this, PostsActivity::class.java)
        intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
        startActivity(intent)
        finish()
    }
    private fun navigateToLongPost(post: Post){
        val intent = Intent(this, LongPostActivity::class.java)
        intent.putExtra("username",post.username)
        intent.putExtra("author_email",post.email)
        intent.putExtra("location",post.location)
        intent.putExtra("capacity",post.capacity.toString())
        intent.putExtra("date",post.date)
        intent.putExtra("hour",post.hour)
        intent.putExtra("entry",post.entry)
        intent.putExtra("security",post.security.toString())
        intent.putExtra("description",post.description)
        intent.putExtra("number_images",post.imageUrls.size)
        intent.putExtra("priority",post.priority)

        for (i in post.imageUrls.indices) {
            intent.putExtra("image_url_$i", post.imageUrls[i])
        }

        startActivity(intent)
    }
}