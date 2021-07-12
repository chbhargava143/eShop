package com.example.muneereshop.ui.activities

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.TextUtils
import android.widget.EditText
import android.widget.Toast
import com.example.muneereshop.progressbar.DialogueProgress
import com.example.muneereshop.user.User
import com.example.muneereshop.databinding.ActivityRegisterBinding
import com.example.muneereshop.firebase.firestore.FireStores
import com.google.android.gms.tasks.OnCompleteListener
import com.google.firebase.auth.AuthResult
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser

class RegisterActivity : AppCompatActivity() {
    var loading = DialogueProgress(this)
    private lateinit var binding: ActivityRegisterBinding
    private lateinit var firstName: EditText
    private lateinit var lastName: EditText
    private lateinit var email: EditText
    private lateinit var password: EditText
    private lateinit var confirm_Password: EditText


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityRegisterBinding.inflate(layoutInflater)
        setContentView(binding.root)
        firstName = binding.registerFirstName
        lastName = binding.registerLastName
        email = binding.registerEmail
        password = binding.registerPassword
        confirm_Password = binding.rgisterConfirmPassword


        binding.registerButton.setOnClickListener {
            if (TextUtils.isEmpty(firstName.text.toString().trim { it <= ' ' })) {
                Toast.makeText(this, "Enter First Name", Toast.LENGTH_SHORT).show()
            } else if (TextUtils.isEmpty(lastName.text.toString().trim { it <= ' ' })) {
                Toast.makeText(this, "Enter Last Name", Toast.LENGTH_SHORT).show()
            } else if (TextUtils.isEmpty(email.text.toString().trim { it <= ' ' })) {
                Toast.makeText(this, "Enter Email", Toast.LENGTH_SHORT).show()
            } else if (TextUtils.isEmpty(password.text.toString().trim { it <= ' ' })) {
                Toast.makeText(this, "Enter Password", Toast.LENGTH_SHORT).show()
            } else if (password.text.toString() != confirm_Password.text.toString()) {
                Toast.makeText(this, "Password Mismatched", Toast.LENGTH_SHORT).show()
            } else {
                registerUser()
            }
        }
        binding.clickLogin.setOnClickListener {
            onBackPressed()
        }

    }

    private fun registerUser() {
        loading.startLoading()
        val user_Mail = email.text.toString().trim { it <= ' ' }
        val user_Password = password.text.toString().trim { it <= ' ' }
        FirebaseAuth.getInstance().createUserWithEmailAndPassword(user_Mail, user_Password)
            .addOnCompleteListener(
                OnCompleteListener<AuthResult> { task ->


                    // Registration Succesfully
                    if (task.isSuccessful) {
                        val firebaseUser: FirebaseUser = task.result!!.user!!

                        // Instance of User data model class.
                        val user = User(
                            firebaseUser.uid,
                            firstName.text.toString().trim { it <= ' ' },
                            lastName.text.toString().trim { it <= ' ' },
                            email.text.toString().trim { it <= ' ' }
                        )
                        FireStores().registerUser(this@RegisterActivity,user)
                       // FireStore().registerUser(this@RegisterActivity,user)

                       // FirebaseAuth.getInstance().signOut()
                       // finish()

                    } else {
                        loading.isDismiss()
                        Toast.makeText(
                            this@RegisterActivity,
                            task.exception!!.message.toString(),
                            Toast.LENGTH_SHORT
                        ).show()
                    }
                }
            )
    }

    fun UserRegisteredOnSuccess() {
        loading.isDismiss()
        Toast.makeText(
            this@RegisterActivity,
            "You Are Registered Successfully",
            Toast.LENGTH_SHORT
        ).show()
        FirebaseAuth.getInstance().signOut()
        finish()
    }
}