package dev.sukharev.clipangel


import android.content.IntentFilter
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View

import android.widget.Button
import android.widget.Toolbar
import androidx.navigation.NavController
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.setupWithNavController
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.android.material.navigation.NavigationView
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import com.google.firebase.messaging.FirebaseMessaging
import dev.sukharev.clipangel.core.App
import dev.sukharev.clipangel.presentation.BottomNavView
import dev.sukharev.clipangel.presentation.ToolbarPresenter
import dev.sukharev.clipangel.services.ClipboardCopyBroadcast
import dev.sukharev.clipangel.services.ClipboardCopyBroadcast.Companion.ACTION_UPDATE_NOTIFICATION

class MainActivity : AppCompatActivity(), ToolbarPresenter, BottomNavView {


    private lateinit var bottomMenu: BottomNavigationView

    companion object {
        private const val REQUEST_CAMERA_PERMISSION = 10
    }

    lateinit var navController: NavController

    override fun onBackPressed() {
        super.onBackPressed()
    }

    private val copyBroadcast = ClipboardCopyBroadcast()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        App.currentActivity = this
        setSupportActionBar(findViewById(R.id.materialToolbar))
        registerReceiver(copyBroadcast, IntentFilter(ACTION_UPDATE_NOTIFICATION))
        val navHostFragment =
                supportFragmentManager.findFragmentById(R.id.nav_host_fragment) as NavHostFragment
        navController = navHostFragment.navController
        bottomMenu = findViewById(R.id.bottomNavigationView)
        bottomMenu.setupWithNavController(navController)
//        bottomMenu.getOrCreateBadge(R.id.action_to_clips).apply {
////            number = 1
//        }
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


}