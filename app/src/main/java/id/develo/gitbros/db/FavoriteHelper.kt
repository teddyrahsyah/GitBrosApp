package id.develo.gitbros.db

import android.content.ContentValues
import android.content.Context
import android.database.Cursor
import android.database.SQLException
import android.database.sqlite.SQLiteDatabase
import id.develo.gitbros.db.DatabaseContract.UserColumns.Companion.COLUMN_USERNAME
import id.develo.gitbros.db.DatabaseContract.UserColumns.Companion.TABLE_NAME
import id.develo.gitbros.db.DatabaseContract.UserColumns.Companion._ID

class FavoriteHelper(context: Context) {

    companion object {
        const val DATABASE_TABLE = TABLE_NAME
        private lateinit var databaseHelper: DatabaseHelper
        private lateinit var database: SQLiteDatabase

        private var INSTANCE: FavoriteHelper? = null
        fun getInstance(context: Context):FavoriteHelper =
            INSTANCE ?: synchronized(this) {
                INSTANCE ?: FavoriteHelper(context)
            }
    }

    init {
        databaseHelper = DatabaseHelper(context)
    }

    fun open() {
        database = databaseHelper.writableDatabase
    }

    fun close() {
        databaseHelper.close()
        if (database.isOpen) database.close()
    }

    @Throws(SQLException::class)
    fun queryAll(): Cursor {
        return database.query(
            DATABASE_TABLE,
            null,
            null,
            null,
            null,
            null,
            "$_ID ASC"
        )
    }

    fun insert(values: ContentValues?): Long {
        return database.insert(DATABASE_TABLE, null, values)
    }

    fun deleteByUsername(username: String): Int {
        return database.delete(DATABASE_TABLE, "$COLUMN_USERNAME = \"$username\"", null)
    }
}