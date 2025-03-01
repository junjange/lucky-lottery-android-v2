package com.junjange.presentation.util

import android.content.Context
import android.view.View
import android.widget.Toast
import com.google.android.material.snackbar.Snackbar

fun Context.showToast(message: String) {
    val toast = Toast.makeText(this, message, Toast.LENGTH_SHORT)
    toast.show()
}

fun View.showSnackbar(
    message: String,
    action: Snackbar.() -> Unit = {},
) {
    val snackbar =
        Snackbar.make(this, message, Snackbar.LENGTH_SHORT).apply {
            action()
        }
    snackbar.show()
}
