package id.develo.gitbros

import android.app.SearchManager
import android.content.Context
import android.content.Intent
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.provider.Settings
import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
import android.view.View
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatDelegate
import androidx.appcompat.widget.SearchView
import androidx.recyclerview.widget.LinearLayoutManager
import id.develo.gitbros.adapter.UserAdapter
import id.develo.gitbros.databinding.ActivityMainBinding
import id.develo.gitbros.model.MainViewModel
import id.develo.gitbros.reminder.AlarmReceiver
import id.develo.gitbros.settings.SettingsActivity

class MainActivity : AppCompatActivity() {

    private lateinit var adapter: UserAdapter
    private lateinit var binding: ActivityMainBinding
    private lateinit var alarmReceiver: AlarmReceiver
    private val mainViewModel: MainViewModel by viewModels()

    private lateinit var reminderString: String

    override fun onCreate(savedInstanceState: Bundle?) {
        binding = ActivityMainBinding.inflate(layoutInflater)
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES)

        alarmReceiver = AlarmReceiver()

        val colorDrawable = ColorDrawable(Color.parseColor("#2A2A2A"))
        supportActionBar?.setBackgroundDrawable(colorDrawable)

        adapter = UserAdapter()
        adapter.notifyDataSetChanged()

        binding.rvUser.layoutManager = LinearLayoutManager(this)
        binding.rvUser.adapter = adapter

        mainViewModel.getUser().observe(this, { user ->
            if (user != null) {
                adapter.setData(user)
                binding.tvSearch.visibility = View.GONE
                showLoading(false)
            }
        })
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        val inflater: MenuInflater = menuInflater
        inflater.inflate(R.menu.main_menu, menu)

        val searchManager = getSystemService(Context.SEARCH_SERVICE) as SearchManager
        val searchView = menu.findItem(R.id.action_search).actionView as SearchView

        searchView.setSearchableInfo(searchManager.getSearchableInfo(componentName))
        searchView.queryHint = resources.getString(R.string.query_hint)
        searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String?): Boolean {
                showLoading(true)
                binding.tvSearch.visibility = View.GONE
                mainViewModel.setUser(this@MainActivity, query)
                return true
            }

            override fun onQueryTextChange(newText: String?): Boolean {
                return false
            }
        })
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            R.id.action_settings -> {
                val mIntent = Intent(this, SettingsActivity::class.java)
                startActivity(mIntent)
                true
            }
            R.id.action_fav -> {
                Intent(this@MainActivity, FavoriteActivity::class.java).also {
                    it.flags = Intent.FLAG_ACTIVITY_SINGLE_TOP
                    startActivity(it)
                }
                true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }

    private fun showLoading(state: Boolean) {
        if (state) {
            binding.progressBar.visibility = View.VISIBLE
        } else {
            binding.progressBar.visibility = View.GONE
        }
    }

    override fun onSupportNavigateUp(): Boolean {
        showLoading(false)
        return super.onSupportNavigateUp()
    }
}