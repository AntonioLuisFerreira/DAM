package dam.a47500.whereto.data

import java.io.Serializable

data class Post(
    var priority: String = "",
    var username: String = "",
    var email: String = "",
    var imageUrls: List<String> = listOf(),
    var location: String = "",
    var capacity: Int = 0,
    var date: String = "",
    var hour: String = "",
    var entry: String = "",
    var security: Boolean = false,
    var description: String = ""
) : Serializable