package dev.sukharev.clipangel

import android.content.IntentFilter
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import androidx.drawerlayout.widget.DrawerLayout

import androidx.navigation.NavController
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.setupWithNavController
import com.google.android.material.appbar.MaterialToolbar
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.android.material.navigation.NavigationView
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import dev.sukharev.clipangel.core.App
import dev.sukharev.clipangel.presentation.BottomNavView
import dev.sukharev.clipangel.presentation.NavDrawerPresenter
import dev.sukharev.clipangel.presentation.ToolbarPresenter
import dev.sukharev.clipangel.services.ClipboardCopyBroadcast
import dev.sukharev.clipangel.services.ClipboardCopyBroadcast.Companion.ACTION_UPDATE_NOTIFICATION

class MainActivity : AppCompatActivity(), ToolbarPresenter, BottomNavView, NavDrawerPresenter {


    private lateinit var bottomMenu: BottomNavigationView
    private var navDrawer: NavigationView? = null
    private var drawerLayout: DrawerLayout? = null

    companion object {
        private const val REQUEST_CAMERA_PERMISSION = 10
    }

    lateinit var navController: NavController


    private val copyBroadcast = ClipboardCopyBroadcast()

    private var toolbar: MaterialToolbar?= null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        App.currentActivity = this
        toolbar = findViewById(R.id.materialToolbar)
        navDrawer = findViewById(R.id.navigationView)
        drawerLayout = findViewById(R.id.drawer_layout)
        drawerLayout?.setDrawerLockMode(DrawerLayout.LOCK_MODE_UNLOCKED)

        setSupportActionBar(toolbar)
        registerReceiver(copyBroadcast, IntentFilter(ACTION_UPDATE_NOTIFICATION))
        val navHostFragment =
                supportFragmentManager.findFragmentById(R.id.nav_host_fragment) as NavHostFragment
        navController = navHostFragment.navController
        bottomMenu = findViewById(R.id.bottomNavigationView)
        bottomMenu.setupWithNavController(navController)
        bottomMenu.getOrCreateBadge(R.id.action_to_clips).apply {
//            number = 1
        }
        Firebase.auth.signInAnonymously().addOnSuccessListener {
            println()
        }.addOnFailureListener {
            println()
        }

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

    override fun getToolbar(): MaterialToolbar? = toolbar

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
        navDrawer?.isEnabled = state
        drawerLayout?.isEnabled = state
    }

    override fun open() {
        drawerLayout?.open()
    }


}