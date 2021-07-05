package com.example.muneereshop.activities

import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.example.muneereshop.R
import com.example.muneereshop.constants.Constants
import com.example.muneereshop.databinding.ActivityHomeBinding
import com.google.firebase.auth.FirebaseAuth

class HomeActivity : AppCompatActivity() {
    private lateinit var binding: ActivityHomeBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityHomeBinding.inflate(layoutInflater)
        setContentView(binding.root)
       signOut()
        val sharedPreferences = getSharedPreferences(Constants.MUNEERESHOP_PREFERENCES,Context.MODE_PRIVATE)
        val username = sharedPreferences.getString(Constants.LOGGED_IN_USERNAME,"")!!
        binding.tvUserName.text = "The logged in user is $username"



    }
    private fun signOut(){
        binding.clickLogout.setOnClickListener {
            FirebaseAuth.getInstance().signOut()
            startActivity(Intent(this@HomeActivity,LoginActivity::class.java))
            finish()
        }
    }
}