package com.app.incroyable.lockscreen.lock.pin.utils

import android.animation.ObjectAnimator
import com.app.incroyable.lockscreen.lock.pin.view.PinImageView

object AnimationUtils {
    fun animatePinEntered(view: PinImageView) {
        val animator = ObjectAnimator.ofFloat(view, "alpha", 0.2f, 0.4f, 0.8f, 1.0f)
        animator.duration = 200
        animator.start()
    }

    fun animatePinError(view: PinImageView) {
        val animator = ObjectAnimator.ofFloat(
            view,
            "translationX",
            -2f, -4f, -6f, -8f, -6f, -4f, -2f, 0f, 2f, 4f, 6f, 8f, 6f, 4f, 2f
        )
        animator.duration = 200
        animator.repeatCount = 2
        animator.start()
    }
}
