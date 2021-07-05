package com.example.muneereshop.firebase.firestore

import android.app.Activity
import android.content.Context
import android.content.SharedPreferences
import android.util.Log
import com.example.muneereshop.activities.LoginActivity
import com.example.muneereshop.activities.RegisterActivity
import com.example.muneereshop.constants.Constants
import com.example.muneereshop.user.User
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.SetOptions

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
}