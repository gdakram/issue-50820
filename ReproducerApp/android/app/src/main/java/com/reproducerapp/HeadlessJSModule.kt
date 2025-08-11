package com.reproducerapp

import android.util.Log
import com.facebook.react.bridge.ReactApplicationContext
import com.facebook.react.bridge.ReactContextBaseJavaModule
import com.facebook.react.bridge.ReactMethod

public class HeadlessJSModule internal constructor(private val context: ReactApplicationContext?) :
    ReactContextBaseJavaModule(context) {

    override fun getName(): String {
        return "HeadlessJSModule"
    }

    @ReactMethod
    public fun updateContext() {
        currentContext = context
    }

    public companion object {
        public var currentContext: ReactApplicationContext? = null
    }
}