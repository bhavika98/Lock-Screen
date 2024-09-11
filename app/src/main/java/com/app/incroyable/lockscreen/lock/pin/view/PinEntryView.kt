package com.app.incroyable.lockscreen.lock.pin.view

import android.content.Context
import android.os.Vibrator
import android.util.AttributeSet
import android.view.View
import android.widget.LinearLayout
import android.widget.TextView
import com.app.incroyable.lockscreen.R
import com.app.incroyable.lockscreen.lock.pin.enums.PinButtons
import com.app.incroyable.lockscreen.lock.pin.listener.PinEntryAuthenticationListener
import com.app.incroyable.lockscreen.lock.pin.listener.PinEntrySetupListener
import com.app.incroyable.lockscreen.storage.getDefaultPreferences
import com.app.incroyable.lockscreen.storage.prefPinPassword
import com.app.incroyable.lockscreen.storage.setPreferences

class PinEntryView @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0
) : LinearLayout(context, attrs, defStyleAttr) {

    companion object {
        const val MODE_AUTHENTICATE = 1
        const val MODE_SETUP = 2

        private const val STATE_INITIAL = 1
        private const val STATE_CONFIRM = 2
    }

    private var mode = -1
    private var pin: String? = null

    private var state: Int = 0
    private var pinArray = IntArray(4)
    private var pinConfirmArray = IntArray(4)
    private var charIndex = -1

    private lateinit var tvMessage: TextView
    private var msg = ""

    private val imgViews = arrayOfNulls<PinImageView>(4)

    private var setupListener: PinEntrySetupListener? = null
    private var authenticationListener: PinEntryAuthenticationListener? = null

    init {
        init()
    }

    fun setMsg(msg: String) {
        this.msg = msg
        tvMessage.text = msg
    }

    fun setModeSetup() {
        unsetVariables()
        tvMessage.setText(R.string.pin_enter)
        this.mode = MODE_SETUP
        this.state = STATE_INITIAL
    }

    fun setModeAuthenticate() {
        this.mode = MODE_AUTHENTICATE
        unsetVariables()
        this.state = STATE_INITIAL
        if (!msg.equals(context.resources.getString(R.string.pin_enter_old), ignoreCase = true))
            tvMessage.setText(R.string.pin_to_unlock)
        this.pin = context.getDefaultPreferences(prefPinPassword) as String
    }

    fun setSetupListener(listener: PinEntrySetupListener) {
        this.setupListener = listener
    }

    fun setupAuthenticationListener(listener: PinEntryAuthenticationListener) {
        this.authenticationListener = listener
    }

    fun sendKey(key: PinButtons) {
        if (mode == -1) {
            throw Exception("Mode is not set")
        } else {
            processKey(key)
        }
    }

    private fun init() {
        val params = LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT)
        val view = View.inflate(context, R.layout.pin_selector, null)
        imgViews[0] = view.findViewById(R.id.pe0)
        imgViews[1] = view.findViewById(R.id.pe1)
        imgViews[2] = view.findViewById(R.id.pe2)
        imgViews[3] = view.findViewById(R.id.pe3)
        tvMessage = view.findViewById(R.id.tvMessage)
        addView(view, params)
    }

    private fun processKey(key: PinButtons) {
        if (mode == MODE_SETUP) {
            processKeyForSetup(key)
        } else if (mode == MODE_AUTHENTICATE) {
            processKeyForAuthentication(key)
        }
    }

    private fun processKeyForSetup(key: PinButtons) {
        when (key) {
            PinButtons.BUTTON_0, PinButtons.BUTTON_1, PinButtons.BUTTON_2, PinButtons.BUTTON_3,
            PinButtons.BUTTON_4, PinButtons.BUTTON_5, PinButtons.BUTTON_6, PinButtons.BUTTON_7,
            PinButtons.BUTTON_8, PinButtons.BUTTON_9 -> {
                if (charIndex == -1 && state == STATE_INITIAL) {
                    tvMessage.setText(R.string.pin_enter)
                }
                if (charIndex in -1..2) {
                    charIndex++
                    imgViews[charIndex]?.setSelected(true)

                    if (state == STATE_INITIAL) {
                        pinArray[charIndex] = key.ordinal
                    } else if (state == STATE_CONFIRM) {
                        pinConfirmArray[charIndex] = key.ordinal
                    }
                }
                if (charIndex == 3) {
                    processKeyEntryComplete()
                }
            }
            PinButtons.BUTTON_DOT -> {
                // Handle button dot if needed
            }
            PinButtons.BUTTON_DELETE -> {
                if (charIndex > -1) {
                    imgViews[charIndex]?.setSelected(false)
                    charIndex--
                }
            }
        }
    }

    private fun processKeyEntryComplete() {
        if (mode == MODE_SETUP) {
            if (state == STATE_INITIAL) {
                tvMessage.setText(R.string.pin_confirm)
                state = STATE_CONFIRM
                setupListener?.onPinEntered(getString(pinArray))
                unsetAllPins()
            } else if (state == STATE_CONFIRM) {
                setupListener?.onPinConfirmed(getString(pinConfirmArray))
                var pinEqual = true
                var setPin = ""
                for (i in 0 until 4) {
                    setPin += pinArray[i]
                    if (pinArray[i] != pinConfirmArray[i]) {
                        pinEqual = false
                        break
                    }
                }
                if (pinEqual) {
                    context.setPreferences(prefPinPassword, setPin)
                    setupListener?.onPinSet(setPin)
                } else {
                    presentErrorUI()
                    setupListener?.onPinMismatch()
                    state = STATE_INITIAL
                    tvMessage.setText(R.string.pin_mismatch)
                    unsetAllPins()
                    unsetVariables()
                }
            }
        } else if (mode == MODE_AUTHENTICATE) {
            if (pin.equals(getString(pinArray), ignoreCase = true)) {
                authenticationListener?.onPinCorrect()
            } else {
                presentErrorUI()
                unsetAllPins()
                unsetVariables()
                tvMessage.setText(R.string.pin_wrong)
                authenticationListener?.onPinWrong()
            }
        }
    }

    private fun processKeyForAuthentication(key: PinButtons) {
        when (key) {
            PinButtons.BUTTON_0, PinButtons.BUTTON_1, PinButtons.BUTTON_2, PinButtons.BUTTON_3,
            PinButtons.BUTTON_4, PinButtons.BUTTON_5, PinButtons.BUTTON_6, PinButtons.BUTTON_7,
            PinButtons.BUTTON_8, PinButtons.BUTTON_9 -> {
                if (charIndex == -1 && state == STATE_INITIAL) {
                    if (!msg.equals(
                            context.resources.getString(R.string.pin_enter_old),
                            ignoreCase = true
                        )
                    )
                        tvMessage.setText(R.string.pin_to_unlock)
                    else
                        tvMessage.setText(R.string.pin_enter_old)
                }
                if (charIndex in -1..2) {
                    charIndex++
                    imgViews[charIndex]?.setSelected(true)
                    pinArray[charIndex] = key.ordinal
                }
                if (charIndex == 3) {
                    processKeyEntryComplete()
                }
            }
            PinButtons.BUTTON_DOT -> {
                // Handle button dot if needed
            }
            PinButtons.BUTTON_DELETE -> {
                if (charIndex > -1) {
                    imgViews[charIndex]?.setSelected(false)
                    charIndex--
                }
            }
        }
    }

    private fun presentErrorUI() {
        for (iv in imgViews) {
            iv?.animateError()
        }
        val v = context.getSystemService(Context.VIBRATOR_SERVICE) as Vibrator
        val pattern = longArrayOf(100, 200)
        v.vibrate(pattern, -1)
    }

    fun unsetAllPins() {
        charIndex = -1
        postDelayed(
            {
                imgViews.forEach { iv ->
                    iv?.setSelected(false)
                }
            },
            200
        )
    }

    private fun unsetVariables() {
        for (i in 0 until 4) {
            pinArray[i] = -1
            pinConfirmArray[i] = -1
        }
    }

    private fun getString(array: IntArray): String {
        var text = ""
        for (i in array.indices) {
            text += array[i]
        }
        return text
    }
}
