package com.example.muneereshop.firebase.firestore

import android.app.Activity
import android.content.Context
import android.content.SharedPreferences
import android.net.Uri
import android.util.Log
import com.google.firebase.storage.FirebaseStorage
import com.example.muneereshop.ui.activities.LoginActivity
import com.example.muneereshop.ui.activities.ProfileActivity
import com.example.muneereshop.ui.activities.RegisterActivity
import com.example.muneereshop.constants.Constants
import com.example.muneereshop.user.User
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.SetOptions
import com.google.firebase.storage.StorageReference

class FireStores {
    private val myFirestore = FirebaseFirestore.getInstance()

    fun registerUser(activity: RegisterActivity, userInfo: User) {
        myFirestore.collection(Constants.USERS)
            .document(userInfo.id)
            .set(userInfo, SetOptions.merge())
            .addOnSuccessListener {

                activity.UserRegisteredOnSuccess()

            }.addOnFailureListener { e ->
                activity.loading.isDismiss()
                Log.e(activity.javaClass.simpleName, "Error while registering the User.", e)
            }
    }

    fun getCurrentUserId():String{
        val currentUser = FirebaseAuth.getInstance().currentUser
        var currentUserID = ""
        if (currentUser != null){
            currentUserID = currentUser.uid
        }
        return currentUserID
    }

    fun getUserDetails(activity:Activity){
        myFirestore.collection(Constants.USERS).document(getCurrentUserId())
            .get()
            .addOnSuccessListener { document ->
                Log.i(activity.javaClass.simpleName,document.toString())
                val user = document.toObject(User::class.java)
                val sharedPreferences = activity.getSharedPreferences(
                    Constants.MUNEERESHOP_PREFERENCES,
                    Context.MODE_PRIVATE
                )
                var editor : SharedPreferences.Editor = sharedPreferences.edit()
                editor.putString(Constants.LOGGED_IN_USERNAME,"${user?.firstName} ${user?.lastName}")
                editor.apply()

                when(activity){
                    is LoginActivity ->{
                        activity.userDetailsSuccess(user!!)
                    }

                }

            }
            .addOnFailureListener { e ->
        when(activity){
            is LoginActivity ->{
                activity.loading.isDismiss()
            }
        }
            }
    }

    fun updateUserProfileData(activity: Activity, userHashMap: HashMap<String, Any>){
        myFirestore.collection(Constants.USERS)
            .document(getCurrentUserId())

            .update(userHashMap) // Error Here video no 46

            .addOnSuccessListener {
            when (activity){
                is ProfileActivity -> {
                    activity.userProfileUpdateSuccess()

                }
            }
        }.addOnFailureListener { e ->
            when (activity){
                is ProfileActivity -> {
                    activity.loading.isDismiss()
                }
            }
            Log.e(activity.javaClass.simpleName,"Error while upadating the user details.",e)
        }
    }

    // A function to upload the image to the cloud storage.
    fun uploadImageToCloudStorage(activity: Activity, imageFileURI: Uri?,imageType:String){
        val myRef : StorageReference = FirebaseStorage.getInstance().reference.child(imageType + System.currentTimeMillis() + "." + Constants.getFileExtension(activity,imageFileURI))
        myRef.putFile(imageFileURI!!).addOnSuccessListener { taskSnapshot ->

            Log.e("Firebase Image URL",
                taskSnapshot.metadata!!.reference!!.downloadUrl.toString())

            // Get the downloadable url from the task snapshot
            taskSnapshot.metadata!!.reference!!.downloadUrl.addOnSuccessListener { uri ->
                Log.e("Download Image URL",uri.toString())

                // Here call a function of base activity for transferring the result to it.
                when(activity){
                    is ProfileActivity -> {
                        activity.imageUploadSuccess(uri.toString())
                    }
                }
            }.addOnFailureListener { exception ->
                when (activity){
                    is ProfileActivity -> {
                        activity.loading.isDismiss()
                    }
                }
                Log.e(
                    activity.javaClass.simpleName,
                    exception.message,
                    exception
                )
            }

        }
    }


}