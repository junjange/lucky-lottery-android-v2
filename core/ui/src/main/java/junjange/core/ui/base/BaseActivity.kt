package junjange.core.ui.base

import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.enableEdgeToEdge

abstract class BaseActivity : ComponentActivity() {
    protected inline val TAG get() = this::class.java.simpleName
    protected open val activityTransition = ActivityTransition.Push

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        activityTransition.overridePendingTransition(this)
    }

    protected open fun handleException(throwable: Throwable) {
        Log.e(TAG, throwable.stackTraceToString())
        finish()
    }

    override fun finish() {
        super.finish()
        activityTransition.overridePendingPopTransition(this)
    }
}
