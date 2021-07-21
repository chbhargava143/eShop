package com.example.muneereshop.ui.fragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.DialogFragment
import com.example.muneereshop.R
import com.example.muneereshop.progressbar.DialogueProgress
import com.example.muneereshop.progressbar.FragmentDialogueProgress

class BaseFragment : Fragment() {
private val loading = FragmentDialogueProgress(this)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_base, container, false)
    }


}