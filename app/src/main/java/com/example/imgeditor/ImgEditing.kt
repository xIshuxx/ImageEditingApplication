package com.example.imgeditor

import android.graphics.BitmapFactory
import android.net.Uri
import android.os.Bundle
import android.widget.ImageView
import androidx.appcompat.app.AppCompatActivity
import androidx.core.net.toUri
import com.example.imgeditor.databinding.ActivityImgEditingBinding


class ImgEditing : AppCompatActivity() {

    private lateinit var binding:ActivityImgEditingBinding
    var imageUri: Uri? = null


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding= ActivityImgEditingBinding.inflate(layoutInflater)
        setContentView(binding.imgediting)

        if (intent.hasExtra("byteArray")) {
            val _imv = binding.imageView
            val _bitmap = BitmapFactory.decodeByteArray(
                intent.getByteArrayExtra("byteArray"),
                0,
                intent.getByteArrayExtra("byteArray")!!.size
            )
            _imv.setImageBitmap(_bitmap)
        }

        if(intent.hasExtra("theuri")){
            imageUri = Uri.parse(intent.getStringExtra("theuri"))
            binding.imageView.setImageURI(imageUri)
        }

    }
}