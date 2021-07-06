package com.example.muneereshop.activities

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.InputType
import android.text.TextUtils
import android.util.Log
import android.util.Log.i
import android.widget.EditText
import android.widget.Toast
import com.example.muneereshop.constants.Constants
import com.example.muneereshop.progressbar.DialogueProgress
import com.example.muneereshop.databinding.ActivityLoginBinding
import com.example.muneereshop.firebase.firestore.FireStores
import com.example.muneereshop.user.User
import com.google.android.gms.tasks.OnCompleteListener
import com.google.firebase.auth.FirebaseAuth

class LoginActivity : AppCompatActivity() {

    var loading = DialogueProgress(this)
    private lateinit var binding: ActivityLoginBinding
    private lateinit var et_email: EditText
    private lateinit var et_password: EditText
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        //setContentView(R.layout.activity_login)
        binding = ActivityLoginBinding.inflate(layoutInflater)
        setContentView(binding.root)
        et_email = binding.loginEmail
        et_password = binding.loginPassword
        et_password.inputType = InputType.TYPE_TEXT_VARIATION_PASSWORD
        binding.clickRegister.setOnClickListener {
            val intent = Intent(this@LoginActivity, RegisterActivity::class.java)
            startActivity(intent)
        }

        binding.loginButton.setOnClickListener {

            if (TextUtils.isEmpty(et_email.text.toString().trim { it <= ' ' })) {

                Toast.makeText(this, "Enter Email", Toast.LENGTH_SHORT).show()
            } else if (TextUtils.isEmpty(et_password.text.toString().trim { it <= ' ' })) {

                Toast.makeText(this, "Enter Password", Toast.LENGTH_SHORT).show()
            } else {
                loginUser()
            }
        }
        binding.forgotPassword.setOnClickListener {
            val intent = Intent(this@LoginActivity, ForgotPasswordActivity::class.java)
            startActivity(intent)
        }

    }

    fun loginUser() {
        loading.startLoading()
        val email = et_email.text.toString().trim { it <= ' ' }
        val password = et_password.text.toString().trim { it <= ' ' }
        FirebaseAuth.getInstance().signInWithEmailAndPassword(email, password)
            .addOnCompleteListener(
                OnCompleteListener { task ->
                    if (task.isSuccessful) {
                        FireStores().getUserDetails(this@LoginActivity)
                    } else {
                        loading.isDismiss()
                        Toast.makeText(
                            this@LoginActivity,
                            task.exception!!.message.toString(),
                            Toast.LENGTH_SHORT
                        ).show()
                    }

                }
            )
    }

    fun userDetailsSuccess(user: User) {
        loading.isDismiss()
        Log.i("First Name", user.firstName)
        Log.i("Last Name", user.lastName)
        Log.i("Email", user.email)
        if (user.profileCompleted == 0){
            val intent = Intent(this@LoginActivity,ProfileActivity::class.java)
            intent.putExtra(Constants.EXTRA_USER_DETAILS,user)
            startActivity(intent)
        }else {
            startActivity(Intent(this@LoginActivity,HomeActivity::class.java))
        }
        finish()
    }
}
/* val firebaseUserUID = FirebaseAuth.getInstance().currentUser!!.uid
Toast.makeText(this@LoginActivity,"You are LoggedIn Successfully",Toast.LENGTH_SHORT).show()
                  val intent = Intent(this@LoginActivity, HomeActivity::class.java)
                  intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
                  intent.putExtra("user_id",firebaseUserUID)
                  startActivity(intent)
                  finish()*/