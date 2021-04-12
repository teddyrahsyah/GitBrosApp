package id.develo.gitbros.helper

import android.database.Cursor
import android.provider.ContactsContract
import id.develo.gitbros.db.DatabaseContract
import id.develo.gitbros.db.DatabaseContract.UserColumns.Companion.COLUMN_AVATAR_URL
import id.develo.gitbros.db.DatabaseContract.UserColumns.Companion.COLUMN_NAME
import id.develo.gitbros.db.DatabaseContract.UserColumns.Companion.COLUMN_REPOSITORY
import id.develo.gitbros.db.DatabaseContract.UserColumns.Companion.COLUMN_USERNAME
import id.develo.gitbros.db.DatabaseContract.UserColumns.Companion._ID
import id.develo.gitbros.model.FavoriteUser

object MappingHelper {
    fun mapCursorToArrayList(favoritesCursor: Cursor?): ArrayList<FavoriteUser> {
        val notesList = ArrayList<FavoriteUser>()

        favoritesCursor?.apply {
            while (moveToNext()) {
                val id = getInt(getColumnIndexOrThrow(_ID))
                val username = getString(getColumnIndexOrThrow(COLUMN_USERNAME))
                val name = getString(getColumnIndexOrThrow(COLUMN_NAME))
                val avatarUrl = getString(getColumnIndexOrThrow(COLUMN_AVATAR_URL))
                val repos = getInt(getColumnIndexOrThrow(COLUMN_REPOSITORY))

                notesList.add(FavoriteUser(id, username, name, avatarUrl,repos))
            }
        }
        return notesList
    }
}