package hr.algebra.echoessence.dao
import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper

class DatabaseHelper(context: Context) : SQLiteOpenHelper(context, DATABASE_NAME, null, DATABASE_VERSION) {

    companion object {
        private const val DATABASE_NAME = "echoessence.db"
        private const val DATABASE_VERSION = 1

        private const val TABLE_USERS = "users"
        private const val COLUMN_USER_ID = "id"
        private const val COLUMN_USER_EMAIL = "email"
        private const val COLUMN_USER_FIRST_NAME = "first_name"
        private const val COLUMN_USER_LAST_NAME = "last_name"

        private const val TABLE_DATA = "data"
        private const val COLUMN_DATA_ID = "id"
        private const val COLUMN_DATA_TITLE = "title"

        private const val TABLE_LIBRARY = "library"
        private const val COLUMN_LIBRARY_ID = "id"
        private const val COLUMN_LIBRARY_USER_ID = "user_id"
        private const val COLUMN_LIBRARY_DATA_ID = "data_id"
        private const val COLUMN_LIBRARY_ALBUM = "album"
        private const val COLUMN_LIBRARY_DURATION = "duration"
        private const val COLUMN_LIBRARY_ALBUM_COVER_URL = "album_cover_url"
        private const val COLUMN_LIBRARY_ARTIST_NAME = "artist_name"
        private const val COLUMN_LIBRARY_ARTIST_ID = "artist_id"
    }

    override fun onCreate(db: SQLiteDatabase) {
        val createUsersTable = ("CREATE TABLE $TABLE_USERS ("
                + "$COLUMN_USER_ID INTEGER PRIMARY KEY AUTOINCREMENT,"
                + "$COLUMN_USER_EMAIL TEXT,"
                + "$COLUMN_USER_FIRST_NAME TEXT,"
                + "$COLUMN_USER_LAST_NAME TEXT)")

        val createDataTable = ("CREATE TABLE $TABLE_DATA ("
                + "$COLUMN_DATA_ID INTEGER PRIMARY KEY AUTOINCREMENT,"
                + "$COLUMN_DATA_TITLE TEXT)"
                )

        val createLibraryTable = ("CREATE TABLE $TABLE_LIBRARY ("
                + "$COLUMN_LIBRARY_ID INTEGER PRIMARY KEY AUTOINCREMENT,"
                + "$COLUMN_LIBRARY_USER_ID INTEGER,"
                + "$COLUMN_LIBRARY_DATA_ID INTEGER,"
                + "$COLUMN_LIBRARY_ALBUM TEXT,"
                + "$COLUMN_LIBRARY_DURATION INTEGER,"
                + "$COLUMN_LIBRARY_ALBUM_COVER_URL TEXT,"
                + "$COLUMN_LIBRARY_ARTIST_NAME TEXT,"
                + "$COLUMN_LIBRARY_ARTIST_ID INTEGER,"
                + "FOREIGN KEY($COLUMN_LIBRARY_USER_ID) REFERENCES $TABLE_USERS($COLUMN_USER_ID),"
                + "FOREIGN KEY($COLUMN_LIBRARY_DATA_ID) REFERENCES $TABLE_DATA($COLUMN_DATA_ID))")

        db.execSQL(createUsersTable)
        db.execSQL(createDataTable)
        db.execSQL(createLibraryTable)
    }

    override fun onUpgrade(db: SQLiteDatabase, oldVersion: Int, newVersion: Int) {
        db.execSQL("DROP TABLE IF EXISTS $TABLE_USERS")
        db.execSQL("DROP TABLE IF EXISTS $TABLE_DATA")
        db.execSQL("DROP TABLE IF EXISTS $TABLE_LIBRARY")
        onCreate(db)
    }
}
