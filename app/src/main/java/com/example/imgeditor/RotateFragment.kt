package com.example.imgeditor

import android.graphics.Bitmap
import android.graphics.Matrix
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import androidx.core.view.drawToBitmap
import androidx.fragment.app.Fragment
import com.example.imgeditor.databinding.FragmentRotateBinding


class RotateFragment(imgEditing: ImgEditing) :Fragment(){


    private lateinit var binding: FragmentRotateBinding
    private lateinit var org:Bitmap
    private var activity=imgEditing
    private var counter:Float=0f




    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        org= activity.binding.imageView.drawToBitmap()
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_rotate,container,false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val btn= view.findViewById<Button>(R.id.BtnRotate)
        btn?.setOnClickListener {

            activity.binding.imageView.setImageBitmap(rotateImage90deg())
        }
    }

    private fun rotateImage90deg():Bitmap{

        var rotated:Bitmap
        counter +=90
        if(counter==360f){
            counter=0f
        }
        val matrix = Matrix()
        matrix.postRotate(counter)
        rotated = Bitmap.createBitmap(
            org, 0, 0,
            org.getWidth(), org.getHeight(),
            matrix, true
        )

        return rotated
    }





}