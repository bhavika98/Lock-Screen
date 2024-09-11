package com.app.incroyable.lockscreen.receiver

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.os.Build
import android.os.Handler
import com.app.incroyable.lockscreen.service.LockService
import com.app.incroyable.lockscreen.service.RunService

class BootCompleteReceiver : BroadcastReceiver() {
    override fun onReceive(context: Context, intent: Intent) {
        Handler().postDelayed({
            if (!RunService.isServiceRunning(context, LockService::class.java)) {
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                    context.startForegroundService(Intent(context, LockService::class.java))
                } else {
                    context.startService(Intent(context, LockService::class.java))
                }
            }
        }, 1000)
    }
}
