package dev.sukharev.clipangel.presentation.fragments

import android.graphics.PointF
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.dlazaro66.qrcodereaderview.QRCodeReaderView
import dev.sukharev.clipangel.R
import dev.sukharev.clipangel.presentation.ToolbarPresenter


class AttachDeviceFragment: BaseFragment(), QRCodeReaderView.OnQRCodeReadListener  {

    lateinit var qrCodeReaderView: QRCodeReaderView


    override fun initToolbar(presenter: ToolbarPresenter) {
        presenter.hide()
    }

    override fun showBottomNavigation(): Boolean = false

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_attach_device, null)
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initQRCodeReader(view)
        getToolbarPresenter().setTitle("Регистрация устройства")
        getToolbarPresenter().setBackToHome(true)

    }

    private fun initQRCodeReader(view: View) {
        qrCodeReaderView = view.findViewById(R.id.qrdecoderview)
        qrCodeReaderView.setOnQRCodeReadListener(this)
        qrCodeReaderView.setQRDecodingEnabled(true)
        qrCodeReaderView.setBackCamera()
    }

    override fun onQRCodeRead(text: String?, points: Array<out PointF>?) {
        Toast.makeText(requireContext(), "$text", Toast.LENGTH_SHORT).show()
        stopScanning()
        findNavController().previousBackStackEntry?.savedStateHandle?.set("key", text)
        findNavController().popBackStack()
    }

    private fun startScanning() {
        qrCodeReaderView.startCamera()
    }

    private fun stopScanning() {
        qrCodeReaderView.stopCamera()
    }

    override fun onResume() {
        super.onResume()
        startScanning()
    }

    override fun onPause() {
        super.onPause()
        stopScanning()
    }

}