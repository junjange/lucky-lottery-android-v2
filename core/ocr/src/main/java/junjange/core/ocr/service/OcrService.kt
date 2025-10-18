package junjange.core.ocr.service

import android.graphics.Bitmap
import com.googlecode.tesseract.android.TessBaseAPI
import java.io.File
import javax.inject.Inject

class OcrService @Inject constructor(
    private val tess: TessBaseAPI,
) {
    fun getTextOfImage(imagePath: File): String =
        with(tess) {
            setImage(imagePath)
            utF8Text
        }

    fun getTextOfImage(image: Bitmap): String =
        with(tess) {
            setImage(image)
            utF8Text
        }

    fun close() {
        tess.recycle()
    }
}
