package com.example.muneereshop.activities

import android.Manifest
import android.content.pm.PackageManager
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.TextUtils
import android.view.View
import android.widget.Toast
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import com.example.muneereshop.R
import com.example.muneereshop.constants.Constants
import com.example.muneereshop.databinding.ActivityProfileBinding
import com.example.muneereshop.firebase.firestore.FireStores
import com.example.muneereshop.progressbar.DialogueProgress
import com.example.muneereshop.user.User

class ProfileActivity : AppCompatActivity(), View.OnClickListener {
    var loading = DialogueProgress(this)
    private lateinit var binding: ActivityProfileBinding
    private lateinit var myUserDetails: User

    // Add a global variable for URI of a selected image from phone storage.
    private var mySelectedImageFileUri: Uri? = null

    private var myUserProfileImageURL: String = ""


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityProfileBinding.inflate(layoutInflater)
        setContentView(binding.root)
        if (intent.hasExtra(Constants.EXTRA_USER_DETAILS)) {
            // Get the user details from intent as a ParcelableExtra.
            myUserDetails = intent.getParcelableExtra(Constants.EXTRA_USER_DETAILS)!!
        }
        val profileFirstName = binding.profileFirstName
        val profileLastName = binding.profileLastName
        val profileEmail = binding.profileEmail

        profileFirstName.isEnabled = false
        profileFirstName.setText(myUserDetails.firstName)

        profileLastName.isEnabled = false
        profileLastName.setText(myUserDetails.lastName)

        profileEmail.isEnabled = false
        profileEmail.setText(myUserDetails.email)
        val iv_user_photo = binding.ivUserPhoto

        iv_user_photo.setOnClickListener(this@ProfileActivity)
    }

    fun imageUploadSuccess(imageURL: String) {

        myUserProfileImageURL = imageURL

        /// updateUserProfileDetails()
    }

    override fun onClick(v: View?) {
        if (v != null) {
            when (v.id) {
                R.id.iv_user_photo -> {
                    // Here we will check if the permission is already allowed or we need to request for it.
                    // First of all we will check the READ_EXTERNAL_STORAGE permission and if it is not allowed we will request for the same.
                    if (ContextCompat.checkSelfPermission(
                            this,
                            Manifest.permission.READ_EXTERNAL_STORAGE
                        )
                        == PackageManager.PERMISSION_GRANTED
                    ) {
                        Toast.makeText(
                            this@ProfileActivity,
                            "You already have the storage permissions",
                            Toast.LENGTH_LONG
                        ).show()
                    } else {

                        /*Requests permissions to be granted to this application. These permissions
                         must be requested in your manifest, they should not be granted to your app,
                         and they should have protection level*/

                        ActivityCompat.requestPermissions(
                            this,
                            arrayOf(Manifest.permission.READ_EXTERNAL_STORAGE),
                            Constants.READ_STORAGE_PERMISSION_CODE
                        )
                    }
                }

            }

        }
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)

        if (requestCode == Constants.READ_STORAGE_PERMISSION_CODE) {
            // if permission granted
            if (grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                Toast.makeText(this, "The storage permission granted.", Toast.LENGTH_SHORT).show()
            } else {
                Toast.makeText(this, "The storage permission Denied.", Toast.LENGTH_SHORT).show()
            }
        }
    }


}


