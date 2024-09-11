package com.app.incroyable.lockscreen.activity

import android.content.Intent
import android.view.View
import com.app.incroyable.lockscreen.R
import com.app.incroyable.lockscreen.base.BaseActivity
import com.app.incroyable.lockscreen.base.BaseBindingActivity
import com.app.incroyable.lockscreen.databinding.ActivitySetlockBinding
import com.app.incroyable.lockscreen.storage.getDefaultPreferences
import com.app.incroyable.lockscreen.storage.prefPatternEnable
import com.app.incroyable.lockscreen.storage.prefPatternPassword
import com.app.incroyable.lockscreen.storage.prefPinEnable
import com.app.incroyable.lockscreen.storage.prefPinPassword
import com.app.incroyable.lockscreen.storage.setPreferences
import com.app.incroyable.lockscreen.utilities.commonDialog
import com.app.incroyable.lockscreen.utilities.toastMsg

class SetLockActivity : BaseBindingActivity<ActivitySetlockBinding>() {

    override fun setBinding(): ActivitySetlockBinding {
        return ActivitySetlockBinding.inflate(layoutInflater)
    }

    override fun getActivityContext(): BaseActivity {
        return this@SetLockActivity
    }

    override fun initView() {
        super.initView()
    }

    override fun initViewListener() {
        super.initViewListener()

        setClickListener(
            mBinding.clickBack, mBinding.clickNone, mBinding.clickPin, mBinding.clickPattern
        )
    }

    override fun onClick(v: View) {
        super.onClick(v)
        when (v) {
            mBinding.clickBack -> {
                finish()
            }

            mBinding.clickNone -> {
                if (getDefaultPreferences(prefPinEnable) as Boolean ||
                    getDefaultPreferences(prefPatternEnable) as Boolean
                )
                    passwordNone()
                else
                    toastMsg(getString(R.string.password_not_set))
            }

            mBinding.clickPin -> {
                nextActivity(1)
            }

            mBinding.clickPattern -> {
                nextActivity(2)
            }
        }
    }

    private fun initLockViews() {
        val pinData = getDefaultPreferences(prefPinEnable) as Boolean
        val patternData = getDefaultPreferences(prefPatternEnable) as Boolean

        if (pinData)
            setResource(mBinding.imgPin, true)
        else
            setResource(mBinding.imgPin, false)

        if (patternData)
            setResource(mBinding.imgPattern, true)
        else
            setResource(mBinding.imgPattern, false)

        if (!pinData && !patternData)
            setResource(mBinding.imgNone, true)
        else
            setResource(mBinding.imgNone, false)
    }

    private fun setResource(view: View, value: Boolean) {
        if (value)
            view.setBackgroundResource(R.drawable.stroke_icon_selected)
        else
            view.setBackgroundResource(R.drawable.stroke_icon)
    }

    private fun passwordNone() {
        commonDialog(layoutResId = R.layout.dialog_common,
            cancelable = true,
            title = getString(R.string.remove_all_lock),
            message = "",
            positiveClickListener = { dialog, _ ->
                setPreferences(prefPinEnable, false)
                setPreferences(prefPatternEnable, false)
                setPreferences(prefPinPassword, "")
                setPreferences(prefPatternPassword, "")
                mBinding.imgNone.setBackgroundResource(R.drawable.stroke_icon_selected)
                mBinding.imgPin.setBackgroundResource(R.drawable.stroke_icon)
                mBinding.imgPattern.setBackgroundResource(R.drawable.stroke_icon)
                toastMsg(getString(R.string.all_lock_removed))
                dialog.dismiss()
            },
            negativeClickListener = {})
    }

    private fun nextActivity(position: Int) {
        var intent: Intent? = null
        when (position) {
            1 -> intent = Intent(this, PinActivity::class.java)
            2 -> intent = Intent(this, PatternActivity::class.java)
        }
        startActivity(intent)
    }

    override fun onResume() {
        super.onResume()
        initLockViews()
    }
}