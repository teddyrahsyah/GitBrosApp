package id.develo.gitbros.db

import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper
import id.develo.gitbros.db.DatabaseContract.UserColumns.Companion.COLUMN_AVATAR_URL
import id.develo.gitbros.db.DatabaseContract.UserColumns.Companion.COLUMN_NAME
import id.develo.gitbros.db.DatabaseContract.UserColumns.Companion.COLUMN_REPOSITORY
import id.develo.gitbros.db.DatabaseContract.UserColumns.Companion.COLUMN_URL
import id.develo.gitbros.db.DatabaseContract.UserColumns.Companion.COLUMN_USERNAME
import id.develo.gitbros.db.DatabaseContract.UserColumns.Companion.TABLE_NAME
import id.develo.gitbros.db.DatabaseContract.UserColumns.Companion._ID

internal class DatabaseHelper(context: Context) :
    SQLiteOpenHelper(context, DATABASE_NAME, null, DATABASE_VERSION) {

    companion object {
        private const val DATABASE_NAME = "db_gitbros"
        private const val DATABASE_VERSION = 1
        private const val SQL_CREATE_TABLE_FAV =
            "CREATE TABLE $TABLE_NAME" +
                    "($_ID INTEGER PRIMARY KEY AUTOINCREMENT," +
                    "$COLUMN_USERNAME TEXT NOT NULL UNIQUE," +
                    "$COLUMN_NAME TEXT NOT NULL," +
                    "$COLUMN_AVATAR_URL TEXT NOT NULL," +
                    "$COLUMN_REPOSITORY INTEGER NOT NULL," +
                    "$COLUMN_URL TEXT NOT NULL)"
    }

    override fun onCreate(db: SQLiteDatabase?) {
        db?.execSQL(SQL_CREATE_TABLE_FAV)
    }

    override fun onUpgrade(db: SQLiteDatabase?, p1: Int, p2: Int) {
        db?.execSQL("DROP TABLE IF EXISTS $TABLE_NAME")
        onCreate(db)
    }
}