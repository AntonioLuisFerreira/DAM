package dam.a47500.whereto.database

import android.os.Build
import android.util.Log
import androidx.annotation.RequiresApi
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import dam.a47500.whereto.data.Post
import dam.a47500.whereto.data.User

class FirebasePostsRepository(private val firebaseFirestore: FirebaseFirestore) {

    @RequiresApi(Build.VERSION_CODES.O)
    suspend fun getPosts(): LiveData<List<Post>> {

        //teste
        /*val post = Post(
            priority = 7,
            username = "antFerreira",
            imageUrls = listOf("url1"),
            location = "Lisboa",
            capacity = 50,
            date = "2024-06-28",
            entry = "20 €",
            security = false,
            description = "This is a sample post description."
        )*/
        //firebaseFirestore.writePosts(post)

        val postsLiveData = firebaseFirestore.readPosts()

        if (postsLiveData.isEmpty()) {

            //não há nada

        } else {
            return MutableLiveData(postsLiveData)
        }
        return MutableLiveData()
    }

    suspend fun getMyPosts(): LiveData<List<Post>> {
        val postsLiveData = firebaseFirestore.readMyPosts()

        if (postsLiveData.isEmpty()) {

            //não há nada

        } else {
            return MutableLiveData(postsLiveData)
        }
        return MutableLiveData()
    }

    suspend fun getUserPosts(userName: String): LiveData<List<Post>> {
        val postsLiveData = firebaseFirestore.readUserPosts(userName)

        if (postsLiveData.isEmpty()) {

            //não há nada

        } else {
            return MutableLiveData(postsLiveData)
        }
        return MutableLiveData()
    }

    suspend fun getUserUid(username: String): String {
        val userUid = firebaseFirestore.getUID(username)

        return userUid
    }
}