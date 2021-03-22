package dev.sukharev.clipangel.presentation.fragments.settings

import android.os.Bundle
import android.view.View
import androidx.lifecycle.ViewModelProvider
import androidx.preference.Preference
import androidx.preference.PreferenceFragmentCompat
import dev.sukharev.clipangel.R
import dev.sukharev.clipangel.presentation.ToolbarPresenter

class SettingsFragment: PreferenceFragmentCompat() {


    lateinit var viewModel: SettingsViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        viewModel = ViewModelProvider(requireActivity())[SettingsViewModel::class.java]

        (requireActivity() as ToolbarPresenter).apply {
            this.setTitle("Настройки")
            this.setBackToHome(false)
            this.show()
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
//        viewModel.settingsLiveData.observe(viewLifecycleOwner) {
//            val preference: Preference? = findPreference("list_preference_1")
//            preference?.setOnPreferenceClickListener {
//                println()
//                true
//            }
//        }
//
//        viewModel.getSettings()
    }

    override fun onCreatePreferences(savedInstanceState: Bundle?, rootKey: String?) {
        setPreferencesFromResource(R.xml.main_preferences, rootKey)
    }

//    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
//        super.onViewCreated(view, savedInstanceState)
//    }

//    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
//        return inflater.inflate(R.layout.fragment_settings, null)
//    }

}