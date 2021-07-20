package com.example.muneereshop.ui.activities

import android.Manifest
import android.app.Activity
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.TextUtils
import android.util.Log
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
import com.example.muneereshop.utils.GlideLoader
import java.io.IOException

class ProfileActivity : AppCompatActivity(), View.OnClickListener {
    // Progress Dialogue
    var loading = DialogueProgress(this)

    private lateinit var binding: ActivityProfileBinding

    // Instance of User data model class. We will initialize it later on.
    private lateinit var myUserDetails: User

    // Add a global variable for URI of a selected image from phone storage.
    private var mySelectedImageFileUri: Uri? = null

    private var myUserProfileImageURL: String = ""


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityProfileBinding.inflate(layoutInflater)
        setContentView(binding.root)
        myUserDetails = User()
        if (intent.hasExtra(Constants.EXTRA_USER_DETAILS)) {
            // Get the user details from intent as a ParcelableExtra.
            myUserDetails = intent.getParcelableExtra(Constants.EXTRA_USER_DETAILS)!!
        }

        // Here, the some of the edittext components are disabled because it is added at a time of Registration.
        val profileFirstName = binding.profileFirstName
        val profileLastName = binding.profileLastName
        val profileEmail = binding.profileEmail
        val saveButton = binding.buttonSubmit
        val profile_photo = binding.ivUserPhoto
        val profile_mobile = binding.profileMobileNumber
       val gender_male = binding.selectMale
        val gender_female = binding.selectFemale


        if (myUserDetails.profileCompleted == 0){

            actionBar?.title = "Complete Profile"
            profileFirstName.isEnabled = false
            profileFirstName.setText(myUserDetails.firstName)
            profileLastName.isEnabled = false
            profileLastName.setText(myUserDetails.lastName)
            profileEmail.isEnabled = false
            profileEmail.setText(myUserDetails.email)
        }else {
            actionBar?.title = "Edit Profile"
            GlideLoader(this@ProfileActivity).loadUserPicture(myUserDetails.image,profile_photo)
            profileFirstName.setText(myUserDetails.firstName)
            profileLastName.setText(myUserDetails.lastName)
            profileEmail.isEnabled = false
            profileEmail.setText(myUserDetails.email)
            if (myUserDetails.mobile != 0L){
                profile_mobile.setText(myUserDetails.mobile.toString())
            }
            if (myUserDetails.gender == Constants.MALE){
                gender_male.isChecked = true

            }
            else {
                gender_female.isChecked = true
            }

        }

        val iv_user_photo = binding.ivUserPhoto

        iv_user_photo.setOnClickListener(this@ProfileActivity)
        saveButton.setOnClickListener(this@ProfileActivity)
    }


    override fun onClick(v: View?) {
        if (v != null) {
            when (v.id) {
                R.id.iv_user_photo -> {

                    if (ContextCompat.checkSelfPermission(
                            this,
                            Manifest.permission.READ_EXTERNAL_STORAGE
                        )
                        == PackageManager.PERMISSION_GRANTED
                    ) {

                        Constants.showImageChooser(this)
                    } else {

                        ActivityCompat.requestPermissions(
                            this@ProfileActivity,
                            arrayOf(Manifest.permission.READ_EXTERNAL_STORAGE),
                            Constants.READ_STORAGE_PERMISSION_CODE
                        )
                    }
                }
                R.id.button_Submit -> {

                    if (validateProfiledetails()) {
                        loading.startLoading()
                        if (mySelectedImageFileUri != null){
                            FireStores().uploadImageToCloudStorage(this,mySelectedImageFileUri,Constants.USER_PROFILE_IMAGE)
                        }else {
                            updateUserProfileDetails()
                        }


                    }
                }
            }

        }
    }

private fun updateUserProfileDetails(){
    val First_Name = binding.profileFirstName
    val Last_Name = binding.profileLastName
    val Mobile_Number = binding.profileMobileNumber
    val Male = binding.selectMale
    val userHashMap = HashMap<String, Any>()

    // Get the FirstName from editText and trim the space
    val firstName = First_Name.text.toString().trim { it <= ' ' }
    if (firstName != myUserDetails.firstName) {
        userHashMap[Constants.FIRST_NAME] = firstName
    }

    // Get the LastName from editText and trim the space
    val lastName = Last_Name.text.toString().trim { it <= ' ' }
    if (lastName != myUserDetails.lastName) {
        userHashMap[Constants.LAST_NAME] = lastName
    }

    // Here we get the text from editText and trim the space
    val mobileNumber = Mobile_Number.text.toString().trim { it <= ' ' }
    val gender = if (Male.isChecked) {
        Constants.MALE
    } else {
        Constants.FEMALE
    }

    if (myUserProfileImageURL.isNotEmpty()) {
        userHashMap[Constants.IMAGE] = myUserProfileImageURL
    }

    if (mobileNumber.isNotEmpty() && mobileNumber != myUserDetails.mobile.toString()) {
        userHashMap[Constants.MOBILE] = mobileNumber.toLong()
    }

    if (gender.isNotEmpty() && gender != myUserDetails.gender) {
        userHashMap[Constants.GENDER] = gender
    }

    // Here if user is about to complete the profile then update the field or else no need.
    // 0: User profile is incomplete.
    // 1: User profile is completed.
    if (myUserDetails.profileCompleted == 0) {
        userHashMap[Constants.COMPLETE_PROFILE] = 1
    }

    // call the registerUser function of FireStore class to make an entry in the database.
    FireStores().updateUserProfileData(
        this@ProfileActivity,
        userHashMap
    )
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
                Constants.showImageChooser(this)

                // Toast.makeText(this, "The storage permission granted.", Toast.LENGTH_SHORT).show()
            } else {
                Toast.makeText(this, "The storage permission Denied.", Toast.LENGTH_SHORT).show()
            }
        }
    }
   override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        val ivUserPhoto = binding.ivUserPhoto
        if (resultCode == Activity.RESULT_OK) {
            if (requestCode == Constants.PICK_IMAGE_REQUEST_CODE){
            if (data != null) {
                try {
                    mySelectedImageFileUri = data.data!!
                    GlideLoader(this@ProfileActivity).loadUserPicture(mySelectedImageFileUri!!, ivUserPhoto)
                    //  binding.ivUserPhoto.setImageURI(Uri.parse(selectedImageUri.toString()))
                } catch (e: IOException) {
                    e.printStackTrace()
                    Toast.makeText(
                        this@ProfileActivity,
                        "image selection failed",
                        Toast.LENGTH_LONG
                    ).show()
                }
            }
        }
        } else if (resultCode == Activity.RESULT_CANCELED) {


            Log.e("Result cancelled", "Image selection Cancelled")
        }
    }

    fun userProfileUpdateSuccess(){
        loading.isDismiss()
        Toast.makeText(this, "Your profile update is success.", Toast.LENGTH_LONG).show()
        // Redirect to the Main Screen after profile completion.
        startActivity(Intent(this@ProfileActivity, DashboardActivity::class.java))
        finish()
    }
    fun validateProfiledetails(): Boolean {
        val mobileNumber = binding.profileMobileNumber.toString().trim { it <= ' ' }
        return when {
            TextUtils.isEmpty(mobileNumber) -> {
                Toast.makeText(this@ProfileActivity, "Please Enter Mobile Number", Toast.LENGTH_SHORT)
                    .show()
                false
            }
            else -> {
                true
            }

        }
    }
    fun imageUploadSuccess(imageURL: String) {
       // loading.isDismiss()


          myUserProfileImageURL = imageURL
        updateUserProfileDetails()

    }


}


