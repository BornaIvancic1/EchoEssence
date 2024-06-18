package hr.algebra.echoessence.dao

import android.content.ContentValues
import android.content.Context
import hr.algebra.echoessence.model.User
import hr.algebra.echoessence.dao.DatabaseHelper

class UserRepository(context: Context) {
    private val dbHelper = DatabaseHelper(context)

    fun addUser(user: User): Long {
        val db = dbHelper.writableDatabase

        // Check if user already exists
        val cursor = db.query(
            "users", arrayOf("id"), "email = ?",
            arrayOf(user.email), null, null, null
        )
        if (cursor.moveToFirst()) {
            cursor.close()
            db.close()
            return -1 // User already exists
        }
        cursor.close()

        // Insert new user
        val values = ContentValues().apply {
            put("id", user.id)
            put("email", user.email)
            put("first_name", user.firstName)
            put("last_name", user.lastName)
        }
        val id = db.insert("users", null, values)
        db.close()
        return id
    }
}
