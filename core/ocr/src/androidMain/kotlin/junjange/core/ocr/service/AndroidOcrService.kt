package junjange.core.ocr.service

import com.googlecode.tesseract.android.TessBaseAPI
import java.io.File

internal class AndroidOcrService(
    private val tess: TessBaseAPI,
) : OcrService {
    override fun getTextOfImage(imagePath: String): String =
        with(tess) {
            setImage(File(imagePath))
            utF8Text
        }
}
