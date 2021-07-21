package com.example.muneereshop.utils

import android.os.Handler
import android.os.Looper
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity

open class BaseFunctions:AppCompatActivity (){


    private var doubleClickToGoBack = false
fun doubleClickToback(){
    if (doubleClickToGoBack) {
        super.onBackPressed()
        return
    }

    this.doubleClickToGoBack = true

    Toast.makeText(
        this,
        "Please click back again to exit",
        Toast.LENGTH_SHORT
    ).show()


    Handler(Looper.myLooper()!!).postDelayed({ doubleClickToGoBack = false }, 2000)
}
}