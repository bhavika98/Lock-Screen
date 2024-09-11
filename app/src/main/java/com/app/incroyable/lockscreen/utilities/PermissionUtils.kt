package com.app.incroyable.lockscreen.utilities

import android.Manifest
import android.app.Activity
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Build
import android.provider.Settings
import androidx.core.content.ContextCompat
import com.karumi.dexter.Dexter
import com.karumi.dexter.MultiplePermissionsReport
import com.karumi.dexter.PermissionToken
import com.karumi.dexter.listener.PermissionRequest
import com.karumi.dexter.listener.multi.MultiplePermissionsListener
import com.app.incroyable.lockscreen.R

object PermissionUtils {

    private val storagePermissions = if (isTiramisuDevice()) {
        listOf(
            Manifest.permission.READ_MEDIA_IMAGES
        )
    } else {
        listOf(
            Manifest.permission.READ_EXTERNAL_STORAGE,
            Manifest.permission.WRITE_EXTERNAL_STORAGE
        )
    }

    private val phonePermissions = listOf(
        Manifest.permission.READ_PHONE_STATE
    )

    fun checkStoragePermission(context: Context, onPermissionGranted: () -> Unit) {
        val permissionsToRequest = storagePermissions.filter {
            ContextCompat.checkSelfPermission(context, it) != PackageManager.PERMISSION_GRANTED
        }.toTypedArray()

        if (permissionsToRequest.isNotEmpty()) {
            askPermission(context, storagePermissions, onPermissionGranted)
        } else {
            onPermissionGranted()
        }
    }

    fun checkPhonePermission(context: Context, onPermissionGranted: () -> Unit) {
        val permissionsToRequest = phonePermissions.filter {
            ContextCompat.checkSelfPermission(context, it) != PackageManager.PERMISSION_GRANTED
        }.toTypedArray()

        if (permissionsToRequest.isNotEmpty()) {
            askPermission(context, phonePermissions, onPermissionGranted)
        } else {
            onPermissionGranted()
        }
    }

    private fun askPermission(
        context: Context,
        mainPermissions: List<String>,
        onPermissionGranted: () -> Unit
    ) {
        Dexter.withContext(context)
            .withPermissions(mainPermissions)
            .withListener(object : MultiplePermissionsListener {
                override fun onPermissionsChecked(report: MultiplePermissionsReport) {
                    if (report.areAllPermissionsGranted()) {
                        onPermissionGranted()
                    }
                    if (report.isAnyPermissionPermanentlyDenied) {
                        showPermissionRationale(context, null, true)
                    }
                }

                override fun onPermissionRationaleShouldBeShown(
                    permissions: MutableList<PermissionRequest>,
                    token: PermissionToken
                ) {
                    showPermissionRationale(context, token, false)
                }
            })
            .onSameThread()
            .check()
    }

    private fun showPermissionRationale(
        context: Context,
        token: PermissionToken?,
        redirect: Boolean
    ) {
        if (context is Activity) {
            context.commonDialog(
                layoutResId = R.layout.dialog_permission,
                cancelable = true,
                title = context.getString(R.string.permission_rationale_title),
                message = context.getString(R.string.permission_rationale_message),
                positiveClickListener = { dialog, _ ->
                    dialog.dismiss()
                    if (redirect)
                        openAppSettings(context)
                    else
                        token?.continuePermissionRequest()
                },
                negativeClickListener = {
                    token?.cancelPermissionRequest()
                },
                outsideTouchListener = {
                    token?.cancelPermissionRequest()
                }
            )
        }
    }

    private fun openAppSettings(context: Context) {
        val intent = Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS)
        intent.data = Uri.fromParts("package", context.packageName, null)
        if (context is Activity) {
            context.startActivityForResult(intent, requestCodeSetting)
        }
    }

    private fun isTiramisuDevice(): Boolean {
        return Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU
    }

    private var overlayPermissionCallback: (() -> Unit)? = null

    fun checkOverlayPermission(activity: Activity, onPermissionGranted: () -> Unit) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M && !Settings.canDrawOverlays(activity)) {
            overlayPermissionCallback = onPermissionGranted
            showOverlayPermissionDialog(activity)
        } else {
            onPermissionGranted()
        }
    }

    fun handleOverlayPermissionResult(activity: Activity, requestCode: Int, resultCode: Int) {
        if (requestCode == requestCodeOverlay) {
            overlayPermissionCallback?.let {
                if (if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                        Settings.canDrawOverlays(activity)
                    } else {
                        TODO("VERSION.SDK_INT < M")
                    }
                ) {
                    it()
                }
                overlayPermissionCallback = null
            }
        }
    }

    private fun showOverlayPermissionDialog(activity: Activity) {
        activity.commonDialog(
            layoutResId = R.layout.dialog_overlay_permission,
            cancelable = true,
            title = activity.getString(R.string.overlay_permission_title),
            message = activity.getString(R.string.overlay_permission_subtitle),
            positiveClickListener = { dialog, _ ->
                dialog.dismiss()
                openOverlaySettings(activity)
            },
            negativeClickListener = {

            }
        )
    }

    private fun openOverlaySettings(activity: Activity) {
        val intent = Intent(Settings.ACTION_MANAGE_OVERLAY_PERMISSION)
        intent.data = Uri.fromParts("package", activity.packageName, null)
        activity.startActivityForResult(intent, requestCodeOverlay)
    }
}

