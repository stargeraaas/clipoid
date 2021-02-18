package dev.sukharev.clipangel.presentation.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.SavedStateHandle
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.dialog.MaterialDialogs
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.google.firebase.crashlytics.internal.common.CrashlyticsCore
import com.google.firebase.crashlytics.internal.model.CrashlyticsReport
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase
import dev.sukharev.clipangel.R
import dev.sukharev.clipangel.domain.channel.models.Channel
import dev.sukharev.clipangel.domain.channel.ChannelInteractor
import dev.sukharev.clipangel.domain.channel.models.ChannelCredentials
import dev.sukharev.clipangel.presentation.ToolbarPresenter
import dev.sukharev.clipangel.presentation.fragments.AttachDeviceFragment.Companion.CHANNEL_ID_EXTRA
import dev.sukharev.clipangel.presentation.fragments.AttachDeviceFragment.Companion.CHANNEL_SECRET_EXTRA
import dev.sukharev.clipangel.presentation.fragments.AttachDeviceFragment.Companion.RESULT_ERROR
import dev.sukharev.clipangel.presentation.fragments.AttachDeviceFragment.Companion.RESULT_ERROR_MESSAGE
import dev.sukharev.clipangel.presentation.fragments.AttachDeviceFragment.Companion.RESULT_OK
import dev.sukharev.clipangel.presentation.fragments.AttachDeviceFragment.Companion.SCAN_RESULT
import dev.sukharev.clipangel.presentation.recycler.ChannelItemVM
import dev.sukharev.clipangel.presentation.recycler.ChannelRecyclerAdapter
import dev.sukharev.clipangel.presentation.viewmodels.ChannelListViewModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import org.koin.android.ext.android.inject
import org.koin.android.viewmodel.ext.android.viewModel
import java.util.*


class ChannelsFragment : BaseFragment(), View.OnClickListener {

    private lateinit var devicesRecyclerView: RecyclerView
    private lateinit var attachDeviceButton: FloatingActionButton


    private val channelAdapter = ChannelRecyclerAdapter()

    private val channelViewModel: ChannelListViewModel by viewModel()

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_devices, null)
    }

    override fun initToolbar(presenter: ToolbarPresenter) {
        presenter.setTitle("Мои устройства")
        presenter.setBackToHome(false)
        presenter.show()
    }

    override fun showBottomNavigation(): Boolean = true

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        devicesRecyclerView = view.findViewById(R.id.devices_recycler)
        attachDeviceButton = view.findViewById(R.id.attach_device)
        attachDeviceButton.setOnClickListener(this)
        devicesRecyclerView.apply {
            adapter = channelAdapter
            layoutManager = LinearLayoutManager(requireContext())
        }
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        manageQrResult()
        channelViewModel.isLoadingLiveData.observe(requireActivity()){
            it?.let {

            }
        }
    }

    private fun manageQrResult() {
        findNavController().currentBackStackEntry?.savedStateHandle?.apply {
            when(get<Int>(SCAN_RESULT)) {
                RESULT_OK -> doOnSuccessQrResult(this)
                RESULT_ERROR -> doOnFailureQrResult(this)
            }
        }
    }

    private fun doOnSuccessQrResult(state: SavedStateHandle) {
        channelViewModel.createChannel(ChannelCredentials(state.get<String>(CHANNEL_ID_EXTRA)!!,
                        state.get<String>(CHANNEL_SECRET_EXTRA)!!))
    }

    private fun doOnFailureQrResult(state: SavedStateHandle) {
        println(state.get<String>(RESULT_ERROR_MESSAGE))
    }

    override fun onClick(view: View?) {
        when (view?.id) {
            R.id.attach_device -> NavHostFragment.findNavController(this)
                    .navigate(R.id.attachDeviceFragment)
        }
    }


}