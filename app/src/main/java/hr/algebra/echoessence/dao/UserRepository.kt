package hr.algebra.echoessence.dao

import android.content.ContentValues
import android.content.Context
import hr.algebra.echoessence.model.User

class UserRepository(context: Context) {
    private val dbHelper = DatabaseHelper(context)

    fun addUser(user: User): Long {
        val db = dbHelper.writableDatabase

        val cursor = db.query(
            "users", arrayOf("id"), "email = ?",
            arrayOf(user.email), null, null, null
        )
        if (cursor.moveToFirst()) {
            cursor.close()
            db.close()
            return -1
        }
        cursor.close()

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
