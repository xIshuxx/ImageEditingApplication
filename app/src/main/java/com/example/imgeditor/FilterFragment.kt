package com.example.imgeditor
import android.graphics.Bitmap
import android.graphics.Color.*
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import androidx.core.view.drawToBitmap
import androidx.fragment.app.Fragment
import kotlin.math.min

class FilterFragment(imgEditing: ImgEditing) :Fragment(){

    private var activity=imgEditing
    private var pixelArray: IntArray? = null
    private lateinit var bm:Bitmap
    private lateinit var org:Bitmap

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        org = activity.bitmap!!.copy(activity.bitmap!!.config,true)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_filter,container,false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)


        val btninvert= view.findViewById<Button>(R.id.BtnInvertfilter)
        val btnsepia= view.findViewById<Button>(R.id.Btnsepiafilter)
        val btnagbr= view.findViewById<Button>(R.id.Btnagbrfilter)
        val btngray= view.findViewById<Button>(R.id.BtnGrayfilter)
        val btnreset= view.findViewById<Button>(R.id.BtnReset)
        btninvert.setOnClickListener {
            applyFilter(ColorMatrices.Inverted)
        }
        btnagbr.setOnClickListener {
            applyFilter(ColorMatrices.AGBR)
        }
        btngray.setOnClickListener {
            applyFilter(ColorMatrices.BlackAndWhite)
        }
        btnsepia.setOnClickListener {
            applyFilter(ColorMatrices.Sepia)
        }
        btnreset.setOnClickListener{
            activity.binding.imageView.setImageBitmap(org)
            activity.bitmap=org
        }



    }
    private fun changePixelColor(pixel: Int, matrix: FloatArray): Int {

        val a: Int = alpha(pixel)
        val r: Int = red(pixel)
        val g: Int = green(pixel)
        val b: Int = blue(pixel)

        val newA: Int =
            min(
                (a * matrix[0 * 5 + 0] + r * matrix[0 * 5 + 1] + g * matrix[0 * 5 + 2] + b * matrix[0 * 5 + 3] + matrix[0 * 5 + 4]).toInt(),
                255
            )
        val newR: Int =
            min(
                (a * matrix[1 * 5 + 0] + r * matrix[1 * 5 + 1] + g * matrix[1 * 5 + 2] + b * matrix[1 * 5 + 3] + matrix[1 * 5 + 4]).toInt(),
                255
            )
        val newG: Int =
            min(
                (a * matrix[2 * 5 + 0] + r * matrix[2 * 5 + 1] + g * matrix[2 * 5 + 2] + b * matrix[2 * 5 + 3] + matrix[2 * 5 + 4]).toInt(),
                255
            )
        val newB: Int =
            min(
                (a * matrix[3 * 5 + 0] + r * matrix[3 * 5 + 1] + g * matrix[3 * 5 + 2] + b * matrix[3 * 5 + 3] + matrix[3 * 5 + 4]).toInt(),
                255
            )

        return argb(newA, newR, newG, newB)
    }

    private fun applyFilter(colorMatrix: ColorMatrices) {
        bm= org.copy(org.config,true)
        bm.let{
            pixelArray = IntArray(it.width * it.height)
            it.getPixels(pixelArray, 0, it.width, 0, 0, it.width, it.height)
        }
        pixelArray.let {
            if (it != null) {
                for (pixelIndex in it.indices) {
                    it.set(pixelIndex, changePixelColor(it[pixelIndex], colorMatrix.matrix))
                }
            }
        }
        bm.let {
            it.setPixels(pixelArray, 0, it.width, 0, 0, it.width, it.height)
            activity.binding.imageView.setImageBitmap(it)
            activity.bitmap=it
        }

    }








}