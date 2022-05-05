package com.example.imgeditor
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import androidx.fragment.app.Fragment



class SharpenFragment(imgEditing: ImgEditing) :Fragment(){

    private var activity=imgEditing

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_sharpen,container,false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val btn= view?.findViewById<Button>(R.id.BtnSharpen)
        btn?.setOnClickListener {
            activity.binding.imageView.setImageBitmap(null)
        }
    }






}