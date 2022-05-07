package com.example.imgeditor
import android.graphics.Bitmap
import android.os.Bundle
import android.renderscript.*
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.SeekBar
import androidx.core.view.drawToBitmap
import androidx.fragment.app.Fragment
import com.example.imgeditor.databinding.FragmentSharpenBinding


class SharpenFragment(imgEditing: ImgEditing) :Fragment(){
    private lateinit var binding: FragmentSharpenBinding
    private var activity=imgEditing
    private var amount:Float = 50f
    private var radius:Float= 10f
    private var threshold:Int= 20

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
        val btn= view.findViewById<Button>(R.id.BtnSharpen)
        val seekbarAmount= view.findViewById<SeekBar>(R.id.seekBarAmount)
        val seekbarRadius=view.findViewById<SeekBar>(R.id.seekBarRadius)
        val seekbarThreshold=view.findViewById<SeekBar>(R.id.seekBarThreshold)
        seekbarAmount.max=100
        seekbarRadius.max=10
        seekbarThreshold.max=255

        seekbarAmount.setProgress(amount.toInt())
        seekbarRadius.setProgress(radius.toInt())
        seekbarThreshold.setProgress(threshold)

        btn?.setOnClickListener {
            Log.d("test","print this shit")
            activity.binding.imageView.setImageBitmap(sharpenImage(view))
        }
    }

    fun sharpenImage(view: View):Bitmap{
        val seekbarAmount= view.findViewById<SeekBar>(R.id.seekBarAmount)
        val seekbarRadius=view.findViewById<SeekBar>(R.id.seekBarRadius)
        val seekbarThreshold=view.findViewById<SeekBar>(R.id.seekBarThreshold)
        amount= seekbarAmount.progress.toFloat()
        radius= seekbarRadius.progress.toFloat()
        threshold=seekbarThreshold.progress
        var original:Bitmap= activity.bitmap!!
        //var blurred:Bitmap= fastBlur(original,1f,10)!!
        var blurred:Bitmap= blur(original,radius)
        var sharpened:Bitmap = addImages(original,
        filteredImage(original,blurred,amount, threshold ))

        return sharpened
    }
    fun blur(original: Bitmap, radius: Float): Bitmap {
        var bitmap: Bitmap =
            Bitmap.createBitmap(original.width, original.height, Bitmap.Config.ARGB_8888)
        var rs = RenderScript.create(activity)
        var allocIn = Allocation.createFromBitmap(rs, original)
        var allocOut = Allocation.createFromBitmap(rs, bitmap)
        var boxblur = ScriptIntrinsicBlur.create(rs, Element.U8_4(rs))
        boxblur.setInput(allocIn)
        boxblur.setRadius(radius)
        boxblur.forEach(allocOut)
        allocOut.copyTo(bitmap)
        rs.destroy()
        return bitmap

    }

    fun filteredImage(original: Bitmap, blurred: Bitmap, amount: Float, threshold: Int): Bitmap {
        var bitmap: Bitmap = original.copy(Bitmap.Config.ARGB_8888, true)
        for (x in 0 until original.width) {
            for (y in 0 until original.height) {
                val argb1: Int = original.getPixel(x, y)
                val argb2: Int = blurred.getPixel(x, y)


                val r1 = argb1 shr 16 and 0xFF
                val g1 = argb1 shr 8 and 0xFF
                val b1 = argb1 and 0xFF

                val r2 = argb2 shr 16 and 0xFF
                val g2 = argb2 shr 8 and 0xFF
                val b2 = argb2 and 0xFF

                val rDiff = if (Math.abs(r2 - r1) >= threshold) Math.abs(r2 - r1) else 0
                val gDiff = if (Math.abs(g2 - g1) >= threshold) Math.abs(g2 - g1) else 0
                val bDiff = if (Math.abs(b2 - b1) >= threshold) Math.abs(b2 - b1) else 0
                val alpha = ((amount / 100) * 255).toInt()
                val diff = (alpha shl 24) or (rDiff shl 16) or (gDiff shl 8) or bDiff
                bitmap.setPixel(x, y, diff)

            }
        }


        return bitmap
    }

    fun addImages(original: Bitmap, filtered: Bitmap): Bitmap {
        var bitmap: Bitmap = original.copy(Bitmap.Config.ARGB_8888, true)
        for (x in 0 until original.width) {
            for (y in 0 until original.height) {
                val argb1: Int = original.getPixel(x, y)
                val argb2: Int = filtered.getPixel(x, y)


                val r1 = argb1 shr 16 and 0xFF
                val g1 = argb1 shr 8 and 0xFF
                val b1 = argb1 and 0xFF

                val r2 = argb2 shr 16 and 0xFF
                val g2 = argb2 shr 8 and 0xFF
                val b2 = argb2 and 0xFF

                val rDiff = if (r2 + r1 > 255) 255 else r2 + r1
                val gDiff = if (g2 + g1 > 255) 255 else g2 + g1
                val bDiff = if (b2 + b1 > 255) 255 else b2 + b1
                val diff = (255 shl 24) or (rDiff shl 16) or (gDiff shl 8) or bDiff
                bitmap.setPixel(x, y, diff)
            }
        }
        return bitmap


    }


    private fun fastBlur(sentBitmap: Bitmap, scale: Float, radius: Int): Bitmap? {
        var sentBitmap = sentBitmap
        val width = Math.round(sentBitmap.width * scale)
        val height = Math.round(sentBitmap.height * scale)
        sentBitmap = Bitmap.createScaledBitmap(sentBitmap, width, height, false)
        val bitmap = sentBitmap.copy(sentBitmap.config, true)
        if (radius < 1) {
            return null
        }
        val w = bitmap.width
        val h = bitmap.height
        val pix = IntArray(w * h)
        Log.e("pix", w.toString() + " " + h + " " + pix.size)
        bitmap.getPixels(pix, 0, w, 0, 0, w, h)
        val wm = w - 1
        val hm = h - 1
        val wh = w * h
        val div = radius + radius + 1
        val r = IntArray(wh)
        val g = IntArray(wh)
        val b = IntArray(wh)
        var rsum: Int
        var gsum: Int
        var bsum: Int
        var x: Int
        var y: Int
        var i: Int
        var p: Int
        var yp: Int
        var yi: Int
        var yw: Int
        val vmin = IntArray(Math.max(w, h))
        var divsum = div + 1 shr 1
        divsum *= divsum
        val dv = IntArray(256 * divsum)
        i = 0
        while (i < 256 * divsum) {
            dv[i] = i / divsum
            i++
        }
        yi = 0
        yw = yi
        val stack = Array(div) {
            IntArray(
                3
            )
        }
        var stackpointer: Int
        var stackstart: Int
        var sir: IntArray
        var rbs: Int
        val r1 = radius + 1
        var routsum: Int
        var goutsum: Int
        var boutsum: Int
        var rinsum: Int
        var ginsum: Int
        var binsum: Int
        y = 0
        while (y < h) {
            bsum = 0
            gsum = bsum
            rsum = gsum
            boutsum = rsum
            goutsum = boutsum
            routsum = goutsum
            binsum = routsum
            ginsum = binsum
            rinsum = ginsum
            i = -radius
            while (i <= radius) {
                p = pix[yi + Math.min(wm, Math.max(i, 0))]
                sir = stack[i + radius]
                sir[0] = p and 0xff0000 shr 16
                sir[1] = p and 0x00ff00 shr 8
                sir[2] = p and 0x0000ff
                rbs = r1 - Math.abs(i)
                rsum += sir[0] * rbs
                gsum += sir[1] * rbs
                bsum += sir[2] * rbs
                if (i > 0) {
                    rinsum += sir[0]
                    ginsum += sir[1]
                    binsum += sir[2]
                } else {
                    routsum += sir[0]
                    goutsum += sir[1]
                    boutsum += sir[2]
                }
                i++
            }
            stackpointer = radius
            x = 0
            while (x < w) {
                r[yi] = dv[rsum]
                g[yi] = dv[gsum]
                b[yi] = dv[bsum]
                rsum -= routsum
                gsum -= goutsum
                bsum -= boutsum
                stackstart = stackpointer - radius + div
                sir = stack[stackstart % div]
                routsum -= sir[0]
                goutsum -= sir[1]
                boutsum -= sir[2]
                if (y == 0) {
                    vmin[x] = Math.min(x + radius + 1, wm)
                }
                p = pix[yw + vmin[x]]
                sir[0] = p and 0xff0000 shr 16
                sir[1] = p and 0x00ff00 shr 8
                sir[2] = p and 0x0000ff
                rinsum += sir[0]
                ginsum += sir[1]
                binsum += sir[2]
                rsum += rinsum
                gsum += ginsum
                bsum += binsum
                stackpointer = (stackpointer + 1) % div
                sir = stack[stackpointer % div]
                routsum += sir[0]
                goutsum += sir[1]
                boutsum += sir[2]
                rinsum -= sir[0]
                ginsum -= sir[1]
                binsum -= sir[2]
                yi++
                x++
            }
            yw += w
            y++
        }
        x = 0
        while (x < w) {
            bsum = 0
            gsum = bsum
            rsum = gsum
            boutsum = rsum
            goutsum = boutsum
            routsum = goutsum
            binsum = routsum
            ginsum = binsum
            rinsum = ginsum
            yp = -radius * w
            i = -radius
            while (i <= radius) {
                yi = Math.max(0, yp) + x
                sir = stack[i + radius]
                sir[0] = r[yi]
                sir[1] = g[yi]
                sir[2] = b[yi]
                rbs = r1 - Math.abs(i)
                rsum += r[yi] * rbs
                gsum += g[yi] * rbs
                bsum += b[yi] * rbs
                if (i > 0) {
                    rinsum += sir[0]
                    ginsum += sir[1]
                    binsum += sir[2]
                } else {
                    routsum += sir[0]
                    goutsum += sir[1]
                    boutsum += sir[2]
                }
                if (i < hm) {
                    yp += w
                }
                i++
            }
            yi = x
            stackpointer = radius
            y = 0
            while (y < h) {

                // Preserve alpha channel: ( 0xff000000 & pix[yi] )
                pix[yi] =
                    -0x1000000 and pix[yi] or (dv[rsum] shl 16) or (dv[gsum] shl 8) or dv[bsum]
                rsum -= routsum
                gsum -= goutsum
                bsum -= boutsum
                stackstart = stackpointer - radius + div
                sir = stack[stackstart % div]
                routsum -= sir[0]
                goutsum -= sir[1]
                boutsum -= sir[2]
                if (x == 0) {
                    vmin[y] = Math.min(y + r1, hm) * w
                }
                p = x + vmin[y]
                sir[0] = r[p]
                sir[1] = g[p]
                sir[2] = b[p]
                rinsum += sir[0]
                ginsum += sir[1]
                binsum += sir[2]
                rsum += rinsum
                gsum += ginsum
                bsum += binsum
                stackpointer = (stackpointer + 1) % div
                sir = stack[stackpointer]
                routsum += sir[0]
                goutsum += sir[1]
                boutsum += sir[2]
                rinsum -= sir[0]
                ginsum -= sir[1]
                binsum -= sir[2]
                yi += w
                y++
            }
            x++
        }
        Log.e("pix", w.toString() + " " + h + " " + pix.size)
        bitmap.setPixels(pix, 0, w, 0, 0, w, h)
        return bitmap
    }


}