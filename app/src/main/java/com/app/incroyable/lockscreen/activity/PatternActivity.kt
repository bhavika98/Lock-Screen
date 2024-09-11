package com.app.incroyable.lockscreen.activity

import android.content.Intent
import android.view.View
import com.app.incroyable.lockscreen.R
import com.app.incroyable.lockscreen.base.BaseActivity
import com.app.incroyable.lockscreen.base.BaseBindingActivity
import com.app.incroyable.lockscreen.databinding.ActivityPatternBinding
import com.app.incroyable.lockscreen.enums.SecurityType
import com.app.incroyable.lockscreen.lock.pattern.PatternLockView
import com.app.incroyable.lockscreen.storage.getDefaultPreferences
import com.app.incroyable.lockscreen.storage.getPatternString
import com.app.incroyable.lockscreen.storage.prefLockSet
import com.app.incroyable.lockscreen.storage.prefPatternEnable
import com.app.incroyable.lockscreen.storage.prefPatternPassword
import com.app.incroyable.lockscreen.storage.setPreferences
import com.app.incroyable.lockscreen.utilities.commonDialog
import com.app.incroyable.lockscreen.utilities.gone
import com.app.incroyable.lockscreen.utilities.invisible
import com.app.incroyable.lockscreen.utilities.toastMsg
import com.app.incroyable.lockscreen.utilities.visible

class PatternActivity : BaseBindingActivity<ActivityPatternBinding>() {

    private var mIsPasswordSet = false
    private var mPassword: String? = null

    private var patternSet = false
    private var isWrong = false

    override fun initView() {
        super.initView()
        patternSet = getDefaultPreferences(prefPatternEnable) as Boolean
        with(mBinding) {
            patternView.setOnPatternListener(listener)
            if (patternSet) {
                patternRemove.invisible
                patternRemove.setOnClickListener {
                    removePattern()
                }
                mIsPasswordSet = false
                patternMsg.text = getString(R.string.draw_old_pattern)
            } else {
                patternRemove.gone
                patternMsg.text = getString(R.string.draw_new_pattern)
            }
        }
//        mBinding.patternLock.enableSecureMode()
    }

    private fun removePattern() {
        commonDialog(layoutResId = R.layout.dialog_common,
            cancelable = true,
            title = getString(R.string.pattern_sure_to_remove),
            message = "",
            positiveClickListener = { dialog, _ ->
                setPreferences(prefPatternPassword, "")
                setPreferences(prefPatternEnable, false)
                dialog.dismiss()
                finish()
                toastMsg(getString(R.string.pattern_removed_successfully))
            },
            negativeClickListener = {
            })
    }

    override fun setBinding(): ActivityPatternBinding {
        return ActivityPatternBinding.inflate(layoutInflater)
    }

    override fun getActivityContext(): BaseActivity {
        return this@PatternActivity
    }

    override fun initViewListener() {
        super.initViewListener()

        setClickListener(
            mBinding.clickBack
        )
    }

    override fun onClick(v: View) {
        super.onClick(v)
        when (v) {
            mBinding.clickBack -> {
                finish()
            }
        }
    }

    private var listener = object : PatternLockView.OnPatternListener {

        override fun onStarted() {
            super.onStarted()
        }

        override fun onProgress(ids: ArrayList<Int>) {
            super.onProgress(ids)
        }

        override fun onComplete(ids: ArrayList<Int>): Boolean {
            val resultAsString = getPatternString(ids)
            if (!patternSet) {
                newPattern(resultAsString)
            } else {
                val oldPass = getDefaultPreferences(prefPatternPassword) as String
                if (resultAsString == oldPass) {
                    mBinding.patternRemove.visible
                    patternSet = false
                    mIsPasswordSet = false
                    mPassword = ""
                    mBinding.patternMsg.text = getString(R.string.draw_new_pattern)
                    isWrong = false
                } else {
                    mBinding.patternMsg.text = getString(R.string.wrong_pattern_try_again)
                    isWrong = true
                }
            }
            return !isWrong
        }
    }

    private fun newPattern(resultAsString: String) {
        if (mIsPasswordSet) {
            val password: String = resultAsString
            if (password == mPassword) {
                setPreferences(prefPatternPassword, mPassword.toString())
                if (!(getDefaultPreferences(prefLockSet) as Boolean)) {
                    val intent = Intent(this, QuestionActivity::class.java)
                    intent.putExtra("securityType", SecurityType.PATTERN.toString())
                    intent.putExtra("setPassword", true)
                    startActivity(intent)
                } else {
                    setPreferences(prefPatternEnable, true)
                    toastMsg(getString(R.string.pattern_set_successfully))
                }
                isWrong = false
                finish()
            } else {
                mBinding.patternMsg.text = getString(R.string.wrong_pattern_try_again)
                isWrong = true
            }
        } else {
            if (resultAsString.length < 4) {
                mBinding.patternMsg.text = getString(R.string.connect_at_least_4_dots_try_again)
                isWrong = true
            } else {
                mIsPasswordSet = true
                mPassword = resultAsString
                mBinding.patternMsg.text = getString(R.string.draw_pattern_again_to_confirm)
                isWrong = false
            }
        }
    }
}