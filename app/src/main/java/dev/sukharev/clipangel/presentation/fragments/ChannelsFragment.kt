package dev.sukharev.clipangel.presentation.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.lifecycle.Observer
import androidx.lifecycle.SavedStateHandle
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.floatingactionbutton.FloatingActionButton
import dev.sukharev.clipangel.R
import dev.sukharev.clipangel.domain.channel.models.Channel
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
import dev.sukharev.clipangel.presentation.view.info.InformationView
import dev.sukharev.clipangel.presentation.viewmodels.ChannelListViewModel
import kotlinx.coroutines.*
import org.koin.android.viewmodel.ext.android.viewModel


class ChannelsFragment : BaseFragment(), View.OnClickListener {

    private lateinit var devicesRecyclerView: RecyclerView
    private lateinit var attachDeviceButton: FloatingActionButton
    private var loadingContent: ConstraintLayout? = null

    private var emptyChannelsLayout: InformationView? = null
    private var errorLayout: InformationView? = null

    private val channelAdapter = ChannelRecyclerAdapter()

    private val channelViewModel: ChannelListViewModel by viewModel()

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_devices, null)
    }

    private val channelStateObserver = Observer<ChannelFragmentState> {
        when (it) {
            is ChannelFragmentState.Loading -> {
                println()
                devicesRecyclerView.visibility = View.INVISIBLE
                errorLayout?.visibility = View.INVISIBLE
                loadingContent?.visibility = View.VISIBLE
            }

            is ChannelFragmentState.Content<*> -> {
                (it.value as? List<Channel>)?.let {
                    if (it.isEmpty()) {
                        emptyChannelsLayout?.visibility = View.VISIBLE
                        devicesRecyclerView.visibility = View.INVISIBLE
                    } else {
                        channelAdapter.addItems(it.map {
                            ChannelItemVM(it.id, it.name, it.createTime, it.isDeleted)
                        })
                        emptyChannelsLayout?.visibility = View.INVISIBLE
                        devicesRecyclerView.visibility = View.VISIBLE
                    }

                    errorLayout?.visibility = View.INVISIBLE
                    loadingContent?.visibility = View.INVISIBLE
                }
            }

            is ChannelFragmentState.Failure -> {
                devicesRecyclerView.visibility = View.INVISIBLE
                errorLayout?.visibility = View.VISIBLE
                loadingContent?.visibility = View.INVISIBLE
            }
        }
    }


    override fun initToolbar(presenter: ToolbarPresenter) {
        presenter.setTitle("Мои устройства")
        presenter.setBackToHome(false)
        presenter.show()
    }

    override fun showBottomNavigation(): Boolean = true

    @ExperimentalCoroutinesApi
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        devicesRecyclerView = view.findViewById(R.id.devices_recycler)
        attachDeviceButton = view.findViewById(R.id.attach_device)

        emptyChannelsLayout = view.findViewById(R.id.empty_content_template)
        errorLayout = view.findViewById(R.id.error_view)

        emptyChannelsLayout?.visibility = View.INVISIBLE
        emptyChannelsLayout?.setOnClickSubmitButtonListener {
            toCreateNewDevice()
        }

        errorLayout?.visibility = View.INVISIBLE

        loadingContent = view.findViewById(R.id.loading_container)

        attachDeviceButton.setOnClickListener(this)
        devicesRecyclerView.apply {
            adapter = channelAdapter
            layoutManager = LinearLayoutManager(requireContext())
        }

        channelViewModel.channelFragmentState.observe(requireActivity(), channelStateObserver)
        channelViewModel.loadState()
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        manageQrResult()
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
            R.id.attach_device -> toCreateNewDevice()
        }
    }

    private fun toCreateNewDevice() {
        NavHostFragment.findNavController(this)
                .navigate(R.id.attachDeviceFragment)
    }


}