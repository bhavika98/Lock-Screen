package com.app.incroyable.lockscreen.activity

import android.content.Intent
import android.view.View
import android.widget.EditText
import com.app.incroyable.lockscreen.R
import com.app.incroyable.lockscreen.base.BaseActivity
import com.app.incroyable.lockscreen.base.BaseBindingActivity
import com.app.incroyable.lockscreen.databinding.ActivitySettingBinding
import com.app.incroyable.lockscreen.enums.SecurityType
import com.app.incroyable.lockscreen.storage.getDefaultPreferences
import com.app.incroyable.lockscreen.storage.getPrefQuestionData
import com.app.incroyable.lockscreen.storage.prefLockHint
import com.app.incroyable.lockscreen.storage.prefLockSet
import com.app.incroyable.lockscreen.storage.prefSavedAnswer
import com.app.incroyable.lockscreen.storage.prefSavedQuestion
import com.app.incroyable.lockscreen.storage.prefTimeFormat
import com.app.incroyable.lockscreen.storage.setPreferences
import com.app.incroyable.lockscreen.utilities.commonDialog
import com.app.incroyable.lockscreen.utilities.toastMsg

class SettingActivity : BaseBindingActivity<ActivitySettingBinding>() {

    override fun setBinding(): ActivitySettingBinding {
        return ActivitySettingBinding.inflate(layoutInflater)
    }

    override fun getActivityContext(): BaseActivity {
        return this@SettingActivity
    }

    override fun initView() {
        super.initView()
        val hrsFormat = getDefaultPreferences(prefTimeFormat) as Boolean
        timeFormat(hrsFormat)
        val hintFormat = getDefaultPreferences(prefLockHint) as Boolean
        lockHintFormat(hintFormat)
    }

    override fun initViewListener() {
        super.initViewListener()
        setClickListener(
            mBinding.clickBack,
            mBinding.previewLayout,
            mBinding.securityLayout,
            mBinding.switchTimeFormat,
            mBinding.switchLockHint
        )
    }

    override fun onClick(v: View) {
        super.onClick(v)
        when (v) {
            mBinding.clickBack -> {
                finish()
            }

            mBinding.previewLayout -> {
                nextActivity(2)
            }

            mBinding.securityLayout -> {
                val lockSet = getDefaultPreferences(prefLockSet) as Boolean
                if (!lockSet) {
                    nextActivity(3)
                } else
                    giveAnswer()
            }

            mBinding.switchTimeFormat -> {
                val hrsFormat = getDefaultPreferences(prefTimeFormat) as Boolean
                timeFormat(!hrsFormat)
                setPreferences(prefTimeFormat, !hrsFormat)
            }

            mBinding.switchLockHint -> {
                val hintFormat = getDefaultPreferences(prefLockHint) as Boolean
                lockHintFormat(!hintFormat)
                setPreferences(prefLockHint, !hintFormat)
            }
        }
    }

    private fun lockHintFormat(value: Boolean) {
        when (value) {
            true -> {
                mBinding.switchLockHint.setImageResource(R.drawable.switch_enable)
            }

            false -> {
                mBinding.switchLockHint.setImageResource(R.drawable.switch_disable)
            }
        }
    }

    private fun timeFormat(value: Boolean) {
        when (value) {
            true -> {
                mBinding.switchTimeFormat.setImageResource(R.drawable.switch_enable)
                mBinding.systemTimeFormat.text = getString(R.string.using_12hour_format)
            }

            false -> {
                mBinding.switchTimeFormat.setImageResource(R.drawable.switch_disable)
                mBinding.systemTimeFormat.text = getString(R.string.using_24hour_format)
            }
        }
    }

    private fun giveAnswer() {
        val tempList = getPrefQuestionData()
        val pos =
            tempList.indexOfFirst { it.position == getDefaultPreferences(prefSavedQuestion) as Int }
        commonDialog(layoutResId = R.layout.dialog_check_answer,
            cancelable = true,
            title = getString(R.string.verify_answer),
            message = tempList[pos].question,
            positiveClickListener = { dialog, view ->
                val dialogInput = view.findViewById<EditText>(R.id.dialog_input)
                if (dialogInput.text.toString().isNotEmpty()) {
                    if (dialogInput.text.toString() == getDefaultPreferences(prefSavedAnswer) as String) {
                        nextActivity(4)
                        dialog.dismiss()
                    } else {
                        dialogInput.text.clear()
                        toastMsg(getString(R.string.password_is_wrong))
                    }
                } else toastMsg(getString(R.string.please_write_something))
            },
            negativeClickListener = { _ ->
            })
    }

    private fun nextActivity(position: Int) {
        var intent: Intent? = null
        when (position) {
            2 -> intent = Intent(this, PreviewActivity::class.java)
            3 -> {
                intent = Intent(this, QuestionActivity::class.java)
                intent.putExtra("securityType", SecurityType.NONE.toString())
                intent.putExtra("setPassword", true)
            }

            4 -> {
                intent = Intent(this, QuestionActivity::class.java)
                intent.putExtra("setPassword", false)
            }
        }
        startActivity(intent)
    }
}