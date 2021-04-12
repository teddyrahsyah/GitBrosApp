package id.develo.gitbros

import android.annotation.SuppressLint
import android.content.ContentValues
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.annotation.StringRes
import androidx.appcompat.app.AppCompatDelegate
import com.bumptech.glide.Glide
import com.google.android.material.tabs.TabLayoutMediator
import com.loopj.android.http.AsyncHttpClient
import com.loopj.android.http.AsyncHttpResponseHandler
import cz.msebera.android.httpclient.Header
import id.develo.gitbros.adapter.SectionPagerAdapter
import id.develo.gitbros.databinding.ActivityDetailBinding
import id.develo.gitbros.db.DatabaseContract.UserColumns.Companion.COLUMN_AVATAR_URL
import id.develo.gitbros.db.DatabaseContract.UserColumns.Companion.COLUMN_NAME
import id.develo.gitbros.db.DatabaseContract.UserColumns.Companion.COLUMN_REPOSITORY
import id.develo.gitbros.db.DatabaseContract.UserColumns.Companion.COLUMN_USERNAME
import id.develo.gitbros.db.FavoriteHelper
import org.json.JSONObject
import java.lang.Exception


interface IParentActivityCallback {
    fun giveUserUrl(): String?
}

class DetailActivity : AppCompatActivity(), IParentActivityCallback {

    companion object {
        @StringRes
        private val TAB_TITLES = intArrayOf(
            R.string.followers,
            R.string.following
        )
        const val EXTRA_Detail = "extra_detail"
    }

    private lateinit var binding: ActivityDetailBinding
    private lateinit var favoriteHelper: FavoriteHelper

    private var userUrl: String? = null
    private lateinit var name: String
    private lateinit var username: String
    private var repository: Int = 0
    private lateinit var avatar: String
    private var totalFollowers: Int = 0
    private var totalFollowing: Int = 0

    private var statusFavorite: Boolean = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityDetailBinding.inflate(layoutInflater)
        setContentView(binding.root)

        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES)

        favoriteHelper = FavoriteHelper.getInstance(applicationContext)
        favoriteHelper.open()

        val colorDrawable = ColorDrawable(Color.parseColor("#2A2A2A"))
        supportActionBar?.setBackgroundDrawable(colorDrawable)
        supportActionBar?.title = resources.getString(R.string.user_detail)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        val sectionPagerAdapter = SectionPagerAdapter(this)
        binding.viewPager.adapter = sectionPagerAdapter

        supportActionBar?.elevation = 0f
        userUrl = intent.getStringExtra(EXTRA_Detail)

        getUserData(userUrl)

        setStatusFavorite(statusFavorite)
        binding.fabFavorite.setOnClickListener {
            statusFavorite = !statusFavorite
            addDeleteFavorite(statusFavorite)
            setStatusFavorite(statusFavorite)
        }
    }

    fun setStatusFavorite(statusFavorite: Boolean) {
        if (statusFavorite) {
            binding.fabFavorite.setImageResource(R.drawable.ic_star_orange_24)
        } else {
            binding.fabFavorite.setImageResource(R.drawable.ic_star_outline)
        }

    }

    private fun addDeleteFavorite(status: Boolean) {
        if (status) {
            // Adding to Favorite DB
            val values = ContentValues()
            values.put(COLUMN_USERNAME, username)
            values.put(COLUMN_NAME, name)
            Log.d("USER", username)
            values.put(COLUMN_AVATAR_URL, avatar)
            values.put(COLUMN_REPOSITORY, repository.toString())
            favoriteHelper.insert(values)
        }
        else {
            // Deleting to Favorite DB
            favoriteHelper.deleteByUsername(username)
        }
    }

    private fun getUserData(url: String?) {
        val client = AsyncHttpClient()
        client.addHeader("Authorization", "token b8438da8c17bc27d7c586739fa12b1f8d50db3a5")
        client.addHeader("User-Agent", "request")
        client.get(url, object : AsyncHttpResponseHandler() {
            @SuppressLint("StringFormatMatches")
            override fun onSuccess(
                statusCode: Int,
                headers: Array<out Header>?,
                responseBody: ByteArray
            ) {
                val result = String(responseBody)
                try {
                    val jsonObj = JSONObject(result)
                    name = jsonObj.getString("name")
                    username = jsonObj.getString("login")
                    repository = jsonObj.getInt("public_repos")
                    avatar = jsonObj.getString("avatar_url")
                    totalFollowers = jsonObj.getInt("followers")
                    totalFollowing = jsonObj.getInt("following")


                    if (name == "null") {
                        binding.tvDtlName.text = resources.getString(R.string.name)
                    } else {
                        binding.tvDtlName.text = name
                    }

                    binding.tvDtlUsername.text = resources.getString(R.string.username, username)
                    if (repository > 0) {
                        binding.tvDtlRepository.text =
                            resources.getString(R.string.repos, repository)
                    } else {
                        binding.tvDtlRepository.text =
                            resources.getString(R.string.repo, repository)
                    }
                    Glide.with(this@DetailActivity)
                        .load(avatar)
                        .into(binding.imgDtlUser)

                    TabLayoutMediator(binding.tabs, binding.viewPager) { tab, position ->
                        when (position) {
                            0 -> tab.text =
                                resources.getString(TAB_TITLES[position], totalFollowers)
                            1 -> tab.text =
                                resources.getString(TAB_TITLES[position], totalFollowing)
                        }
                    }.attach()
                } catch (e: Exception) {
                    Toast.makeText(
                        this@DetailActivity,
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
                    this@DetailActivity,
                    "Failed to get data!\n$errorMessage",
                    Toast.LENGTH_SHORT
                ).show()
            }
        })
    }

    override fun onSupportNavigateUp(): Boolean {
        onBackPressed()
        return true
    }

    override fun onBackPressed() {
        super.onBackPressed()
        favoriteHelper.close()
        finish()
    }

    override fun giveUserUrl(): String? {
        userUrl = intent.getStringExtra(EXTRA_Detail)
        return userUrl
    }
}
