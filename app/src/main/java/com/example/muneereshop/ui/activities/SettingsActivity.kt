package com.example.muneereshop.ui.activities

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import com.example.muneereshop.constants.Constants
import com.example.muneereshop.databinding.ActivitySettingsBinding
import com.example.muneereshop.firebase.firestore.FireStores
import com.example.muneereshop.progressbar.DialogueProgress
import com.example.muneereshop.user.User
import com.example.muneereshop.utils.GlideLoader
import com.google.firebase.auth.FirebaseAuth

class SettingsActivity : AppCompatActivity(){

    private lateinit var binding : ActivitySettingsBinding
    val loading = DialogueProgress(this@SettingsActivity)
    private lateinit var myUserDetails:User

    private lateinit var user_name : TextView
    private lateinit var user_email : TextView
    private lateinit var user_mobile_number : TextView
    private lateinit var user_gender : TextView
    private lateinit var user_edit : TextView
    private lateinit var user_address : TextView
    private lateinit var user_log_out : Button
    private lateinit var user_profile_image : ImageView


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivitySettingsBinding.inflate(layoutInflater)
         user_name = binding.settingsUserName
         user_email = binding.settingsUserEmail
         user_mobile_number = binding.settingsUserMobileNumber
         user_gender = binding.settingsUserGender
         user_edit = binding.settingsEdit
        user_log_out = binding.settingsLogout
        user_address = binding.settingsAddress
         user_profile_image = binding.ivSettingsPhoto
        setContentView(binding.root)
        user_edit.setOnClickListener {
            val intent = Intent(this@SettingsActivity,ProfileActivity::class.java)
            intent.putExtra(Constants.EXTRA_USER_DETAILS,myUserDetails)
            startActivity(intent)
        }
        user_log_out.setOnClickListener {
            FirebaseAuth.getInstance().signOut()
            val intent = Intent(this@SettingsActivity,LoginActivity::class.java)
            intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
            startActivity(intent)
            finish()
        }
        user_address.setOnClickListener {

        }
    }
    fun getUserDetailsFromFirebase(){
        loading.startLoading()
        FireStores().getUserDetails(this)


    }

    fun userDetailsSuccess(user:User){
        myUserDetails = user
        loading.isDismiss()

        GlideLoader(this@SettingsActivity).loadUserPicture(user.image,user_profile_image)
        user_name.text = ": ${user.firstName} ${ user.lastName }"
        user_email.text = ": ${user.email}"
        user_gender.text = ": ${user.gender}"
        user_mobile_number.text = ": ${user.mobile}"
    }

    override fun onResume() {
        super.onResume()
        getUserDetailsFromFirebase()
    }


}