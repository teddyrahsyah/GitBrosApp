package id.develo.gitbros

import android.database.ContentObserver
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.net.Uri
import android.os.Bundle
import android.os.Handler
import android.os.HandlerThread
import android.util.Log
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.app.AppCompatDelegate
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.android.material.snackbar.Snackbar
import id.develo.gitbros.adapter.FavUserAdapter
import id.develo.gitbros.databinding.ActivityFavoriteBinding
import id.develo.gitbros.db.DatabaseContract.UserColumns.Companion.CONTENT_URI
import id.develo.gitbros.db.FavoriteHelper
import id.develo.gitbros.helper.MappingHelper
import id.develo.gitbros.model.FavoriteUser
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

        adapter = FavUserAdapter()
        adapter.notifyDataSetChanged()

        binding.rvFavUser.layoutManager = LinearLayoutManager(this)
        binding.rvFavUser.adapter = adapter
        binding.rvFavUser.setHasFixedSize(true)

        val handlerThread = HandlerThread("DataObserver")
        handlerThread.start()
        val handler = Handler(handlerThread.looper)

        showLoading(true)
        loadNotesAsync()
        
        val myObserver = object : ContentObserver(handler){
            override fun onChange(selfChange: Boolean) {
                super.onChange(selfChange)
                loadNotesAsync()
            }
        }

        contentResolver.registerContentObserver(CONTENT_URI, true, myObserver)

        // Callback Method when you click Delete Button (ImageButton in RecyclerView Item)
        adapter.setOnItemClickCallback(object : FavUserAdapter.OnItemClickCallback {
            override fun onItemClicked(uriWithId: Uri) {
                // It should delete the item in database but in actual app it doesn't.
                contentResolver.delete(uriWithId, null, null)
                loadNotesAsync()
            }
        })
    }

    private fun loadNotesAsync() {
        GlobalScope.launch(Dispatchers.Main) {
            binding.progressBar.visibility = View.VISIBLE

            val defferedNotes = async(Dispatchers.IO) {
                val cursor = contentResolver.query(CONTENT_URI, null, null, null, null)
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