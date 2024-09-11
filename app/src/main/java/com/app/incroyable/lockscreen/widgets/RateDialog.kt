package com.app.incroyable.lockscreen.widgets

import android.content.Context
import android.content.SharedPreferences
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.view.View
import android.view.Window
import android.widget.TextView
import androidx.appcompat.app.AppCompatDialog
import com.app.incroyable.lockscreen.R
import com.app.incroyable.lockscreen.storage.prefRate
import com.app.incroyable.lockscreen.storage.setPreferences
import com.app.incroyable.lockscreen.utilities.redirectApp

class RateDialog private constructor(
    context: Context,
    private val builder: Builder
) : AppCompatDialog(context), View.OnClickListener {

    companion object {
        private const val SESSION_COUNT = "session_count"
        private const val SHOW_NEVER = "show_never"
    }

    private val ratePrefs = "RateDialog"
    private lateinit var sharedpreferences: SharedPreferences
    private lateinit var tvNegative: TextView
    private lateinit var tvPositive: TextView
    private var session: Int = builder.session

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        requestWindowFeature(Window.FEATURE_NO_TITLE)
        window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        setContentView(R.layout.dialog_rate)

        tvNegative = findViewById<View>(R.id.dialog_button_negative) as TextView
        tvPositive = findViewById<View>(R.id.dialog_button_positive) as TextView

        tvPositive.setOnClickListener(this)
        tvNegative.setOnClickListener(this)
    }

    override fun onClick(view: View) {
        when (view.id) {
            R.id.dialog_button_negative -> {
                context.setPreferences(prefRate, false)
                dismiss()
            }
            R.id.dialog_button_positive -> {
                context.redirectApp(context.packageName)
                dismiss()
            }
        }
    }

    override fun show() {
        if (checkIfSessionMatches(session)) {
            super.show()
        }
    }

    private fun checkIfSessionMatches(session: Int): Boolean {
        if (session == 1) {
            return true
        }

        sharedpreferences = context.getSharedPreferences(ratePrefs, Context.MODE_PRIVATE)

        if (sharedpreferences.getBoolean(SHOW_NEVER, false)) {
            return false
        }

        val count = sharedpreferences.getInt(SESSION_COUNT, 1)

        return if (session == count) {
            val editor: SharedPreferences.Editor = sharedpreferences.edit()
            editor.putInt(SESSION_COUNT, 1)
            editor.apply()
            true
        } else if (session > count) {
            val newCount = count + 1
            val editor: SharedPreferences.Editor = sharedpreferences.edit()
            editor.putInt(SESSION_COUNT, newCount)
            editor.apply()
            false
        } else {
            val editor: SharedPreferences.Editor = sharedpreferences.edit()
            editor.putInt(SESSION_COUNT, 2)
            editor.apply()
            false
        }
    }

    class Builder(private val context: Context) {
        var session = 1

        fun session(session: Int): Builder {
            this.session = session
            return this
        }

        fun build(): RateDialog {
            return RateDialog(context, this)
        }
    }
}