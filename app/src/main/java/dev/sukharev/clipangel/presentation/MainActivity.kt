package dev.sukharev.clipangel.presentation


import android.content.Intent
import android.content.IntentFilter
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatDelegate
import androidx.appcompat.app.AppCompatDelegate.MODE_NIGHT_NO
import androidx.biometric.BiometricPrompt
import androidx.drawerlayout.widget.DrawerLayout
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.NavController
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.setupWithNavController
import com.google.android.material.appbar.MaterialToolbar
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import dev.sukharev.clipangel.R
import dev.sukharev.clipangel.presentation.fragments.cliplist.ClipListFragmentArgs
import dev.sukharev.clipangel.presentation.utils.BiometricPromptCreator
import dev.sukharev.clipangel.presentation.viewmodels.channellist.MainViewModel
import dev.sukharev.clipangel.services.ClipboardCopyBroadcast
import dev.sukharev.clipangel.services.ClipboardCopyBroadcast.Companion.ACTION_UPDATE_NOTIFICATION

class MainActivity : AppCompatActivity(), ToolbarPresenter, BottomNavView,
        NavDrawerPresenter {

    private lateinit var biometricPrompt: BiometricPrompt
    private lateinit var bottomMenu: BottomNavigationView

    companion object {
        const val CLIP_ID_EXTRA = "CLIP_ID"
        const val OPEN_DETAILED_CLIP_ACTION = "com.clipoid.OPEN_DETAILED_CLIP"
        const val OPEN_DETAILED_CLIP_REQUEST_CODE = 45
    }

    private lateinit var navController: NavController
    private lateinit var navHostFragment: NavHostFragment
    private lateinit var drawerLayout: DrawerLayout
    private lateinit var materialToolbar: MaterialToolbar

    private val copyBroadcast = ClipboardCopyBroadcast()

    private lateinit var mainViewModel: MainViewModel

    private val accessRequestObserver = Observer<MainViewModel.PermitAccess> { accessType ->
        accessType?.let {
            callBiometry(accessType)
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        AppCompatDelegate.setDefaultNightMode(MODE_NIGHT_NO)
        setContentView(R.layout.activity_main)
        mainViewModel = ViewModelProvider(this)[MainViewModel::class.java]

        mainViewModel.accessRequest.observe(this, accessRequestObserver)

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

        biometricPrompt = BiometricPromptCreator.create(this, mainViewModel)

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

    private fun callBiometry(accessType: MainViewModel.PermitAccess) {
        biometricPrompt.authenticate(
                BiometricPromptCreator.createPromptInfo(this, accessType)
        )
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
            val args = ClipListFragmentArgs(intent.getStringExtra(CLIP_ID_EXTRA))
            mainViewModel.forceDetail.value = args.detailedClipId
            if (navController.currentDestination?.id == R.id.action_to_devices) {
                navController.navigate(R.id.action_action_to_devices_to_action_to_clips, args.toBundle())
            }
        }
    }

}