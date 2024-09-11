package com.app.incroyable.lockscreen.utilities

import android.annotation.SuppressLint
import android.app.Activity
import android.app.Dialog
import android.content.*
import android.net.ConnectivityManager
import android.net.NetworkCapabilities
import android.net.Uri
import android.os.Build
import android.os.SystemClock
import android.text.SpannableString
import android.text.Spanned
import android.text.TextUtils
import android.text.style.ForegroundColorSpan
import android.view.LayoutInflater
import android.view.View
import android.widget.TextView
import androidx.appcompat.app.AlertDialog
import com.app.incroyable.lockscreen.R
import com.app.incroyable.lockscreen.storage.*
import com.app.incroyable.lockscreen.widgets.RateDialog
import java.text.SimpleDateFormat
import java.util.*

@SuppressLint("RestrictedApi")
fun Context.commonDialog(
    layoutResId: Int,
    cancelable: Boolean,
    title: String,
    message: String,
    positiveClickListener: (dialog: Dialog, dialogView: View) -> Unit,
    negativeClickListener: (View) -> Unit,
    outsideTouchListener: DialogInterface.OnCancelListener? = null
) {
    val dialogView = LayoutInflater.from(this).inflate(layoutResId, null)

    val builder = AlertDialog.Builder(this)
        .setView(dialogView)
        .setCancelable(cancelable)

    val dpi = this.resources.displayMetrics.density
    builder.setView(
        dialogView,
        (19 * dpi).toInt(),
        (5 * dpi).toInt(),
        (14 * dpi).toInt(),
        (5 * dpi).toInt()
    )

    val dialog = builder.create()
    dialog.show()

    dialog.window?.setBackgroundDrawableResource(android.R.color.transparent)

    val positiveButton = dialogView.findViewById<TextView>(R.id.dialog_button_positive)
    val negativeButton = dialogView.findViewById<TextView>(R.id.dialog_button_negative)
    val txtTitle = dialogView.findViewById<TextView>(R.id.dialog_title)
    val txtMessage = dialogView.findViewById<TextView>(R.id.dialog_message)

    if (title.isNotEmpty())
        txtTitle.text = title

    if (message.isNotEmpty()) {
        txtMessage.visible
        txtMessage.text = message
    }

    positiveButton.setOnClickListener {
        positiveClickListener(dialog, dialogView)
//        dialog.dismiss()
    }

    negativeButton.setOnClickListener {
        negativeClickListener(dialogView)
        dialog.dismiss()
    }

    dialog.setCanceledOnTouchOutside(cancelable)
    dialog.setOnCancelListener(outsideTouchListener)
}

private var lastClickTime: Long = 0

fun singleClick(action: () -> Unit) {
    if (SystemClock.elapsedRealtime() - lastClickTime < 1500L) {
        return
    }
    lastClickTime = SystemClock.elapsedRealtime()
    action.invoke()
}

fun Context.firstCharColor(originalString: String): SpannableString {
    val spannableString = SpannableString(originalString)
    val regexPattern = "\\b(\\w)".toRegex()
    val matches = regexPattern.findAll(originalString)
    for (match in matches) {
        val start = match.range.first
        val end = match.range.last + 1
        val colorSpan = ForegroundColorSpan(resources.getColor(R.color.colorAccent))
        spannableString.setSpan(colorSpan, start, end, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE)
    }
    return spannableString
}

fun Context.isOnline(): Boolean {
    val connectivityManager =
        getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
    return if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
        val networkCapabilities = connectivityManager.activeNetwork?.let {
            connectivityManager.getNetworkCapabilities(it)
        }
        networkCapabilities?.hasCapability(NetworkCapabilities.NET_CAPABILITY_INTERNET)
            ?: false
    } else {
        val activeNetworkInfo = connectivityManager.activeNetworkInfo
        activeNetworkInfo != null && activeNetworkInfo.isConnected
    }
}

fun Context.policyLink() {
    val privacyLink = getAdString(prefPrivacy)
    if (privacyLink.isNotEmpty()) {
        val intent = Intent(Intent.ACTION_VIEW, Uri.parse(privacyLink))
        if (intent.resolveActivity(packageManager) != null) {
            startActivity(intent)
        }
    }
}

fun Context.redirectApp(packageVal: String) {
    val playStoreNotAvailableMsg = getString(R.string.play_store_not_available)
    val turnOnConnectionMsg = getString(R.string.turn_on_connection)
    try {
        val uri = Uri.parse("market://details?id=$packageVal")
        val myAppLinkToMarket = Intent(Intent.ACTION_VIEW, uri)
        myAppLinkToMarket.flags = Intent.FLAG_ACTIVITY_NEW_TASK

        if (isOnline()) {
            startActivitySafe(myAppLinkToMarket) {
                toastMsg(playStoreNotAvailableMsg)
            }
        } else {
            toastMsg(turnOnConnectionMsg)
        }
    } catch (e: ActivityNotFoundException) {
        toastMsg(playStoreNotAvailableMsg)
    }
}

fun Context.startActivitySafe(intent: Intent, onError: () -> Unit = {}) {
    if (intent.resolveActivity(packageManager) != null) {
        startActivity(intent)
    } else {
        onError()
    }
}

fun Context.shareApp() {
    val appPackageName = packageName
    val appPlayStoreLink = "https://play.google.com/store/apps/details?id=$appPackageName"
    val intent = Intent(Intent.ACTION_SEND)
    intent.type = "text/plain"
    intent.putExtra(Intent.EXTRA_TEXT, "Check out this cool app: $appPlayStoreLink")
    startActivity(Intent.createChooser(intent, "Share via"))
}

fun Context.moreApp() {
    val publication = getAdString(prefPub)
    if (!TextUtils.isEmpty(publication) && !publication.equals("null", ignoreCase = true)) {
        val marketUri = Uri.parse("market://search?q=pub:$publication")
        val marketIntent = Intent(Intent.ACTION_VIEW, marketUri)
        if (marketIntent.resolveActivity(packageManager) != null) {
            startActivity(marketIntent)
        } else {
            toastMsg(getString(R.string.play_store_not_available))
        }
    }
}

fun Activity.networkError(
    onNetworkAvailable: () -> Unit,
    negativeClickListener: () -> Unit
) {
    if (!isOnline()) {
        showNetworkErrorPopup(onNetworkAvailable, negativeClickListener)
    } else {
        onNetworkAvailable()
    }
}

fun Activity.showNetworkErrorPopup(
    onNetworkAvailable: () -> Unit,
    negativeClickListener: () -> Unit
) {
    commonDialog(
        layoutResId = R.layout.dialog_network_error,
        cancelable = false,
        title = getString(R.string.network_error),
        message = getString(R.string.check_your_connection),
        positiveClickListener = { dialog, _ ->
            dialog.dismiss()
            networkError(onNetworkAvailable, negativeClickListener)
        },
        negativeClickListener = {
            negativeClickListener()
        }
    )
}

fun Context.currentVersion(): Int {
    val packageInfo = packageManager.getPackageInfo(packageName, 0)
    return if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.P) {
        packageInfo.longVersionCode.toInt()
    } else {
        packageInfo.versionCode
    }
}

fun Activity.forceUpdateDialog(
    title: String,
    message: String,
    latestPackage: String
) {
    commonDialog(layoutResId = R.layout.dialog_force_update,
        cancelable = false,
        title = title,
        message = message,
        positiveClickListener = { dialog, _ ->
            dialog.dismiss()
            redirectApp(latestPackage)
            finish()
        },
        negativeClickListener = {
            finishAffinity()
        })
}

fun Activity.rateDialog() {
    val secondTime = getDefaultPreferences(prefSecondTime) as Boolean
    if (!secondTime) {
        setPreferences(prefSecondTime, true)
    } else {
        val prefRate = getDefaultPreferences(prefRate) as Boolean
        if (!prefRate) {
            val currentDate = SimpleDateFormat("yyyyMMdd").format(Date())
            if (getDefaultPreferences(prefLastDate) != currentDate) {
                RateDialog.Builder(this).build().show()
                setPreferences(prefLastDate, currentDate)
            }
        }
    }
}