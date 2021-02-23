package dev.sukharev.clipangel


import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View

import android.widget.Button
import android.widget.Toolbar
import androidx.navigation.NavController
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.setupWithNavController
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import com.google.firebase.messaging.FirebaseMessaging
import dev.sukharev.clipangel.presentation.BottomNavView
import dev.sukharev.clipangel.presentation.ToolbarPresenter

class MainActivity : AppCompatActivity(), ToolbarPresenter, BottomNavView {


    private lateinit var bottomMenu: BottomNavigationView

    companion object {
        private const val REQUEST_CAMERA_PERMISSION = 10
    }

    lateinit var navController: NavController

    override fun onBackPressed() {
        super.onBackPressed()
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        setSupportActionBar(findViewById(R.id.materialToolbar))

        val navHostFragment =
                supportFragmentManager.findFragmentById(R.id.nav_host_fragment) as NavHostFragment
        navController = navHostFragment.navController
        bottomMenu = findViewById(R.id.bottomNavigationView)
        bottomMenu.setupWithNavController(navController)

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


}