package junjange.core.ui.util

import android.content.Context
import android.widget.Toast
import androidx.annotation.StringRes

fun Context.showToast(message: String) {
    val toast = Toast.makeText(this, message, Toast.LENGTH_SHORT)
    toast.show()
}

fun Context.showToast(
    @StringRes res: Int,
) {
    val toast = Toast.makeText(this, this.getString(res), Toast.LENGTH_SHORT)
    toast.show()
}
