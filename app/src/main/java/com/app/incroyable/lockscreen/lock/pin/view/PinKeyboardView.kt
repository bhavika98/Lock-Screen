package com.app.incroyable.lockscreen.lock.pin.view

import android.content.Context
import android.util.AttributeSet
import android.view.View
import android.widget.ImageView
import android.widget.LinearLayout
import com.app.incroyable.lockscreen.R
import com.app.incroyable.lockscreen.lock.pin.enums.PinButtons

class PinKeyboardView @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0
) : LinearLayout(context, attrs, defStyleAttr) {

    private lateinit var pin0: ImageView
    private lateinit var pin1: ImageView
    private lateinit var pin2: ImageView
    private lateinit var pin3: ImageView
    private lateinit var pin4: ImageView
    private lateinit var pin5: ImageView
    private lateinit var pin6: ImageView
    private lateinit var pin7: ImageView
    private lateinit var pin8: ImageView
    private lateinit var pin9: ImageView
    private lateinit var pinDot: ImageView
    private lateinit var pinDelete: ImageView
    private var pinEntryView: PinEntryView? = null

    init {
        init()
    }

    private fun init() {
        val params = LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT)
        val view = View.inflate(context, R.layout.pin_keyboard, null)
        setupButtons(view)
        setupListeners()
        addView(view, params)
    }

    private fun setupButtons(view: View) {
        pin0 = view.findViewById(R.id.pin0)
        pin1 = view.findViewById(R.id.pin1)
        pin2 = view.findViewById(R.id.pin2)
        pin3 = view.findViewById(R.id.pin3)
        pin4 = view.findViewById(R.id.pin4)
        pin5 = view.findViewById(R.id.pin5)
        pin6 = view.findViewById(R.id.pin6)
        pin7 = view.findViewById(R.id.pin7)
        pin8 = view.findViewById(R.id.pin8)
        pin9 = view.findViewById(R.id.pin9)
        pinDelete = view.findViewById(R.id.pinDelete)
        pinDot = view.findViewById(R.id.pinDot)
    }

    private fun setupListeners() {
        pin0.setOnClickListener {
            getVibrate()
            setListener(PinButtons.BUTTON_0)
        }
        pin1.setOnClickListener {
            getVibrate()
            setListener(PinButtons.BUTTON_1)
        }
        pin2.setOnClickListener {
            getVibrate()
            setListener(PinButtons.BUTTON_2)
        }
        pin3.setOnClickListener {
            getVibrate()
            setListener(PinButtons.BUTTON_3)
        }
        pin4.setOnClickListener {
            getVibrate()
            setListener(PinButtons.BUTTON_4)
        }
        pin5.setOnClickListener {
            getVibrate()
            setListener(PinButtons.BUTTON_5)
        }
        pin6.setOnClickListener {
            getVibrate()
            setListener(PinButtons.BUTTON_6)
        }
        pin7.setOnClickListener {
            getVibrate()
            setListener(PinButtons.BUTTON_7)
        }
        pin8.setOnClickListener {
            getVibrate()
            setListener(PinButtons.BUTTON_8)
        }
        pin9.setOnClickListener {
            getVibrate()
            setListener(PinButtons.BUTTON_9)
        }
        pinDot.setOnClickListener {
            setListener(PinButtons.BUTTON_DOT)
        }
        pinDelete.setOnClickListener {
            setListener(PinButtons.BUTTON_DELETE)
        }
    }

    private fun getVibrate() {
        // Perform vibration logic
    }

    private fun setListener(which: PinButtons) {
        try {
            pinEntryView?.sendKey(which)
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    fun setPinEntryView(view: PinEntryView) {
        pinEntryView = view
    }
}
