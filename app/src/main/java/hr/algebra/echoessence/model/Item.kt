package hr.algebra.echoessence.model

data class Item(
    var id: Long?,
    val title: String,
    val artist: String,
    val coverImagePath: String,
    val releaseDate: String
)
