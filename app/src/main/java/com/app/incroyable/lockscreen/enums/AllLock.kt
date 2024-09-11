package com.app.incroyable.lockscreen.enums

import com.app.incroyable.lockscreen.R

enum class AllLock(val layoutResId: Int) {
    One(R.layout.layout_screen_pin),
    Two(R.layout.layout_screen_main),
    Three(R.layout.layout_screen_pattern)
}