package com.app.incroyable.lockscreen.service

import android.annotation.SuppressLint
import android.app.*
import android.app.PendingIntent.FLAG_IMMUTABLE
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.os.Build
import android.os.IBinder
import android.os.SystemClock
import androidx.core.content.ContextCompat.*
import com.app.incroyable.lockscreen.R
import com.app.incroyable.lockscreen.activity.LockActivity
import com.app.incroyable.lockscreen.storage.*

class LockService : Service() {

    private var lockStatus: Boolean = false
    private lateinit var alarmManager: AlarmManager
    private lateinit var pendingIntent: PendingIntent

    private val screenStateReceiver: BroadcastReceiver = object : BroadcastReceiver() {
        @SuppressLint("WrongConstant")
        override fun onReceive(context: Context, intent: Intent) {
            if (context != null) {
                val action = intent.action
                if (action != "android.intent.action.SCREEN_ON") {
                    setPreferences(prefAlreadyLock, true)
                } else if (getDefaultPreferences(prefLockStatus) as Boolean) {
                    this@LockService.lock(context)
                }
            }
        }
    }

    private fun lock() {
        lock(true)
    }

    fun lock(context: Context) {
        try {
            val intent = Intent(context, LockActivity::class.java)
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
            startActivity(intent)
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    private fun lock(enabled: Boolean) {
        if (enabled) {
            val intentFilter = IntentFilter()
            intentFilter.addAction("android.intent.action.SCREEN_OFF")
            intentFilter.addAction("android.intent.action.SCREEN_ON")
            intentFilter.priority = 1000
            intentFilter.priority = 899
            registerReceiver(screenStateReceiver, intentFilter)
        } else {
            val broadcastReceiver = screenStateReceiver
            if (broadcastReceiver != null) {
                unregisterReceiver(broadcastReceiver)
            }
        }
    }

    private fun stopService() {
        stopForeground(true)
        stopSelf()
    }

    private fun createNotificationChannel() {
        if (lockStatus) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                val packageName = packageName
                val notificationChannel = NotificationChannel(
                    packageName,
                    resources.getString(R.string.app_name),
                    NotificationManager.IMPORTANCE_DEFAULT
                )
                notificationChannel.enableLights(false)
                notificationChannel.setShowBadge(false)
                notificationChannel.lockscreenVisibility = Notification.VISIBILITY_PRIVATE
                (getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager).createNotificationChannel(
                    notificationChannel
                )
                startForeground(
                    2, Notification.Builder(
                        this,
                        packageName
                    )
                        .setOnlyAlertOnce(true)
                        .setSmallIcon(R.mipmap.ic_launcher)
                        .setContentTitle(resources.getString(R.string.app_name))
                        .setContentText(resources.getString(R.string.lock_is_enable))
                        .setCategory(Notification.CATEGORY_SERVICE)
                        .build()
                )
            }
        }
    }

    private fun setupServiceRestartAlarm() {
        val intent = Intent(this, javaClass)
        intent.putExtra("RestartService", "LockScreen_Service_Restart")
        intent.`package` = packageName
        pendingIntent = PendingIntent.getService(applicationContext, 1, intent, FLAG_IMMUTABLE)
        alarmManager = applicationContext.getSystemService(Context.ALARM_SERVICE) as AlarmManager
        alarmManager.setRepeating(
            AlarmManager.ELAPSED_REALTIME,
            5000 + SystemClock.elapsedRealtime(),
            60000,
            pendingIntent
        )
    }

    override fun onBind(intent: Intent): IBinder? {
        return null
    }

    override fun onCreate() {
        super.onCreate()
        lockStatus = getDefaultPreferences(prefLockStatus) as Boolean
        createNotificationChannel()
        lock()
        setupServiceRestartAlarm()
    }

    override fun onDestroy() {
        super.onDestroy()
        val broadcastReceiver = screenStateReceiver
        if (broadcastReceiver != null) {
            unregisterReceiver(broadcastReceiver)
        }
        val alarmManager = alarmManager
        if (alarmManager != null) {
            alarmManager.cancel(pendingIntent)
        }
    }

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        if (intent?.action == "stopForeGroundService") {
            stopService()
        } else if (intent?.action == "startForeGroundService") {
            createNotificationChannel()
        }
        return START_STICKY
    }
}

