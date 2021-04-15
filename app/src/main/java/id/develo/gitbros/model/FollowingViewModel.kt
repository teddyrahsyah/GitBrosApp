package id.develo.gitbros.model

import android.content.Context
import android.widget.Toast
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.loopj.android.http.AsyncHttpClient
import com.loopj.android.http.AsyncHttpResponseHandler
import cz.msebera.android.httpclient.Header
import org.json.JSONArray

class FollowingViewModel : ViewModel() {

    val listFollowing = MutableLiveData<ArrayList<UserFollow>>()

    fun setFollowers(context: Context?, url: String) {
        val listItems = ArrayList<UserFollow>()

        val client = AsyncHttpClient()
        client.addHeader("Authorization", "token <APITOKEN>")
        client.addHeader("User-Agent", "request")
        client.get(url, object : AsyncHttpResponseHandler() {
            override fun onSuccess(
                statusCode: Int,
                headers: Array<out Header>?,
                responseBody: ByteArray
            ) {
                val result = String(responseBody)
                try {
                    val items = JSONArray(result)
                    for (i in 0 until items.length()) {
                        val userFollowers = UserFollow()
                        val followersObject = items.getJSONObject(i)

                        userFollowers.username = followersObject.getString("login")
                        userFollowers.avatar = followersObject.getString("avatar_url")

                        listItems.add(userFollowers)
                    }
                    listFollowing.postValue(listItems)
                } catch (e: Exception) {
                    Toast.makeText(
                        context,
                        "Failed to get data!\n${e.message.toString()}",
                        Toast.LENGTH_SHORT
                    ).show()
                }
            }

            override fun onFailure(
                statusCode: Int,
                headers: Array<out Header>?,
                responseBody: ByteArray?,
                error: Throwable?
            ) {
                val errorMessage = when (statusCode) {
                    401 -> "$statusCode : Bad Request"
                    403 -> "$statusCode : Forbidden"
                    404 -> "$statusCode : Not Found"
                    else -> "$statusCode : ${error?.message}"
                }
                Toast.makeText(
                    context,
                    "Failed to get data!\n$errorMessage",
                    Toast.LENGTH_SHORT
                ).show()
            }
        })
    }

    fun getFollowers(): LiveData<ArrayList<UserFollow>> = listFollowing
}