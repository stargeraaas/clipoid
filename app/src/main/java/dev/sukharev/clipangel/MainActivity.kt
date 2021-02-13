package dev.sukharev.clipangel


import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Size
import android.view.TextureView

import android.widget.Button
import androidx.camera.core.*

class MainActivity : AppCompatActivity() {


    companion object {
        private const val REQUEST_CAMERA_PERMISSION = 10
    }

    private lateinit var textureView: TextureView

    private lateinit var buttonPlus: Button

    val previewConfig = PreviewConfig.Builder()
            // We want to show input from back camera of the device
            .setLensFacing(CameraX.LensFacing.BACK)
            .setTargetResolution(Size(2000, 1500))
            .build()

    val preview = Preview(previewConfig)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
    }
}