package com.arjun.cubewealth.activities

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.arjun.cubewealth.R
import com.arjun.cubewealth.viewModel.MainViewModel

class HomeActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_home)

        val myViewModel = MainViewModel()
    }
}