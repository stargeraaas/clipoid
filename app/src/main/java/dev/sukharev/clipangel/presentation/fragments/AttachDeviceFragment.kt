package dev.sukharev.clipangel.presentation.fragments

import android.graphics.PointF
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import com.dlazaro66.qrcodereaderview.QRCodeReaderView
import dev.sukharev.clipangel.R


class AttachDeviceFragment: Fragment(), QRCodeReaderView.OnQRCodeReadListener  {

    lateinit var qrCodeReaderView: QRCodeReaderView

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_attach_device, container)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initQRCodeReader(view)
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