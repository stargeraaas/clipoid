package dev.sukharev.clipangel


import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle

import android.widget.Button
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.setupWithNavController
import com.google.android.material.bottomnavigation.BottomNavigationView

class MainActivity : AppCompatActivity() {



    companion object {
        private const val REQUEST_CAMERA_PERMISSION = 10
    }


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        val navHostFragment =
                supportFragmentManager.findFragmentById(R.id.nav_host_fragment) as NavHostFragment
        val navController = navHostFragment.navController
        val bottomMenu: BottomNavigationView = findViewById(R.id.bottomNavigationView)
        bottomMenu.setupWithNavController(navController)
        bottomMenu.setOnNavigationItemSelectedListener {
            when(it.itemId) {
                R.id.action_to_clips -> {
                    navController.navigate(R.id.clipsFragment)
                }
                R.id.action_to_devices -> {
                    navController.navigate(R.id.devicesFragment)
                }
            }

            true
        }

    }

}