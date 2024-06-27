package dam.a47500.whereto.data

import java.io.Serializable

data class Post(
    var priority: Int,
    var username: String,
    var imageUrls: List<String>,
    var location: String,
    var capacity: Int,
    var date: String,
    var entry: String,
    var security: Boolean,
    var description: String
) : Serializable