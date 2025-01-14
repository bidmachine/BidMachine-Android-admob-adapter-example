package io.bidmachine.example

import android.os.Handler
import android.os.Looper
import android.view.View
import android.view.ViewGroup

private val UI_HANDLER = Handler(Looper.getMainLooper())

fun isUiThread(): Boolean {
    return Looper.myLooper() == Looper.getMainLooper()
}

fun onUiThread(runnable: Runnable) {
    if (isUiThread()) {
        runnable.run()
    } else {
        UI_HANDLER.post(runnable)
    }
}

fun View.removeViewFromParent() {
    if (parent is ViewGroup) {
        (parent as ViewGroup).removeView(this)
    }
}

fun ViewGroup.safeAddView(view: View) {
    try {
        view.removeViewFromParent()
        addView(view)
    } catch (e: Exception) {
        // Do nothing
    }
}