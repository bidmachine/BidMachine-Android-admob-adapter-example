package io.bidmachine.example

import android.content.Context
import android.util.Log
import android.widget.Toast
import com.google.android.gms.ads.AdError
import com.google.android.gms.ads.ResponseInfo

internal object AppLogger {

    private const val TAG = "AppLogger"

    private lateinit var applicationContext: Context

    fun initialize(context: Context) {
        applicationContext = context.applicationContext
    }

    fun log(tag: String, message: String) {
        log("[$tag] $message")
    }

    fun log(message: String) {
        Log.d(TAG, message)
    }

    fun logError(tag: String, message: String) {
        logError("[$tag] $message")
    }

    fun logError(message: String) {
        Log.e(TAG, message)
    }

    fun logEvent(type: String, eventName: String, responseInfo: ResponseInfo?) {
        logEvent(applicationContext, type, eventName, responseInfo)
    }

    fun logEvent(context: Context, type: String, eventName: String, responseInfo: ResponseInfo?) {
        val messageBuilder = StringBuilder(eventName)
        responseInfo?.mediationAdapterClassName?.substringAfterLast(".")
                ?.takeIf { it.isNotEmpty() }
                ?.let { messageBuilder.append(" - $it") }

        log(type, messageBuilder.toString())
        Toast.makeText(context, "$type - $eventName", Toast.LENGTH_SHORT).show()
    }

    fun logEvent(type: String, eventName: String, adError: AdError) {
        logEvent(applicationContext, type, eventName, adError)
    }

    fun logEvent(context: Context, type: String, eventName: String, adError: AdError) {
        logError(type, "$eventName with message - ${adError.message}")
        Toast.makeText(context, "$type - $eventName", Toast.LENGTH_SHORT).show()
    }
}