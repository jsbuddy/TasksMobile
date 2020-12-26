package com.example.tasks.ui.settings

import android.os.Bundle
import androidx.appcompat.app.AppCompatDelegate
import androidx.preference.Preference
import androidx.preference.PreferenceFragmentCompat
import com.example.tasks.R

class SettingsFragment : PreferenceFragmentCompat() {

    override fun onCreatePreferences(savedInstanceState: Bundle?, rootKey: String?) {
        setPreferencesFromResource(R.xml.root_preferences, rootKey)
        initialize()
    }

    private fun initialize() {
        val theme: Preference? = preferenceManager.findPreference("theme")
        theme?.setOnPreferenceChangeListener { _, newValue ->
            changeTheme((newValue as String).toInt())
            true
        }
    }

    private fun changeTheme(value: Int) {
        val values = arrayOf(
            AppCompatDelegate.MODE_NIGHT_NO,
            AppCompatDelegate.MODE_NIGHT_YES,
            AppCompatDelegate.MODE_NIGHT_FOLLOW_SYSTEM
        )
        AppCompatDelegate.setDefaultNightMode(values[value])
    }
}