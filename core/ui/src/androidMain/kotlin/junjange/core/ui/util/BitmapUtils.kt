package junjange.core.ui.util

import android.content.Context
import android.graphics.Bitmap
import android.os.Environment
import java.io.File
import java.io.FileOutputStream
import java.io.IOException

@Throws(IOException::class)
fun saveBitmapToFile(
    context: Context,
    bitmap: Bitmap,
): File {
    val directory = File(context.getExternalFilesDir(Environment.DIRECTORY_PICTURES), "images")
    if (!directory.exists()) {
        directory.mkdirs()
    }
    val file = File(directory, "image.jpg")
    val outputStream = FileOutputStream(file)
    bitmap.compress(Bitmap.CompressFormat.JPEG, 100, outputStream)
    outputStream.flush()
    outputStream.close()
    return file
}
