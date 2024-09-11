package com.app.incroyable.lockscreen.lock.pin.view

import android.content.Context
import android.util.AttributeSet
import android.view.View
import android.widget.LinearLayout
import com.app.incroyable.lockscreen.R
import com.app.incroyable.lockscreen.lock.pin.listener.PinEntryAuthenticationListener
import com.app.incroyable.lockscreen.lock.pin.listener.PinEntrySetupListener

class PinView @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0
) : LinearLayout(context, attrs, defStyleAttr) {
    private lateinit var pinEntryView: PinEntryView
    private lateinit var pinKeyboardView: PinKeyboardView

    init {
        init()
    }

    private fun init() {
        val params = LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT)
        val view = View.inflate(context, R.layout.pin_view, null)
        pinEntryView = view.findViewById(R.id.pinEntryView)
        pinKeyboardView = view.findViewById(R.id.pinKeyboardView)
        pinKeyboardView.setPinEntryView(pinEntryView)
        addView(view, params)
    }

    fun setModeSetup(listener: PinEntrySetupListener) {
        pinEntryView.setModeSetup()
        pinEntryView.setSetupListener(listener)
    }

    fun clearPin() {
        pinEntryView.unsetAllPins()
    }

    fun setModeAuthenticate(listener: PinEntryAuthenticationListener) {
        pinEntryView.setModeAuthenticate()
        pinEntryView.setupAuthenticationListener(listener)
    }

    fun setMessage(msg: String) {
        pinEntryView.setMsg(msg)
    }
}
