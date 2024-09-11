package com.app.incroyable.lockscreen.lock.pin.view

import android.content.Context
import android.util.AttributeSet
import androidx.appcompat.widget.AppCompatImageView
import com.app.incroyable.lockscreen.lock.pin.utils.AnimationUtils

class PinImageView @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0
) : AppCompatImageView(context, attrs, defStyleAttr) {

    override fun setSelected(selected: Boolean) {
        super.setSelected(selected)
        if (selected) {
            AnimationUtils.animatePinEntered(this)
        }
    }

    fun animateError() {
        AnimationUtils.animatePinError(this)
    }
}
