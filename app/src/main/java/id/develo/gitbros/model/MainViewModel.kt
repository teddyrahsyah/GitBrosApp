package id.develo.gitbros.model

import android.content.Context
import android.util.Log
import android.widget.Toast
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.loopj.android.http.AsyncHttpClient
import com.loopj.android.http.AsyncHttpResponseHandler
import cz.msebera.android.httpclient.Header
import org.json.JSONObject
import java.lang.Exception

class MainViewModel : ViewModel() {

    val listUsers = MutableLiveData<ArrayList<User>>()

    fun setUser(context: Context, defaultUser: String? = "teddyrahsyah212") {
        val listItems = ArrayList<User>()

        val url = "https://api.github.com/search/users?q=${defaultUser}"
        val client = AsyncHttpClient()
        client.addHeader("Authorization", "token b8438da8c17bc27d7c586739fa12b1f8d50db3a5")
        client.addHeader("User-Agent", "request")
        client.get(url, object : AsyncHttpResponseHandler() {
            override fun onSuccess(
                statusCode: Int,
                headers: Array<out Header>?,
                responseBody: ByteArray
            ) {
                val result = String(responseBody)
                Log.d("REST", result)

                try {
                    val jsonObject = JSONObject(result)
                    val items = jsonObject.getJSONArray("items")
                    for (i in 0 until items.length()) {
                        val user = User()
                        val item = items.getJSONObject(i)

                        user.username = item.getString("login")
                        user.avatar = item.getString("avatar_url")
                        user.detail_url = item.getString("url")

                        listItems.add(user)
                    }
                    listUsers.postValue(listItems)
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

    fun getUser(): LiveData<ArrayList<User>> = listUsers

}