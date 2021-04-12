package id.develo.gitbros

import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatDelegate
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.android.material.snackbar.Snackbar
import id.develo.gitbros.adapter.FavUserAdapter
import id.develo.gitbros.databinding.ActivityFavoriteBinding
import id.develo.gitbros.db.FavoriteHelper
import id.develo.gitbros.helper.MappingHelper
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.async
import kotlinx.coroutines.launch

class FavoriteActivity : AppCompatActivity() {

    private lateinit var binding: ActivityFavoriteBinding
    private lateinit var adapter: FavUserAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityFavoriteBinding.inflate(layoutInflater)
        setContentView(binding.root)

        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES)

        val colorDrawable = ColorDrawable(Color.parseColor("#2A2A2A"))
        supportActionBar?.setBackgroundDrawable(colorDrawable)
        supportActionBar?.title = resources.getString(R.string.favorite_users)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        binding.rvFavUser.layoutManager = LinearLayoutManager(this)
        binding.rvFavUser.setHasFixedSize(true)
        adapter = FavUserAdapter(this)
        binding.rvFavUser.adapter = adapter

        showLoading(true)
        loadNotesAsync()
    }

    private fun loadNotesAsync() {
        GlobalScope.launch(Dispatchers.Main) {
            binding.progressBar.visibility = View.VISIBLE

            val favoriteHelper = FavoriteHelper.getInstance(applicationContext)
            favoriteHelper.open()

            val defferedNotes = async(Dispatchers.IO) {
                val cursor = favoriteHelper.queryAll()
                MappingHelper.mapCursorToArrayList(cursor)
            }
            binding.progressBar.visibility = View.INVISIBLE
            val notes = defferedNotes.await()
            if (notes.size > 0) {
                adapter.listFavs = notes
            } else {
                adapter.listFavs = ArrayList()
                showSnackBarMessage("Tidak ada data saat ini...")
            }
            showLoading(false)
            favoriteHelper.close()
        }
    }

    private fun showSnackBarMessage(message: String) {
        Snackbar.make(binding.rvFavUser, message, Snackbar.LENGTH_SHORT).show()
    }

    private fun showLoading(state: Boolean) {
        if (state) {
            binding.progressBar.visibility = View.VISIBLE
        } else {
            binding.progressBar.visibility = View.GONE
        }
    }

    override fun onSupportNavigateUp(): Boolean {
        onBackPressed()
        return true
    }

    override fun onBackPressed() {
        super.onBackPressed()
        finish()
    }
}