package io.bidmachine.example

import android.os.Handler
import android.os.Looper

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