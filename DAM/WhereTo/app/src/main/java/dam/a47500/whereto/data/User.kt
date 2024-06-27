package dam.a47500.whereto.data

import java.io.Serializable

data class User(
    val username : String,
    val email: String,
    val uid: String
): Serializable