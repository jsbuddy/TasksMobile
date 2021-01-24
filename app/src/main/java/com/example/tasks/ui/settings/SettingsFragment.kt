package com.example.tasks.ui.settings

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatDelegate
import androidx.appcompat.widget.Toolbar
import androidx.preference.Preference
import androidx.preference.PreferenceFragmentCompat
import com.example.tasks.R

class SettingsFragment : PreferenceFragmentCompat() {

    override fun onCreatePreferences(savedInstanceState: Bundle?, rootKey: String?) {
        setPreferencesFromResource(R.xml.root_preferences, rootKey)
        initialize()
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val root = super.onCreateView(inflater, container, savedInstanceState)!!
        setupToolbar(root)
        return root
    }

    private fun setupToolbar(root: View) {
        root.findViewById<Toolbar>(R.id.toolbar).apply {
            title = "Settings"
            setNavigationIcon(R.drawable.ic_baseline_arrow_back_24)
            setNavigationOnClickListener { requireActivity().onBackPressed() }
        }
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