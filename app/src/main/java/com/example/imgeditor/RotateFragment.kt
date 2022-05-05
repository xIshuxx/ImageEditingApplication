package com.example.imgeditor

import android.app.Activity
import android.content.Context
import android.content.ContextWrapper
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import androidx.fragment.app.Fragment
import com.example.imgeditor.databinding.FragmentRotateBinding


class RotateFragment(imgEditing: ImgEditing) :Fragment(){
    lateinit var binding: FragmentRotateBinding
    var activity=imgEditing

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view= inflater.inflate(R.layout.fragment_rotate,container,false)
        val btn= view?.findViewById<Button>(R.id.BtnRotate)
        btn?.setOnClickListener {
            activity.binding.imageView.setImageBitmap(null)
        }
        return view
    }


}