package dev.sukharev.clipangel.presentation.fragments

import android.os.Bundle
import android.view.*
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
import dev.sukharev.clipangel.presentation.fragments.dialogs.CustomAlertDialog
import dev.sukharev.clipangel.presentation.fragments.dialogs.DetailChannelBottomDialog
import dev.sukharev.clipangel.presentation.recycler.ChannelItemVM
import dev.sukharev.clipangel.presentation.recycler.ChannelRecyclerAdapter
import dev.sukharev.clipangel.presentation.view.info.InformationView
import dev.sukharev.clipangel.presentation.viewmodels.channellist.ChannelListViewModel
import dev.sukharev.clipangel.utils.toDateFormat1
import kotlinx.coroutines.*
import kotlinx.coroutines.flow.collect
import org.koin.android.viewmodel.ext.android.viewModel


class ChannelsFragment : BaseFragment(), View.OnClickListener {

    private var devicesRecyclerView: RecyclerView? = null
    private lateinit var attachDeviceButton: FloatingActionButton
    private var loadingContent: ConstraintLayout? = null

    private var emptyChannelsLayout: InformationView? = null
    private var errorLayout: InformationView? = null

    private val channelAdapter = ChannelRecyclerAdapter()

    private val channelViewModel: ChannelListViewModel by viewModel()

    private val deleteChannelDialog = CustomAlertDialog()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setHasOptionsMenu(true)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_devices, null)
    }

    private val channelStateObserver = Observer<ViewFragmentState> {
        when (it) {
            is ViewFragmentState.Loading -> {
                loadingContent?.visibility = View.VISIBLE
                devicesRecyclerView?.visibility = View.INVISIBLE
                errorLayout?.visibility = View.INVISIBLE
                emptyChannelsLayout?.visibility = View.INVISIBLE
            }

            is ViewFragmentState.Content<*> -> {
                if (deleteChannelDialog.isAdded)
                    deleteChannelDialog.dismiss()

                (it.value as? List<Channel>)?.let {
                    if (it.isEmpty()) {
                        emptyChannelsLayout?.visibility = View.VISIBLE
                        devicesRecyclerView?.visibility = View.INVISIBLE
                    } else {
                        channelAdapter.addItems(it.map {
                            ChannelItemVM(it.id, it.name, it.createTime.toDateFormat1(), it.isDeleted)
                        })
                        emptyChannelsLayout?.visibility = View.INVISIBLE
                        devicesRecyclerView?.visibility = View.VISIBLE
                    }

                    errorLayout?.visibility = View.INVISIBLE
                    loadingContent?.visibility = View.INVISIBLE
                }
            }

            is ViewFragmentState.Failure -> {
                devicesRecyclerView?.visibility = View.INVISIBLE
                errorLayout?.visibility = View.VISIBLE
                loadingContent?.visibility = View.INVISIBLE

                errorLayout?.apply {
                    viewState.titleText = it.title
                    viewState.subtitleText = it.subtitle
                    changeState(viewState)
                }
            }
        }
    }


    override fun initToolbar(presenter: ToolbarPresenter) {
        presenter.setTitle(getString(R.string.my_channels))
        presenter.setBackToHome(false)
        presenter.show()
    }

    override fun showBottomNavigation(): Boolean = true

    val channelListener = object : ChannelRecyclerAdapter.OnItemClickListener {
        override fun onItemClicked(channelItemVM: ChannelItemVM) {
            GlobalScope.launch {
                channelViewModel.getChannelById(channelItemVM.id).collect {
                    requireActivity().runOnUiThread {
                        val detainChannelBottomDialog = DetailChannelBottomDialog(it)
                        detainChannelBottomDialog.setOnClickListener(object : DetailChannelBottomDialog.OnClickListener {
                            override fun onClick(id: String) {
                                showDeletingAlertDialog(id)
                            }
                        })
                        detainChannelBottomDialog.show(childFragmentManager, "DetailChannelBottomDialog")
                    }
                }
            }
        }
    }

    private fun showDeletingAlertDialog(channelId: String) {


        deleteChannelDialog.show(childFragmentManager, "channel_deleting") {
            deleteChannelDialog.apply {
                negativeButton?.text = "Отменить"
                negativeButton?.setOnClickListener {
                    dismiss()
                }
                positiveButton?.text = "Да, удалить"
                positiveButton?.setTextColor(requireContext().getColor(android.R.color.holo_red_dark))
                positiveButton?.setOnClickListener {
                    channelViewModel.action(ChannelListViewModel.Action.DeattachChannel(channelId))
                }

                titleTextView?.text = "Удаление канала"
                bodyTextView?.text = "При удалении данного канала также будут удалены все связанные с ним клипы"
            }
        }

    }

    private val deletingChannelObserver = Observer<Boolean> {
        deleteChannelDialog?.dismiss()
    }

    @ExperimentalCoroutinesApi
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        devicesRecyclerView = view.findViewById(R.id.devices_recycler)
        attachDeviceButton = view.findViewById(R.id.attach_device)

        emptyChannelsLayout = view.findViewById(R.id.empty_content_template)
        errorLayout = view.findViewById(R.id.error_view)

        emptyChannelsLayout?.visibility = View.INVISIBLE
        emptyChannelsLayout?.setOnClickSubmitButtonListener {
            toAttachNewDevice()
        }

        errorLayout?.visibility = View.INVISIBLE

        loadingContent = view.findViewById(R.id.loading_container)

        attachDeviceButton.setOnClickListener(this)
        devicesRecyclerView?.apply {
            layoutManager = LinearLayoutManager(view.context)
            adapter = channelAdapter
        }

        channelViewModel.loadState()
    }

    override fun onStart() {
        super.onStart()
        channelAdapter.addOnItemClickListener(channelListener)
        channelViewModel.viewFragmentState.observe(requireActivity(), channelStateObserver)
    }

    override fun onStop() {
        super.onStop()
        channelAdapter.removeOnItemClickListener(channelListener)
        channelViewModel.viewFragmentState.removeObserver(channelStateObserver)
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.channel_list, menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.action_attach -> toAttachNewDevice()
        }
        return true
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        manageQrResult()
    }

    private fun manageQrResult() {
        findNavController().currentBackStackEntry?.savedStateHandle?.apply {
            when (get<Int>(SCAN_RESULT)) {
                RESULT_OK -> doOnSuccessQrResult(this)
                RESULT_ERROR -> doOnFailureQrResult(this)
            }
            this.remove<Int>(SCAN_RESULT)
        }
    }

    private fun doOnSuccessQrResult(state: SavedStateHandle) {
        channelViewModel.action(ChannelListViewModel.Action.AttachChannel(
                ChannelCredentials(state.get<String>(CHANNEL_ID_EXTRA)!!,
                        state.get<String>(CHANNEL_SECRET_EXTRA)!!)))
    }

    private fun doOnFailureQrResult(state: SavedStateHandle) {
        println(state.get<String>(RESULT_ERROR_MESSAGE))
    }

    override fun onClick(view: View?) {
        when (view?.id) {
            R.id.attach_device -> toAttachNewDevice()
        }
    }

    private fun toAttachNewDevice() {
        NavHostFragment.findNavController(this)
                .navigate(R.id.attachDeviceFragment)
    }


}