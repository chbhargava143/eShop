package com.example.muneereshop.ui.activities

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import com.example.muneereshop.R

class SplashActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splash)

    }
    override fun onResume() {
        super.onResume()
        Handler(Looper.myLooper()!!).postDelayed(Runnable {
            val intent = Intent(this@SplashActivity, DashboardActivity::class.java)
            startActivity(intent)
            finish()
        },2000)
    }

}