package com.app.incroyable.lockscreen.widgets

import android.animation.ValueAnimator
import android.animation.ValueAnimator.AnimatorUpdateListener
import android.annotation.SuppressLint
import android.content.Context
import android.util.AttributeSet
import android.view.LayoutInflater
import android.view.MotionEvent
import android.view.View
import android.widget.ImageView
import android.widget.RelativeLayout
import android.widget.Toast
import com.romainpiel.shimmer.Shimmer
import com.romainpiel.shimmer.ShimmerTextView
import com.app.incroyable.lockscreen.R

class SlideToActView : RelativeLayout {

    private var imgThumb: ImageView?
    private var textLabel: ShimmerTextView?

    private var sliding: Boolean
    private var thumbWidth: Int
    var initialSliderPosition: Int
    private var sliderPosition: Int

    var initialSlidingX: Float

    private var listener: OnUnlockListener?
    private var revealListener: OnRevealListener?

    internal inner class UpdateListener(private val params: LayoutParams) :
        AnimatorUpdateListener {
        override fun onAnimationUpdate(valueAnimator: ValueAnimator) {
            params.leftMargin = (valueAnimator.animatedValue as Int).toInt()
            imgThumb!!.requestLayout()
        }
    }

    interface OnRevealListener {
        fun onReveal(f: Float)
    }

    interface OnUnlockListener {
        fun onUnlock()
    }

    constructor(context: Context) : super(context) {
        listener = null
        revealListener = null
        textLabel = null
        imgThumb = null
        thumbWidth = 0
        sliding = false
        sliderPosition = 0
        initialSliderPosition = 0
        initialSlidingX = 0.0f
        init(context)
    }

    constructor(context: Context, attrs: AttributeSet?) : super(context, attrs) {
        listener = null
        revealListener = null
        textLabel = null
        imgThumb = null
        thumbWidth = 0
        sliding = false
        sliderPosition = 0
        initialSliderPosition = 0
        initialSlidingX = 0.0f
        init(context)
    }

    constructor(context: Context, attrs: AttributeSet?, defStyle: Int) : super(
        context,
        attrs,
        defStyle
    ) {
        listener = null
        revealListener = null
        textLabel = null
        imgThumb = null
        thumbWidth = 0
        sliding = false
        sliderPosition = 0
        initialSliderPosition = 0
        initialSlidingX = 0.0f
        init(context)
    }

    fun setOnUnlockListener(listener: OnUnlockListener?) {
        this.listener = listener
    }

    fun setOnRevealListener(revealListener: OnRevealListener?) {
        this.revealListener = revealListener
    }

    fun reset() {
        val animator = ValueAnimator.ofInt(
            *intArrayOf(
                (imgThumb!!.layoutParams as LayoutParams).leftMargin,
                0
            )
        )
        animator.addUpdateListener(UpdateListener(imgThumb!!.layoutParams as LayoutParams))
        animator.duration = 300
        animator.start()
        textLabel!!.alpha = 1f
        if (revealListener != null) {
            revealListener!!.onReveal(1f)
        }
    }

    private fun init(context: Context) {
        (context.getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater).inflate(
            R.layout.widget_slide_to_actview,
            this,
            true
        )
        textLabel = findViewById<View>(R.id.text_label) as ShimmerTextView
        imgThumb = findViewById<View>(R.id.img_thumb) as ImageView
        thumbWidth = dpToPx(135)
        val shimmer = Shimmer()
        shimmer.direction = Shimmer.ANIMATION_DIRECTION_LTR
        shimmer.duration = 1000
        shimmer.start(textLabel)
    }

    @SuppressLint("ClickableViewAccessibility")
    override fun onTouchEvent(event: MotionEvent): Boolean {
        super.onTouchEvent(event)
        if (event.action == 0) {
            if (event.x > sliderPosition.toFloat() && event.x < (sliderPosition + thumbWidth).toFloat()) {
                sliding = true
                initialSlidingX = event.x
                initialSliderPosition = sliderPosition
            }
        } else if (event.action == 1 || event.action == 4) {
            if (sliderPosition < measuredWidth - thumbWidth) {
                sliding = false
                sliderPosition = 0
                reset()
            } else if (listener != null) {
                listener!!.onUnlock()
            }
        } else if (event.action == 2 && sliding) {
            sliderPosition = (initialSliderPosition.toFloat() + (event.x - initialSlidingX)).toInt()
            if (sliderPosition <= 0) {
                sliderPosition = 0
            }
            if (sliderPosition >= measuredWidth - thumbWidth) {
                sliderPosition = measuredWidth - thumbWidth
            } else {
                val progress =
                    ((sliderPosition * 100).toFloat() / ((measuredWidth - thumbWidth).toFloat() * 1f)).toInt()
                textLabel!!.alpha = 1f - progress.toFloat() * 0.02f
                if (revealListener != null) {
                    revealListener!!.onReveal(1f - progress.toFloat() * 0.01f)
                }
            }
            setMarginLeft(sliderPosition)
        }
        return true
    }

    private fun setMarginLeft(margin: Int) {
        if (imgThumb != null) {
            val params = imgThumb!!.layoutParams as LayoutParams
            params.setMargins(margin, 0, 0, Toast.LENGTH_SHORT)
            imgThumb!!.layoutParams = params
        }
    }

    private fun dpToPx(dp: Int): Int {
        return Math.round(dp.toFloat() * resources.displayMetrics.density)
    }
}
