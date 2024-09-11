package com.app.incroyable.lockscreen.service

import android.app.ActivityManager
import android.content.Context

object RunService {
    fun isServiceRunning(context: Context, cls: Class<LockService>): Boolean {
        val activityManager = context.getSystemService(Context.ACTIVITY_SERVICE) as ActivityManager
        val runningServices = activityManager.getRunningServices(Int.MAX_VALUE)
        for (runningServiceInfo in runningServices) {
            if (cls.name == runningServiceInfo.service.className) {
                return true
            }
        }
        return false
    }
}
