package id.develo.gitbros.model

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class FavoriteUser (
    var id: Int = 0,
    var username: String? = null,
    var name: String? = null,
    var avatar_url: String? = null,
    var repository: Int = 0
) : Parcelable