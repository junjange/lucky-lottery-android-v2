package junjange.core.ocr.di

import android.content.Context
import com.googlecode.tesseract.android.TessBaseAPI
import org.koin.android.ext.koin.androidContext
import org.koin.dsl.module
import java.io.File
import java.io.FileOutputStream
import java.io.InputStream
import java.io.OutputStream

private const val KOR = "kor.traineddata"
private const val KOR_VERT = "kor_vert.traineddata"
private const val DIGITS = "digits.traineddata"
private const val DIGITS1 = "digits1.traineddata"
private const val DIGITS_COMMA = "digits_comma.traineddata"
private const val DIGITS_LAYER = "digits_layer.traineddata"

val ocrModule = module {
    single {
        val context = androidContext()
        val tess = TessBaseAPI()

        val dataPath = File(context.filesDir, "tesseract")
        val subdir = File(dataPath, "tessdata")

        checkDir(dataPath, subdir)
        checkTrainedData(
            context,
            subdir,
            KOR,
            KOR_VERT,
            DIGITS,
            DIGITS1,
            DIGITS_COMMA,
            DIGITS_LAYER,
        )

        tess.init(dataPath.absolutePath, "kor+kor_vert+digits+digits1+digits_comma+digits_layer")
        tess.setVariable(TessBaseAPI.VAR_CHAR_BLACKLIST, "!@#$%^&*()_+=-[]}{;:'\"\\|~`,./<>?")
        tess
    }
}

private fun checkDir(vararg dirs: File) {
    for (dir in dirs) with(dir) { if (!exists()) mkdir() }
}

private fun checkTrainedData(
    context: Context,
    dir: File,
    vararg languages: String,
) {
    for (language in languages) {
        with(File(dir, language)) {
            if (!exists()) copyFrom(context.assets.open(language))
        }
    }
}

private fun File.copyFrom(inputStream: InputStream) {
    val outputStream: OutputStream = FileOutputStream(this)

    val buffer = ByteArray(1024)
    while (true) {
        val byteCount = inputStream.read(buffer)
        if (byteCount < 0) break
        outputStream.write(buffer, 0, byteCount)
    }

    outputStream.flush()
    outputStream.close()

    inputStream.close()
}
