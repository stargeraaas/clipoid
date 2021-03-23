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

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

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