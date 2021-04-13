package id.develo.gitbros.db

import android.net.Uri
import android.provider.BaseColumns

object DatabaseContract {

    const val AUTHORITY = "id.develo.gitbros"
    const val SCHEME = "content"

    class UserColumns: BaseColumns {
        companion object {
            const val _ID = "_id"
            const val TABLE_NAME = "favorite_user"
            const val COLUMN_USERNAME = "username"
            const val COLUMN_NAME = "name"
            const val COLUMN_AVATAR_URL = "avatar_url"
            const val COLUMN_REPOSITORY = "repository"

            val CONTENT_URI: Uri = Uri.Builder().scheme(SCHEME)
                .authority(AUTHORITY)
                .appendPath(TABLE_NAME)
                .build()
        }
    }
}
