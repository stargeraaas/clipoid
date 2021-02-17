package dev.sukharev.clipangel.presentation.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase
import dev.sukharev.clipangel.R
import dev.sukharev.clipangel.domain.channel.models.Channel
import dev.sukharev.clipangel.domain.channel.ChannelInteractor
import dev.sukharev.clipangel.domain.channel.models.ChannelCredentials
import dev.sukharev.clipangel.presentation.ToolbarPresenter
import dev.sukharev.clipangel.presentation.recycler.ChannelItemVM
import dev.sukharev.clipangel.presentation.recycler.ChannelRecyclerAdapter
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import org.koin.android.ext.android.inject
import java.util.*


class ChannelsFragment : BaseFragment(), View.OnClickListener {

    private lateinit var devicesRecyclerView: RecyclerView
    private lateinit var attachDeviceButton: FloatingActionButton

    private val channelInteractor: ChannelInteractor by inject()

    private val channelAdapter = ChannelRecyclerAdapter()

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

        CoroutineScope(Dispatchers.IO).launch {
            val db = Firebase.database
            val ref = db.getReference("vAXOQXeRD64MZ!NNCrNIUw==")
            val recipient = ref.child("recipients")

            val data = db.getReference("vAXOQXeRD64MZ!NNCrNIUw==").child("data")
                    .get()
                    .addOnCompleteListener {
                        println()
                    }
                    .addOnFailureListener {
                        println()
                    }

            val key = ref.child("recipients").push().key

            val childUpdates = hashMapOf<String, Any>(
                    "${UUID.randomUUID().toString()}" to ""
            )

            recipient.updateChildren(childUpdates)

            channelInteractor.createChannel(ChannelCredentials(UUID.randomUUID().toString(), "ASD"))

        }

        devicesRecyclerView.apply {
            adapter = channelAdapter
            layoutManager = LinearLayoutManager(requireContext())
        }

        findNavController().currentBackStackEntry?.savedStateHandle?.get<String>("key")

        channelAdapter.addItems(listOf(ChannelItemVM("1", "A", "S", false)))
    }

    override fun onClick(view: View?) {
        when (view?.id) {
            R.id.attach_device -> NavHostFragment.findNavController(this)
                    .navigate(R.id.attachDeviceFragment)
        }
    }


}