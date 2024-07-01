package dam.a47500.whereto.database

import android.annotation.SuppressLint
import android.os.Build
import android.util.Log
import androidx.annotation.RequiresApi
import androidx.lifecycle.MutableLiveData
import com.google.firebase.auth.ktx.auth
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.GenericTypeIndicator
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import com.google.type.Date
import dam.a47500.whereto.data.Post
import dam.a47500.whereto.data.User
import kotlinx.coroutines.tasks.await
import java.time.LocalDate
import java.time.LocalDateTime
import java.time.LocalTime
import java.time.format.DateTimeFormatter
import java.util.prefs.Preferences

class FirebaseFirestore {



    private val database: FirebaseDatabase = FirebaseDatabase.getInstance("https://whereto-ba143-default-rtdb.europe-west1.firebasedatabase.app/")
    private val userReference: DatabaseReference = database.getReference("user")
    private val postsReference: DatabaseReference = database.getReference("post")


    @RequiresApi(Build.VERSION_CODES.O)
    /*suspend fun readPosts(local: String): List<Post> {

        val dataSnapshot = postsReference.get().await()
        val postList = mutableListOf<Post>()

        // Normalize the input local for comparison
        val normalizedLocal = local.replace(" ", "").lowercase()

        for (snapshot in dataSnapshot.children) {
            val priority = snapshot.key.toString()
            val username = snapshot.child("username").getValue(String::class.java) ?: ""
            val email = snapshot.child("email").getValue(String::class.java) ?: ""
            val imageUrls = snapshot.child("imageUrls").getValue(object : GenericTypeIndicator<List<String>>() {}) ?: emptyList()
            val location = snapshot.child("location").getValue(String::class.java) ?: ""
            val capacity = snapshot.child("capacity").getValue(Int::class.java) ?: 0
            val date = snapshot.child("date").getValue(String::class.java) ?: ""
            val hour = snapshot.child("hour").getValue(String::class.java) ?: ""
            val entry = snapshot.child("entry").getValue(String::class.java) ?: ""
            val security = snapshot.child("security").getValue(Boolean::class.java) ?: false
            val description = snapshot.child("description").getValue(String::class.java) ?: ""

            val post = Post(priority, username, email, imageUrls, location, capacity, date, hour, entry, security, description)

            // Normalize the location for comparison
            val normalizedLocation = location.replace(" ", "").lowercase()
            // Check location filter
            if (normalizedLocation == normalizedLocal || local.isEmpty()) {
                postList.add(post)
            }
        }

        return postList
    }*/

    suspend fun readPosts(local: String): List<Post> {
        val dataSnapshot = postsReference.get().await()
        val postList = mutableListOf<Post>()

        // Normalize the input local for comparison
        val normalizedLocal = local.replace(" ", "").lowercase()

        // Get the current date
        val currentDate = LocalDate.now()

        for (snapshot in dataSnapshot.children) {
            val priority = snapshot.key.toString()
            val username = snapshot.child("username").getValue(String::class.java) ?: ""
            val email = snapshot.child("email").getValue(String::class.java) ?: ""
            val imageUrls = snapshot.child("imageUrls").getValue(object : GenericTypeIndicator<List<String>>() {}) ?: emptyList()
            val location = snapshot.child("location").getValue(String::class.java) ?: ""
            val capacity = snapshot.child("capacity").getValue(Int::class.java) ?: 0
            val date = snapshot.child("date").getValue(String::class.java) ?: ""
            val hour = snapshot.child("hour").getValue(String::class.java) ?: ""
            val entry = snapshot.child("entry").getValue(String::class.java) ?: ""
            val security = snapshot.child("security").getValue(Boolean::class.java) ?: false
            val description = snapshot.child("description").getValue(String::class.java) ?: ""

            val post = Post(priority, username, email, imageUrls, location, capacity, date, hour, entry, security, description)

            // Normalize the location for comparison
            val normalizedLocation = location.replace(" ", "").lowercase()

            // Check location filter and date filter
            if ((normalizedLocation == normalizedLocal || local.isEmpty()) && date.isNotEmpty()) {
                val postDate = LocalDate.parse(date)
                if (!postDate.isBefore(currentDate)) {
                    postList.add(post)
                }
            }
        }

        return postList.sortedWith(compareBy({ LocalDate.parse(it.date) }, { LocalTime.parse(it.hour) }))
    }

    @RequiresApi(Build.VERSION_CODES.O)
    suspend fun readMyPosts(): List<Post> {

        val dataSnapshot = postsReference.get().await()

        val postList = mutableListOf<Post>()

        for (snapshot in dataSnapshot.children) {
            val priority = snapshot.key.toString()

            val username = snapshot.child("username").getValue(String::class.java) ?: ""
            val email = snapshot.child("email").getValue(String::class.java) ?: ""
            val imageUrls = snapshot.child("imageUrls").getValue(object : GenericTypeIndicator<List<String>>() {}) ?: emptyList()
            val location = snapshot.child("location").getValue(String::class.java) ?: ""
            val capacity = snapshot.child("capacity").getValue(Int::class.java) ?: 0
            val date = snapshot.child("date").getValue(String::class.java) ?: ""
            val hour = snapshot.child("hour").getValue(String::class.java) ?: ""
            val entry = snapshot.child("entry").getValue(String::class.java) ?: ""
            val security = snapshot.child("security").getValue(Boolean::class.java) ?: false
            val description = snapshot.child("description").getValue(String::class.java) ?: ""

            val post = Post(priority,username,email, imageUrls, location, capacity, date,hour, entry, security, description)

            if(getUID(username) == Firebase.auth.uid.toString()){
                postList.add(post)
            }

        }

        return postList.sortedWith(compareBy({ LocalDate.parse(it.date) }, { LocalTime.parse(it.hour) }))
    }

    @RequiresApi(Build.VERSION_CODES.O)
    suspend fun readUserPosts(userName: String): List<Post> {

        val dataSnapshot = postsReference.get().await()

        val postList = mutableListOf<Post>()

        for (snapshot in dataSnapshot.children) {
            val priority = snapshot.key.toString()

            val username = snapshot.child("username").getValue(String::class.java) ?: ""
            val email = snapshot.child("email").getValue(String::class.java) ?: ""
            val imageUrls = snapshot.child("imageUrls").getValue(object : GenericTypeIndicator<List<String>>() {}) ?: emptyList()
            val location = snapshot.child("location").getValue(String::class.java) ?: ""
            val capacity = snapshot.child("capacity").getValue(Int::class.java) ?: 0
            val date = snapshot.child("date").getValue(String::class.java) ?: ""
            val hour = snapshot.child("hour").getValue(String::class.java) ?: ""
            val entry = snapshot.child("entry").getValue(String::class.java) ?: ""
            val security = snapshot.child("security").getValue(Boolean::class.java) ?: false
            val description = snapshot.child("description").getValue(String::class.java) ?: ""

            val post = Post(priority,username,email, imageUrls, location, capacity, date,hour, entry, security, description)

            if(username == userName){
                postList.add(post)
            }

        }

        return postList.sortedWith(compareBy({ LocalDate.parse(it.date) }, { LocalTime.parse(it.hour) }))
    }

    suspend fun getLastID(): String {
        val dataSnapshot = postsReference.orderByKey().limitToLast(1).get().await()

        return if (dataSnapshot.exists()) {
            // Get the last key
            val lastKey = dataSnapshot.children.first().key ?: "0"

            // Increment the last key and return as String
            (lastKey.toInt() + 1).toString()
        } else {
            "1" // Default ID if no posts exist yet
        }
    }

    suspend fun writePost(post: Post) {
        postsReference.child(getLastID()).setValue(post).await()
    }

    fun deletePost(id: String){
        postsReference.child(id).removeValue()

    }

    suspend fun getUserByUID(id: String): User {

        val dataSnapshot = userReference.get().await()

        for (snapshot in dataSnapshot.children) {
            val username = snapshot.child("username").getValue(String::class.java) ?: ""
            val email = snapshot.child("email").getValue(String::class.java) ?: ""
            val uid = snapshot.child("uid").getValue(String::class.java) ?: ""
            if(uid == id){
                return User(username, email, uid)

            }
        }
        throw NoSuchElementException("User with id $id not found")
    }

    suspend fun getUserByName(userName: String): User {

        val dataSnapshot = userReference.get().await()

        for (snapshot in dataSnapshot.children) {
            val username = snapshot.child("username").getValue(String::class.java) ?: ""
            val email = snapshot.child("email").getValue(String::class.java) ?: ""
            val uid = snapshot.child("uid").getValue(String::class.java) ?: ""
            if(username == userName){
                return User(username, email, uid)

            }
        }
        throw NoSuchElementException("User with username $userName not found")
    }


    suspend fun writeUser(user: User){
        userReference.child(user.username).setValue(user).await()
    }

    suspend fun getUID(username: String): String {
        val dataSnapshot = userReference.get().await()
        for (snapshot in dataSnapshot.children) {
            val user = snapshot.child("username").getValue(String::class.java) ?: ""
            val uid = snapshot.child("uid").getValue(String::class.java) ?: ""
            if(user == username){
                return uid
            }
        }
        return ""
    }

}