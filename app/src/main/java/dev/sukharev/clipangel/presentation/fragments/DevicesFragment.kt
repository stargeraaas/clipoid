package dev.sukharev.clipangel.presentation.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.floatingactionbutton.FloatingActionButton
import dev.sukharev.clipangel.R


class DevicesFragment: Fragment() {


    private lateinit var devicesRecyclerView: RecyclerView
    private lateinit var attachDeviceButton: FloatingActionButton

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_devices, null)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        devicesRecyclerView = view.findViewById(R.id.devices_recycler)
        attachDeviceButton = view.findViewById(R.id.attach_device)
    }


}