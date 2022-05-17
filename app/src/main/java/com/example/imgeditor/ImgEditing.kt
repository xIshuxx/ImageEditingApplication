package com.example.imgeditor

import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.net.Uri
import android.os.Bundle
import android.provider.MediaStore
import android.widget.Button
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import com.example.imgeditor.databinding.ActivityImgEditingBinding

class ImgEditing : AppCompatActivity() {

    lateinit var binding:ActivityImgEditingBinding
    var imageUri: Uri? = null
    var bitmap:Bitmap?=null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding= ActivityImgEditingBinding.inflate(layoutInflater)
        setContentView(binding.imgediting)

        if (intent.hasExtra("byteArray")) {
                val _imv = binding.imageView
                bitmap = BitmapFactory.decodeByteArray(
                intent.getByteArrayExtra("byteArray"),
                0,
                intent.getByteArrayExtra("byteArray")!!.size
            )
            _imv.setImageBitmap(bitmap)
        }

        if(intent.hasExtra("theuri")){
            imageUri = Uri.parse(intent.getStringExtra("theuri"))
            bitmap = MediaStore.Images.Media.getBitmap(this.contentResolver, imageUri)
            binding.imageView.setImageBitmap(bitmap)
            //binding.imageView.setImageURI(imageUri)
        }

        val rotateFragment= RotateFragment(this)
        val sharpenFragment=SharpenFragment(this)
        val filterFragment=FilterFragment(this)

        setCurrentFragment(rotateFragment)

        binding.bottomNavigationView.setOnNavigationItemSelectedListener{
            when(it.itemId){
                R.id.miRotate->setCurrentFragment(rotateFragment)
                R.id.miSharpen->setCurrentFragment(sharpenFragment)
                R.id.miFilter->setCurrentFragment(filterFragment)
            }
            true
        }


    }

    private fun setCurrentFragment(fragment:Fragment)=supportFragmentManager.beginTransaction().apply {
        replace(R.id.flFragment,fragment)
        commit()
    }


}

