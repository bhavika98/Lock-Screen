package com.app.incroyable.lockscreen.storage

import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.net.Uri
import android.provider.MediaStore
import android.view.inputmethod.InputMethodManager
import android.widget.EditText
import com.app.incroyable.lockscreen.utilities.downloadFolder
import com.app.incroyable.lockscreen.utilities.tempFolder
import java.io.File
import java.io.InputStream
import java.text.SimpleDateFormat
import java.util.*

fun Context.loadBitmapFromAssets(path: String?): Bitmap? {
    var stream: InputStream? = null
    try {
        stream = this.assets.open(path!!)
        return BitmapFactory.decodeStream(stream)
    } catch (ignored: Exception) {
    } finally {
        try {
            stream?.close()
        } catch (ignored: Exception) {
        }
    }
    return null
}

fun Context.folderPath(): String {
    val picturesDir = this.cacheDir.path
    val folder = File(picturesDir, downloadFolder)

    if (!folder.exists()) {
        folder.mkdirs()
    }

    return folder.path + File.separator
}

fun Context.folderTemp(): String {
    val picturesDir = this.cacheDir.path
    val folder = File(picturesDir, tempFolder)

    if (!folder.exists()) {
        folder.mkdirs()
    }

    return folder.path + File.separator
}

fun Context.fetchFolder(): String {
    return this.cacheDir.path + File.separator + downloadFolder
}

fun Context.fetchTemp(): String {
    return this.cacheDir.path + File.separator + tempFolder
}

fun Context.getImagePath(uri: Uri?): String? {
    if (uri == null) {
        return null
    }
    val projection = arrayOf(MediaStore.Images.Media.DATA)
    val cursor = contentResolver.query(uri, projection, null, null, null)
    if (cursor != null) {
        val index = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA)
        cursor.moveToFirst()
        val path = cursor.getString(index)
        cursor.close()
        return path
    }
    return uri.path
}

fun timeFormat(pattern: String): String {
    val calendar = Calendar.getInstance()
    return SimpleDateFormat(pattern).format(calendar.time)
}

fun getPatternString(ids: ArrayList<Int>): String {
    var result = ""
    for (id in ids) {
        result += id.toString()
    }
    return result
}

fun dismissSoftKeyboard(inputMethodManager: InputMethodManager, addTxtEditText: EditText) {
    addTxtEditText.isFocusable = false
    addTxtEditText.isEnabled = false
    addTxtEditText.isFocusableInTouchMode = false
    addTxtEditText.isClickable = false
    inputMethodManager.hideSoftInputFromWindow(addTxtEditText.windowToken, 0)
    addTxtEditText.clearFocus()
}

fun showSoftKeyboard(inputMethodManager: InputMethodManager, addTxtEditText: EditText) {
    addTxtEditText.isFocusable = true
    addTxtEditText.isEnabled = true
    addTxtEditText.isFocusableInTouchMode = true
    addTxtEditText.isClickable = true
    inputMethodManager.toggleSoftInputFromWindow(
        addTxtEditText.applicationWindowToken,
        InputMethodManager.SHOW_FORCED,
        0
    )
    addTxtEditText.requestFocus()
}