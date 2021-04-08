package dev.sukharev.clipangel.presentation

import android.annotation.SuppressLint
import android.os.Bundle
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import dev.sukharev.clipangel.BuildConfig
import dev.sukharev.clipangel.R
import dev.sukharev.clipangel.data.local.repository.credentials.Credentials
import org.koin.android.ext.android.inject

class AboutActivity: AppCompatActivity() {

    private val credentials: Credentials by inject()

    @SuppressLint("SetTextI18n")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_about)
        findViewById<TextView>(R.id.build_version_text)?.apply {
            text = "${getString(R.string.app_version_text)} ${BuildConfig.VERSION_NAME}"
        }
        findViewById<TextView>(R.id.serial_number_text)?.apply {
            text = "${getString(R.string.app_device_identifier)}\n${credentials.getDeviceIdentifier()}"
        }
    }
}