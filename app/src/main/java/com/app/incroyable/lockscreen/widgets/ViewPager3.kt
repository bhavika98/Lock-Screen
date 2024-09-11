package com.app.incroyable.lockscreen.widgets

import android.content.Context
import android.util.AttributeSet
import android.view.MotionEvent
import androidx.viewpager.widget.ViewPager

class ViewPager3 @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null
) : ViewPager(context, attrs) {
    private var enabled = true

    override fun onTouchEvent(event: MotionEvent): Boolean {
        return if (enabled) {
            super.onTouchEvent(event)
        } else {
            false
        }
    }

    override fun onInterceptTouchEvent(event: MotionEvent): Boolean {
        return if (enabled) {
            super.onInterceptTouchEvent(event)
        } else {
            false
        }
    }

    fun setPagingEnabled(enabled: Boolean) {
        this.enabled = enabled
    }

    fun isPagingEnabled(): Boolean {
        return enabled
    }
}
