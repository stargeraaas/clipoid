package dev.sukharev.clipangel.presentation.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.floatingactionbutton.FloatingActionButton
import dev.sukharev.clipangel.R
import dev.sukharev.clipangel.presentation.ToolbarPresenter


class DevicesFragment: BaseFragment(), View.OnClickListener {

    private lateinit var devicesRecyclerView: RecyclerView
    private lateinit var attachDeviceButton: FloatingActionButton

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

        findNavController().currentBackStackEntry?.savedStateHandle?.get<String>("key")
        println()
    }

    override fun onClick(view: View?) {
        when(view?.id) {
            R.id.attach_device -> NavHostFragment.findNavController(this)
                    .navigate(R.id.attachDeviceFragment)
        }
    }


}