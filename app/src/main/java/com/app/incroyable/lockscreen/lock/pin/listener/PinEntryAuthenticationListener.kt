package com.app.incroyable.lockscreen.lock.pin.listener

interface PinEntryAuthenticationListener {
    fun onPinCorrect()
    fun onPinWrong()
}