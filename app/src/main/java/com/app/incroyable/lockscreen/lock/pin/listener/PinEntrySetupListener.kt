package com.app.incroyable.lockscreen.lock.pin.listener

interface PinEntrySetupListener {
    fun onPinEntered(pin: String?)
    fun onPinConfirmed(pin: String?)
    fun onPinMismatch()
    fun onPinSet(pin: String?)
}