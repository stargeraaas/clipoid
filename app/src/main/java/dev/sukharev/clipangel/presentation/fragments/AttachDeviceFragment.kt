package dev.sukharev.clipangel.presentation.fragments

import android.graphics.PointF
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.fragment.findNavController
import com.dlazaro66.qrcodereaderview.QRCodeReaderView
import dev.sukharev.clipangel.R
import dev.sukharev.clipangel.presentation.ToolbarPresenter
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import kotlinx.serialization.decodeFromString
import kotlinx.serialization.json.Json
import java.lang.Exception


class AttachDeviceFragment: BaseFragment(), QRCodeReaderView.OnQRCodeReadListener  {

    lateinit var qrCodeReaderView: QRCodeReaderView

    companion object {
        val RESULT_OK = 1
        val RESULT_CANCELL = 0
        val RESULT_ERROR = -1
        val CHANNEL_ID_EXTRA = "CHANNEL_ID_EXTRA"
        val CHANNEL_SECRET_EXTRA = "CHANNEL_SECRET_EXTRA"
        val SCAN_RESULT = "SCAN_RESULT"
        val RESULT_ERROR_MESSAGE = "RESULT_ERROR_MESSAGE"
    }

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
        text?.let {
            try {
                sendDataAndClose(Json.decodeFromString(it))
            } catch (e: Exception) {
                println()
            }
        }

    }

    private fun sendDataAndClose(data: QrCodeParsedData) {
        stopScanning()
        findNavController().previousBackStackEntry?.savedStateHandle?.apply {
            set(SCAN_RESULT, RESULT_OK)
            set(CHANNEL_ID_EXTRA, data.channelId)
            set(CHANNEL_SECRET_EXTRA, data.secret)
        }
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

    @Serializable
    data class QrCodeParsedData(
            @SerialName("channel") val channelId: String,
            @SerialName("key") val secret: String
    )

}