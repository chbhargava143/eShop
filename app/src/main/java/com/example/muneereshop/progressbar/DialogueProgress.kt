package com.example.muneereshop.progressbar

import android.app.Activity
import android.app.AlertDialog
import com.example.muneereshop.R

class DialogueProgress(val mActivity:Activity) {
    private lateinit var isDialogue :AlertDialog
    fun startLoading(){

        /* set View*/
    val inflater = mActivity.layoutInflater
    val dialogView = inflater.inflate(R.layout.dialogue_progress,null)
        /* set Dialogue*/
        val builder = AlertDialog.Builder(mActivity)
        builder.setView(dialogView)
        builder.setCancelable(false)
        isDialogue = builder.create()
        isDialogue.show()
    }
    fun isDismiss(){
        isDialogue.dismiss()
    }

}