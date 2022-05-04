package com.example.imgeditor

import android.graphics.BitmapFactory
import android.net.Uri
import android.os.Bundle
import android.provider.MediaStore
import android.widget.ImageView
import androidx.appcompat.app.AppCompatActivity
import androidx.core.net.toUri
import androidx.fragment.app.Fragment
import com.example.imgeditor.databinding.ActivityImgEditingBinding
import com.google.android.material.navigation.NavigationBarView


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
            val bitmap = MediaStore.Images.Media.getBitmap(this.contentResolver, imageUri)
            binding.imageView.setImageBitmap(bitmap)
            //binding.imageView.setImageURI(imageUri)
        }

        val rotateFragment= RotateFragment()
        val sharpenFragment=SharpenFragment()
        val filterFragment=FilterFragment()

        setCurrentFragment(sharpenFragment)


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