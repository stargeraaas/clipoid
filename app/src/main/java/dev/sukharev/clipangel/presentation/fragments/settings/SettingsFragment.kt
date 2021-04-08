package dev.sukharev.clipangel.presentation.fragments.settings

import android.os.Bundle
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.lifecycle.ViewModelProvider
import androidx.preference.Preference
import androidx.preference.PreferenceFragmentCompat
import androidx.preference.PreferenceGroup
import androidx.recyclerview.widget.RecyclerView
import dev.sukharev.clipangel.BuildConfig
import dev.sukharev.clipangel.R
import dev.sukharev.clipangel.data.local.repository.credentials.Credentials
import dev.sukharev.clipangel.presentation.ToolbarPresenter
import org.koin.android.ext.android.inject

class SettingsFragment: PreferenceFragmentCompat() {

    lateinit var viewModel: SettingsViewModel

    private val credentials: Credentials by inject()

    override fun onCreate(savedInstanceState: Bundle?) {
        requireActivity().setTheme(R.style.Preferences)
        super.onCreate(savedInstanceState)

        viewModel = ViewModelProvider(requireActivity())[SettingsViewModel::class.java]

        (requireActivity() as ToolbarPresenter).let { presenter ->
            presenter.getToolbar()?.apply {
                title = getString(R.string.settings)
                navigationIcon = null
                presenter.setToolbar(this)
            }
            presenter.show()
        }
    }

    override fun onCreatePreferences(savedInstanceState: Bundle?, rootKey: String?) {
        setPreferencesFromResource(R.xml.main_preferences, rootKey)
        setSummaryInAboutPref("app_device_identifier", credentials.getDeviceIdentifier())
        setSummaryInAboutPref("app_version_text", BuildConfig.VERSION_NAME)
    }

    override fun onCreateRecyclerView(inflater: LayoutInflater?, parent: ViewGroup?, savedInstanceState: Bundle?): RecyclerView {
        val recyclerView = super.onCreateRecyclerView(inflater, parent, savedInstanceState)
        recyclerView.overScrollMode = RecyclerView.OVER_SCROLL_NEVER
        return recyclerView
    }

    private fun setSummaryInAboutPref(key: String, text: String) {
        (preferenceScreen.getPreference(1) as PreferenceGroup)
                .findPreference<Preference>(key)?.summary = text
    }

}