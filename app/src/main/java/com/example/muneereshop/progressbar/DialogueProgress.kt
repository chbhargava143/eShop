package com.example.muneereshop.progressbar

import android.app.Activity
import android.app.AlertDialog
import androidx.fragment.app.Fragment
import com.example.muneereshop.R
import com.example.muneereshop.databinding.DialogueProgressBinding

class DialogueProgress(val mActivity:Activity) {
    private lateinit var isDialogue :AlertDialog
    fun startLoading(){

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
class FragmentDialogueProgress(val fragment : Fragment){
    private lateinit var isDialogue :AlertDialog
    fun startLoading(){

        val inflater = fragment.layoutInflater
        val dialogView = inflater.inflate(R.layout.dialogue_progress,null)
        /* set Dialogue*/
        val builder = AlertDialog.Builder(fragment.context)
        builder.setView(dialogView)
        builder.setCancelable(false)
        isDialogue = builder.create()
        isDialogue.show()
    }
    fun isDismiss(){
        isDialogue.dismiss()
    }
}