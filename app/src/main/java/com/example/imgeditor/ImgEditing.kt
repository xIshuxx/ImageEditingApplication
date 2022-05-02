package com.example.imgeditor

import android.graphics.BitmapFactory
import android.os.Bundle
import android.widget.ImageView
import androidx.appcompat.app.AppCompatActivity
import com.example.imgeditor.databinding.ActivityImgEditingBinding


class ImgEditing : AppCompatActivity() {

    private lateinit var binding:ActivityImgEditingBinding
    var bitmap: Bundle? =null


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

    }
}