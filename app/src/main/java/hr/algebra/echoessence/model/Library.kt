package hr.algebra.echoessence.model

data class Library(
    val id: Long,
    val userId: Int,
    val albumTitle: String,
    val songTitle: String,
    val duration: Int,
    val albumCoverUrl: String,
    val artistName: String,
    val artistId: Int
)