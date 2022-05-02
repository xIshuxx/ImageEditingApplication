package com.example.imgeditor

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.example.imgeditor.databinding.ActivityImgEditingBinding

class ImgEditing : AppCompatActivity() {

    private lateinit var binding:ActivityImgEditingBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding= ActivityImgEditingBinding.inflate(layoutInflater)
        setContentView(binding.imgediting)
    }
}