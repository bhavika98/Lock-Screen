package com.app.incroyable.lockscreen.receiver

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.telephony.TelephonyManager
import com.app.incroyable.lockscreen.activity.LockActivity
import com.app.incroyable.lockscreen.storage.getDefaultPreferences
import com.app.incroyable.lockscreen.storage.prefAlreadyLock
import com.app.incroyable.lockscreen.storage.prefLockStatus

class CallReceiver : BroadcastReceiver() {

    private var lockStatus: Boolean = false

    private fun startLockScreenActivity(context: Context) {
        try {
            val intent = Intent(context, LockActivity::class.java)
            intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
            context.startActivity(intent)
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    private fun handlePhoneState(context: Context, state: String, callState: Int) {
        if (callState != previousCallState) {
            if (state.equals(TelephonyManager.EXTRA_STATE_IDLE, ignoreCase = true)) {
                if (isCallOngoing) {
                    isCallOngoing = false
                    if (lockStatus && context.getDefaultPreferences(prefAlreadyLock) as Boolean) {
                        startLockScreenActivity(context)
                    }
                }
            } else if (state.equals(TelephonyManager.EXTRA_STATE_OFFHOOK, ignoreCase = true)) {
                isCallOngoing = callState != 1
            } else if (state.equals(TelephonyManager.EXTRA_STATE_RINGING, ignoreCase = true)) {
                isCallOngoing = true
                sendPhoneStateBroadcast(context, true)
            }
            previousCallState = callState
        }
    }

    private fun sendPhoneStateBroadcast(context: Context, isCallReceived: Boolean) {
        val intent = Intent("PhoneStateLocalBroadcast")
        intent.putExtra("isCallReceived", isCallReceived)
        context.sendBroadcast(intent)
    }

    override fun onReceive(context: Context, intent: Intent) {
        lockStatus = context.getDefaultPreferences(prefLockStatus) as Boolean
        val state = intent.extras?.getString("state") ?: ""
        intent.action.equals("android.intent.action.NEW_OUTGOING_CALL")
        val callState = when (state) {
            TelephonyManager.EXTRA_STATE_IDLE -> 0
            TelephonyManager.EXTRA_STATE_OFFHOOK -> 2
            TelephonyManager.EXTRA_STATE_RINGING -> 1
            else -> 0
        }
        handlePhoneState(context, state, callState)
    }

    companion object {
        private var isCallOngoing: Boolean = false
        private var previousCallState: Int = 0
    }
}

