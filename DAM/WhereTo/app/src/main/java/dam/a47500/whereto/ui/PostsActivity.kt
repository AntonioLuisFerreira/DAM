package dam.a47500.whereto.ui

import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.view.View
import android.widget.EditText
import android.widget.ImageButton
import androidx.activity.viewModels
import androidx.annotation.RequiresApi
import dam.a47500.whereto.R
import dam.a47500.whereto.data.Post
import dam.a47500.whereto.database.DBModule
import dam.a47500.whereto.databinding.ActivityPostsBinding
import dam.a47500.whereto.viewmodel.PostViewModel

class PostsActivity : BottomNavActivity() {

    val viewModel: PostViewModel by viewModels()

    private lateinit var editTextSearchLocation: EditText
    private lateinit var image_search_Button: ImageButton
    private lateinit var location: String
    @RequiresApi(Build.VERSION_CODES.O)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val includedLayout = findViewById<View>(R.id.search_bar)

        val postBinding = binding as ActivityPostsBinding
        var listView = postBinding.postsRecyclerView

        image_search_Button = includedLayout.findViewById(R.id.image_search_Button)
        editTextSearchLocation = includedLayout.findViewById(R.id.editTextSearchLocation)


        viewModel.initViewModel(DBModule.getInstance(this).firebasePostsRepository)

        viewModel.posts.observe(this) {
            listView.adapter = it?.let { it1 ->
                PostsAdapter(postList = it1, context = this) { shortpost ->

                    navigateToLongPost(shortpost)
                }
            }
        }
        viewModel.fetchPosts("")
        //buscar a localização
        image_search_Button.setOnClickListener {
            location = editTextSearchLocation.text.toString()
            viewModel.fetchPosts(location)
        }

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

    override val contentViewId: Int
        get() = R.layout.activity_posts

    override val navigationMenuItemId: Int
        get() = R.id.navigation_posts
}