package dam.a47500.whereto.database

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import dam.a47500.whereto.data.Post

import dam.a47500.whereto.data.User


class FirebaseAuthRepository(private val firebaseFirestore: FirebaseFirestore) {
    suspend fun setUser(user: User){
        firebaseFirestore.writeUser(user)
    }

    suspend fun getUserByUID(uid: String): User {
        val userData = firebaseFirestore.getUserByUID(uid)

        return userData
    }

    suspend fun getUserByName(username: String): User {
        val userData = firebaseFirestore.getUserByName(username)

        return userData
    }
}