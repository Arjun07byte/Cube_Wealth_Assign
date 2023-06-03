package com.arjun.cubewealth.activities

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.setupWithNavController
import com.arjun.cubewealth.R
import com.arjun.cubewealth.localDatabase.DatabaseBookmarkMovies
import com.arjun.cubewealth.repository.MainRepository
import com.arjun.cubewealth.viewModel.MainViewModel
import com.google.android.material.bottomnavigation.BottomNavigationView

class HomeActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_home)

        // Initializing the MainViewModel using the Repository and
        // Movie ROOM Database as its parameters
        val myViewModel = MainViewModel(MainRepository(DatabaseBookmarkMovies(this)))

        // Establishing the Bottom Navigation View using the Android
        // Navigation Component Library
        val activityNavHost =
            supportFragmentManager.findFragmentById(R.id.fragmentContainer_homeActivity) as NavHostFragment
        val containerNavController = activityNavHost.navController
        val bottomNavView: BottomNavigationView = findViewById(R.id.bottomNav_homeActivity)

        bottomNavView.setupWithNavController(containerNavController)
    }
}