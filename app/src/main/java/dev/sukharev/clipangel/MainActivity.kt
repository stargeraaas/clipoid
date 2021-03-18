package dev.sukharev.clipangel


import android.content.IntentFilter
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.core.content.ContextCompat
import androidx.biometric.BiometricPrompt

import androidx.drawerlayout.widget.DrawerLayout
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
import dev.sukharev.clipangel.services.ClipboardCopyBroadcast
import dev.sukharev.clipangel.services.ClipboardCopyBroadcast.Companion.ACTION_UPDATE_NOTIFICATION
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
    lateinit var drawerLayout: DrawerLayout
    lateinit var materialToolbar: MaterialToolbar

    private val copyBroadcast = ClipboardCopyBroadcast()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        App.currentActivity = this
        materialToolbar = findViewById(R.id.materialToolbar)
        setSupportActionBar(materialToolbar)
        registerReceiver(copyBroadcast, IntentFilter(ACTION_UPDATE_NOTIFICATION))
        val navHostFragment =
                supportFragmentManager.findFragmentById(R.id.nav_host_fragment) as NavHostFragment
        navController = navHostFragment.navController
        bottomMenu = findViewById(R.id.bottomNavigationView)
        drawerLayout = findViewById(R.id.drawer_layout)
        bottomMenu.setupWithNavController(navController)
//        bottomMenu.getOrCreateBadge(R.id.action_to_clips).apply {
////            number = 1
//        }
        Firebase.auth.signInAnonymously().addOnSuccessListener {
            println()
        }.addOnFailureListener {
            println()
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
                        Toast.makeText(applicationContext,
                                "Authentication succeeded!", Toast.LENGTH_SHORT)
                                .show()
                    }

                    override fun onAuthenticationFailed() {
                        super.onAuthenticationFailed()
                        Toast.makeText(applicationContext, "Authentication failed",
                                Toast.LENGTH_SHORT)
                                .show()
                    }
                })

        promptInfo = BiometricPrompt.PromptInfo.Builder()
                .setTitle("Разблокировка клипа")
                .setNegativeButtonText("Отменить")
                .build()

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

    override fun open() {
        drawerLayout.open()
    }


}