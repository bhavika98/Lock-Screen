package com.app.incroyable.lockscreen.utilities

import android.content.Context
import android.util.Log
import android.widget.Toast

const val requestCodeImage: Int = 1
const val requestCodeSetting: Int = 2
const val requestCodeOverlay: Int = 3

//Condition
var imgReplace: Boolean = false

const val isLogMsg: Boolean = true

fun Context.toastMsg(msg: String) {
    Toast.makeText(this, msg, Toast.LENGTH_SHORT).show()
}

fun logMsg(msg: String) {
    if (isLogMsg)
        Log.e("BLOG", msg)
}