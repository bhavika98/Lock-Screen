package com.app.incroyable.lockscreen.activity

import android.content.Intent
import android.view.View
import com.app.incroyable.lockscreen.R
import com.app.incroyable.lockscreen.base.BaseActivity
import com.app.incroyable.lockscreen.base.BaseBindingActivity
import com.app.incroyable.lockscreen.databinding.ActivityPinBinding
import com.app.incroyable.lockscreen.enums.SecurityType
import com.app.incroyable.lockscreen.lock.pin.listener.PinEntryAuthenticationListener
import com.app.incroyable.lockscreen.lock.pin.listener.PinEntrySetupListener
import com.app.incroyable.lockscreen.storage.getDefaultPreferences
import com.app.incroyable.lockscreen.storage.prefLockSet
import com.app.incroyable.lockscreen.storage.prefPinEnable
import com.app.incroyable.lockscreen.storage.prefPinPassword
import com.app.incroyable.lockscreen.storage.setPreferences
import com.app.incroyable.lockscreen.utilities.commonDialog
import com.app.incroyable.lockscreen.utilities.gone
import com.app.incroyable.lockscreen.utilities.invisible
import com.app.incroyable.lockscreen.utilities.toastMsg
import com.app.incroyable.lockscreen.utilities.visible

class PinActivity : BaseBindingActivity<ActivityPinBinding>(), PinEntrySetupListener,
    PinEntryAuthenticationListener {

    private var pinSet = false

    override fun setBinding(): ActivityPinBinding {
        return ActivityPinBinding.inflate(layoutInflater)
    }

    override fun getActivityContext(): BaseActivity {
        return this@PinActivity
    }

    override fun initView() {
        super.initView()
        pinSet = getDefaultPreferences(prefPinEnable) as Boolean
        with(mBinding) {
            if (pinSet) {
                pinRemove.invisible
                pinView.setMessage(resources.getString(R.string.pin_enter_old))
                pinView.setModeAuthenticate(this@PinActivity)
                pinRemove.setOnClickListener {
                    removePin()
                }
            } else {
                pinRemove.gone
                pinView.setModeSetup(this@PinActivity)
            }
        }
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

    private fun removePin() {
        commonDialog(layoutResId = R.layout.dialog_common,
            cancelable = true,
            title = getString(R.string.pin_sure_to_remove),
            message = "",
            positiveClickListener = { dialog, _ ->
                setPreferences(prefPinPassword, "")
                setPreferences(prefPinEnable, false)
                dialog.dismiss()
                finish()
                toastMsg(getString(R.string.pin_removed_successfully))
            },
            negativeClickListener = {
            })
    }

    override fun onPinEntered(pin: String?) {
    }

    override fun onPinConfirmed(pin: String?) {
    }

    override fun onPinMismatch() {
    }

    override fun onPinSet(pin: String?) {
        if (pinSet) {
            toastMsg(getString(R.string.pin_changed_successfully))
            finish()
        } else {
            if (!(getDefaultPreferences(prefLockSet) as Boolean)) {
                val intent = Intent(this, QuestionActivity::class.java)
                intent.putExtra("securityType", SecurityType.PIN.toString())
                intent.putExtra("setPassword", true)
                startActivity(intent)
            } else {
                setPreferences(prefPinEnable, true)
                toastMsg(getString(R.string.pin_set_successfully))
            }
            finish()
        }
    }

    override fun onPinCorrect() {
        if (pinSet) {
            with(mBinding) {
                pinView.setModeSetup(this@PinActivity)
                pinView.clearPin()
                pinRemove.visible
            }
        }
    }

    override fun onPinWrong() {

    }
}