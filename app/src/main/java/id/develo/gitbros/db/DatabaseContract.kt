package id.develo.gitbros.db

import android.provider.BaseColumns

class DatabaseContract {
    class UserColumns: BaseColumns {
        companion object {
            const val _ID = "_id"
            const val TABLE_NAME = "favorite_user"
            const val COLUMN_USERNAME = "username"
            const val COLUMN_NAME = "name"
            const val COLUMN_AVATAR_URL = "avatar_url"
            const val COLUMN_REPOSITORY = "repository"
        }
    }
}