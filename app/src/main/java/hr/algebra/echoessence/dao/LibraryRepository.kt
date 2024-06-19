package hr.algebra.echoessence.dao

import android.content.ContentValues
import android.content.Context
import android.database.Cursor
import hr.algebra.echoessence.model.Library

class LibraryRepository(context: Context) {
    private val dbHelper = DatabaseHelper(context)

    fun addLibraryEntry(library: Library): Long {
        val db = dbHelper.writableDatabase
        val values = ContentValues().apply {
            put("user_id", library.userId)
            put("album", library.albumTitle)
            put("song", library.songTitle)
            put("duration", library.duration)
            put("album_cover_url", library.albumCoverUrl)
            put("artist_name", library.artistName)
            put("artist_id", library.artistId)
        }
        val id = db.insert("library", null, values)
        db.close()
        return id
    }

    fun getLibraryEntriesByUserId(userId: Int): List<Library> {
        val db = dbHelper.readableDatabase
        val cursor: Cursor = db.query(
            "library", arrayOf("id", "user_id", "album", "song", "duration", "album_cover_url", "artist_name", "artist_id"), "user_id = ?",
            arrayOf(userId.toString()), null, null, null
        )
        val libraries = mutableListOf<Library>()
        while (cursor.moveToNext()) {
            val id = cursor.getLong(cursor.getColumnIndexOrThrow("id"))
            val album = cursor.getString(cursor.getColumnIndexOrThrow("album"))
            val song = cursor.getString(cursor.getColumnIndexOrThrow("song"))
            val duration = cursor.getInt(cursor.getColumnIndexOrThrow("duration"))
            val albumCoverUrl = cursor.getString(cursor.getColumnIndexOrThrow("album_cover_url"))
            val artistName = cursor.getString(cursor.getColumnIndexOrThrow("artist_name"))
            val artistId = cursor.getInt(cursor.getColumnIndexOrThrow("artist_id"))
            libraries.add(Library(id, userId, album, song, duration, albumCoverUrl, artistName, artistId))
        }
        cursor.close()
        db.close()
        return libraries
    }

    fun deleteLibraryEntry(libraryId: Long): Int {
        val db = dbHelper.writableDatabase
        val rowsAffected = db.delete("library", "id = ?", arrayOf(libraryId.toString()))
        db.close()
        return rowsAffected
    }
}
