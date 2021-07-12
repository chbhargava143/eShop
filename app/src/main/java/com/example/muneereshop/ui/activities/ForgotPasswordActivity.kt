package com.example.muneereshop.ui.activities

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.ImageView
import android.widget.Toast
import com.example.muneereshop.databinding.ActivityForgotPasswordBinding
import com.example.muneereshop.progressbar.DialogueProgress
import com.google.firebase.auth.FirebaseAuth

class ForgotPasswordActivity : AppCompatActivity() {
    private lateinit var binding : ActivityForgotPasswordBinding
    var loading = DialogueProgress(this)
    private lateinit var click_Back : ImageView
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityForgotPasswordBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.clickSubmit.setOnClickListener {

            val email:String = binding.passwordForgot.text.toString().trim{ it <= ' '}
            if (email.isEmpty()){

                Toast.makeText(this@ForgotPasswordActivity,"Please Enter Email ID",Toast.LENGTH_SHORT).show()
            }else{
                loading.startLoading()
                FirebaseAuth.getInstance().sendPasswordResetEmail(email).addOnCompleteListener { task ->
                    loading.isDismiss()
                    if (task.isSuccessful){
                        Toast.makeText(this@ForgotPasswordActivity,"Password Sent Successfully",Toast.LENGTH_SHORT).show()
                        finish()
                    }else{
                        loading.isDismiss()
                        Toast.makeText(this@ForgotPasswordActivity,task.exception!!.message.toString(),Toast.LENGTH_SHORT).show()
                    }
                }
            }
        }
    }





}