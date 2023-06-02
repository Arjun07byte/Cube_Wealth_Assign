package com.arjun.cubewealth.activities

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import androidx.room.Database
import com.arjun.cubewealth.R
import com.arjun.cubewealth.localDatabase.DatabaseBookmarkMovies
import com.arjun.cubewealth.repository.MainRepository
import com.arjun.cubewealth.viewModel.MainViewModel

class HomeActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_home)

        // Initializing the MainViewModel using the Repository and
        // Movie ROOM Database as its parameters
        val myViewModel = MainViewModel(MainRepository(DatabaseBookmarkMovies(this)))
    }
}