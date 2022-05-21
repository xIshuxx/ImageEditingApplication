package com.example.imgeditor

import android.graphics.Bitmap
import android.graphics.Matrix
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import androidx.core.view.drawToBitmap
import androidx.fragment.app.Fragment
import org.w3c.dom.Text


class RotateFragment(imgEditing: ImgEditing) :Fragment(){

    private lateinit var org:Bitmap
    private var activity=imgEditing
    private var counter:Float=0f

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

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
        val rtText= view.findViewById<TextView>(R.id.rotateText)
        btn?.setOnClickListener {
            org= activity.bitmap!!
            val str = rtText.text.toString()
            val num:Number
            if(str.isNotEmpty()){
                num= str.toInt()
            }else{
                num= 0
            }

            activity.binding.imageView.setImageBitmap(rotateImage90deg(org,num))
        }
    }

    private fun rotateImage90deg(org:Bitmap, num:Int):Bitmap{

        var rotated:Bitmap
        counter += num
        if(counter>=360f){
            counter -= 360
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