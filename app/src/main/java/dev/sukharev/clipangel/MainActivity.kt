package dev.sukharev.clipangel


import android.app.NotificationManager
import android.content.Intent
import android.content.IntentFilter
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.core.content.ContextCompat
import androidx.biometric.BiometricPrompt

import androidx.drawerlayout.widget.DrawerLayout
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.NavController
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.setupWithNavController
import com.google.android.material.appbar.MaterialToolbar
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import dev.sukharev.clipangel.core.App
import dev.sukharev.clipangel.presentation.BottomNavView
import dev.sukharev.clipangel.presentation.NavDrawerPresenter
import dev.sukharev.clipangel.presentation.ToolbarPresenter
import dev.sukharev.clipangel.presentation.fragments.cliplist.ClipsFragment
import dev.sukharev.clipangel.presentation.fragments.cliplist.ClipsFragmentArgs
import dev.sukharev.clipangel.presentation.viewmodels.channellist.MainViewModel
import dev.sukharev.clipangel.services.ClipboardCopyBroadcast
import dev.sukharev.clipangel.services.ClipboardCopyBroadcast.Companion.ACTION_UPDATE_NOTIFICATION
import kotlinx.coroutines.NonCancellable.cancel
import java.util.concurrent.Executor

class MainActivity : AppCompatActivity(), ToolbarPresenter, BottomNavView,
        NavDrawerPresenter {

    private lateinit var executor: Executor
    private lateinit var biometricPrompt: BiometricPrompt
    private lateinit var promptInfo: BiometricPrompt.PromptInfo

    private lateinit var bottomMenu: BottomNavigationView

    companion object {
        private const val REQUEST_CAMERA_PERMISSION = 10
    }

    lateinit var navController: NavController
    lateinit var navHostFragment: NavHostFragment
    lateinit var drawerLayout: DrawerLayout
    lateinit var materialToolbar: MaterialToolbar

    private val copyBroadcast = ClipboardCopyBroadcast()

    private lateinit var mainViewModel: MainViewModel

    protected lateinit var notificationManager: NotificationManager


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        notificationManager = getSystemService(NOTIFICATION_SERVICE) as NotificationManager
        mainViewModel = ViewModelProvider(this)[MainViewModel::class.java]

        mainViewModel.openProtectedClipConfirmation.observe(this) {
            callBiometry()
        }

        App.currentActivity = this
        materialToolbar = findViewById(R.id.materialToolbar)
        setSupportActionBar(materialToolbar)
        registerReceiver(copyBroadcast, IntentFilter(ACTION_UPDATE_NOTIFICATION))
        navHostFragment =
                supportFragmentManager.findFragmentById(R.id.nav_host_fragment) as NavHostFragment
        navController = navHostFragment.navController
        bottomMenu = findViewById(R.id.bottomNavigationView)
        drawerLayout = findViewById(R.id.drawer_layout)
        bottomMenu.setupWithNavController(navController)

        Firebase.auth.signInAnonymously().addOnSuccessListener {
//            Toast.makeText(this, "FB auth success", Toast.LENGTH_SHORT).show()
        }.addOnFailureListener {
            Toast.makeText(this, it.message, Toast.LENGTH_SHORT).show()
        }

        executor = ContextCompat.getMainExecutor(this)
        biometricPrompt = BiometricPrompt(this, executor,
                object : BiometricPrompt.AuthenticationCallback() {
                    override fun onAuthenticationError(errorCode: Int,
                                                       errString: CharSequence) {
                        super.onAuthenticationError(errorCode, errString)

                    }

                    override fun onAuthenticationSucceeded(
                            result: BiometricPrompt.AuthenticationResult) {
                        super.onAuthenticationSucceeded(result)
                        mainViewModel.permitAccessForProtectedClip()
                    }

                    override fun onAuthenticationFailed() {
                        super.onAuthenticationFailed()
                    }
                })

        promptInfo = BiometricPrompt.PromptInfo.Builder()
                .setTitle(getString(R.string.unblock_clip))
                .setNegativeButtonText(getString(R.string.cancel))
                .build()

        checkIntentOnForceDetailedClip(intent)
    }

    override fun show() {
        supportActionBar?.show()
    }

    override fun hide() {
        supportActionBar?.hide()
    }

    override fun setBackToHome(state: Boolean) {
        supportActionBar?.setDisplayHomeAsUpEnabled(state)
        supportActionBar?.setDisplayShowHomeEnabled(state)
    }

    override fun setToolbar(toolbar: MaterialToolbar) {
        setSupportActionBar(toolbar)
    }

    fun callBiometry() {
        biometricPrompt.authenticate(promptInfo)
    }

    override fun getToolbar(): MaterialToolbar = materialToolbar

    override fun setTitle(text: String?) {
        supportActionBar?.title = text
    }

    override fun setVisibility(state: Boolean) {
        bottomMenu.visibility = if (state) View.VISIBLE else View.GONE
    }

    override fun onDestroy() {
        super.onDestroy()
        unregisterReceiver(copyBroadcast)
    }

    override fun enabled(state: Boolean) {

    }

    override fun onNewIntent(intent: Intent?) {
        super.onNewIntent(intent)
        checkIntentOnForceDetailedClip(intent)
    }

    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<out String>, grantResults: IntArray) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        navHostFragment.childFragmentManager.fragments.forEach {
            it.onRequestPermissionsResult(requestCode, permissions, grantResults)
        }
    }

    override fun open() {
        drawerLayout.open()
    }

    private fun checkIntentOnForceDetailedClip(intent: Intent?) {
        intent?.let {
            val args = ClipsFragmentArgs(intent.getStringExtra("CLIP_ID"))
            mainViewModel.forceDetail.value = args.detailedClipId
            if (navController.currentDestination?.id == R.id.action_to_devices) {
                navController.navigate(R.id.action_action_to_devices_to_action_to_clips, args.toBundle())
            }
        }
    }

}