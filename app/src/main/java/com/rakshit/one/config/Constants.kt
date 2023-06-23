package com.test.papers.config

import android.os.Build
import java.util.*

object ConstantsFirestore {
    const val NAME = "name"
    const val IMAGE = "image"
    const val EMAIL = "email"
    const val UID = "uid"
}

fun getDeviceID(): String {
    return UUID.randomUUID().toString()
}

fun getDeviceModel(): String {
    return Build.MANUFACTURER + " - " + Build.MODEL
}

fun getVersion(): String {
    return Build.VERSION.SDK_INT.toString()
}