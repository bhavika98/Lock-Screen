package com.app.incroyable.lockscreen.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.viewpager.widget.PagerAdapter
import com.app.incroyable.lockscreen.activity.LockActivity
import com.app.incroyable.lockscreen.enums.AllLock
import com.app.incroyable.lockscreen.enums.PatternLock
import com.app.incroyable.lockscreen.enums.PinLock

class CustomPagerAdapter(
    private val mContext: Context,
    mainView: Int
) :
    PagerAdapter() {
    private var customEnum: AllLock? = null
    private var pinEnum: PinLock? = null
    private var patternEnum: PatternLock? = null
    private var layoutValue = 0
    var mainView = 0
    private lateinit var layout: ViewGroup

    init {
        this.mainView = mainView
    }

    override fun instantiateItem(collection: ViewGroup, position: Int): Any {
        val inflater = LayoutInflater.from(mContext)
        when (mainView) {
            1 -> {
                customEnum = AllLock.values()[position]
                layoutValue = customEnum!!.layoutResId
            }
            2 -> {
                pinEnum = PinLock.values()[position]
                layoutValue = pinEnum!!.layoutResId
            }
            3 -> {
                patternEnum = PatternLock.values()[position]
                layoutValue = patternEnum!!.layoutResId
            }
        }
        layout = inflater.inflate(layoutValue, collection, false) as ViewGroup
        collection.addView(layout)
        when (mainView) {
            1 -> setView(position)
            2 -> if (position == 0) setView(0) else if (position == 1) setView(1)
            3 -> if (position == 0) setView(1) else if (position == 1) setView(2)
        }
        return layout
    }

    private fun setView(value: Int) {
        when (value) {
            0 -> (mContext as LockActivity).screenOne(layout)
            1 -> (mContext as LockActivity).screenTwo(layout)
            2 -> (mContext as LockActivity).screenThree(layout)
        }
    }

    override fun destroyItem(collection: ViewGroup, position: Int, view: Any) {
        collection.removeView(view as View)
    }

    override fun getCount(): Int {
        var total = 0
        when (mainView) {
            1 -> total = AllLock.values().size
            2 -> total = PinLock.values().size
            3 -> total = PatternLock.values().size
        }
        return total
    }

    override fun isViewFromObject(view: View, `object`: Any): Boolean {
        return view === `object`
    }
}
