package id.develo.gitbros.settings

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import id.develo.gitbros.R

class SettingsActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_settings)

        supportFragmentManager
            .beginTransaction()
            .replace(R.id.settings_holder, MyPreferenceFragment())
            .commit()
    }
}