package id.develo.gitbros.settings

import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import android.provider.Settings
import android.widget.Toast
import androidx.preference.Preference
import androidx.preference.PreferenceFragmentCompat
import androidx.preference.SwitchPreference
import id.develo.gitbros.R
import id.develo.gitbros.reminder.AlarmReceiver

class MyPreferenceFragment() : PreferenceFragmentCompat(),
    SharedPreferences.OnSharedPreferenceChangeListener {

    private lateinit var LANGUAGE: String
    private lateinit var STATE_REMINDER: String

    private lateinit var languagePreference: Preference
    private lateinit var reminderPreference: SwitchPreference

    private lateinit var alarmReceiver: AlarmReceiver

    override fun onCreatePreferences(savedInstanceState: Bundle?, rootKey: String?) {
        addPreferencesFromResource(R.xml.settings_screen)
        init()
        setSummaries()
    }

    override fun onResume() {
        super.onResume()
        preferenceScreen.sharedPreferences.registerOnSharedPreferenceChangeListener(this)
    }

    override fun onPause() {
        super.onPause()
        preferenceScreen.sharedPreferences.unregisterOnSharedPreferenceChangeListener(this)
    }

    private fun init() {

        alarmReceiver = AlarmReceiver()
        LANGUAGE = resources.getString(R.string.key_lang)
        STATE_REMINDER = resources.getString(R.string.key_reminder)

        languagePreference = findPreference<Preference>(LANGUAGE) as Preference
        reminderPreference = findPreference<SwitchPreference>(STATE_REMINDER) as SwitchPreference

        languagePreference.setOnPreferenceClickListener {
            moveToLocale()
            true
        }
    }

    private fun moveToLocale() {
        val intent = Intent(Settings.ACTION_LOCALE_SETTINGS)
        startActivity(intent)
    }

    private fun setSummaries() {
        val sh = preferenceManager.sharedPreferences
        languagePreference.summary = sh.getString(LANGUAGE, resources.getString(R.string.languages))
        reminderPreference.isChecked = sh.getBoolean(STATE_REMINDER, false)
    }

    override fun onSharedPreferenceChanged(sharedPreference: SharedPreferences, key: String) {
        if (key == STATE_REMINDER) {
            reminderPreference.isChecked = sharedPreference.getBoolean(STATE_REMINDER, false)
            setOrUnsetAlarm(reminderPreference.isChecked)
        }
    }

    fun setOrUnsetAlarm(stateReminder: Boolean) {
        if (stateReminder) {
            val myMessage = "Let's find out new github users!"
            alarmReceiver.setRepeatingAlarm(
                context,
                AlarmReceiver.TYPE_REPEATING,
                "09:00",
                myMessage
            )
            Toast.makeText(context, "Reminder is ON!", Toast.LENGTH_SHORT).show()
        } else {
            alarmReceiver.cancelAlarm(context, AlarmReceiver.TYPE_REPEATING)
            Toast.makeText(context, "Reminder is OFF!", Toast.LENGTH_SHORT).show()
        }
    }
}