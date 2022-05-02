package com.example.imgeditor


import android.Manifest
import android.content.ActivityNotFoundException
import android.content.Intent
import android.graphics.Bitmap
import android.os.Build
import android.os.Bundle
import android.provider.MediaStore
import androidx.appcompat.app.AppCompatActivity
import com.example.imgeditor.databinding.ActivityMainBinding
import java.io.ByteArrayOutputStream


class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    val REQUEST_IMAGE_CAPTURE = 1
    val REQUEST_GALLERY=2



    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding= ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.BtnCamera.setOnClickListener{
            dispatchTakePictureIntent();
        }
        binding.BtnGallery.setOnClickListener{
            dispatachPickPhotoIntent();
        }

    }
    private fun dispatachPickPhotoIntent() {
        val intent = Intent(Intent.ACTION_PICK)
        intent.type = "image/*"
        startActivityForResult(intent, REQUEST_GALLERY)
    }
    private fun dispatchTakePictureIntent() {
        val takePictureIntent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
        try {
            startActivityForResult(takePictureIntent, REQUEST_IMAGE_CAPTURE)
        } catch (e: ActivityNotFoundException) {
            // display error state to the user
        }
    }




    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == REQUEST_IMAGE_CAPTURE && resultCode == RESULT_OK ||
            requestCode == REQUEST_GALLERY
                ) {
            val _intent = Intent(this, ImgEditing::class.java)
            var _bitmap: Bitmap = data?.extras?.get("data") as Bitmap

            val _bs = ByteArrayOutputStream()
            _bitmap.compress(Bitmap.CompressFormat.PNG, 50, _bs)
            _intent.putExtra("byteArray", _bs.toByteArray())
            startActivity(_intent)
        }
    }




}