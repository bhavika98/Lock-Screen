package com.app.incroyable.lockscreen.activity

import android.content.Intent
import android.view.View
import com.app.incroyable.lockscreen.R
import com.app.incroyable.lockscreen.base.BaseActivity
import com.app.incroyable.lockscreen.base.BaseBindingActivity
import com.app.incroyable.lockscreen.databinding.ActivityStartBinding
import com.app.incroyable.lockscreen.utilities.commonDialog
import com.app.incroyable.lockscreen.utilities.firstCharColor
import com.app.incroyable.lockscreen.utilities.moreApp
import com.app.incroyable.lockscreen.utilities.policyLink
import com.app.incroyable.lockscreen.utilities.redirectApp
import com.app.incroyable.lockscreen.utilities.shareApp
import com.app.incroyable.lockscreen.utilities.singleClick

class StartActivity : BaseBindingActivity<ActivityStartBinding>() {

    override fun setBinding(): ActivityStartBinding {
        return ActivityStartBinding.inflate(layoutInflater)
    }

    override fun getActivityContext(): BaseActivity {
        return this@StartActivity
    }

    override fun initView() {
        super.initView()
        setMainTitle()
    }

    private fun setMainTitle() {
        with(mBinding) {
            txtTitle.text = firstCharColor(txtTitle.text.toString())
        }
    }

    override fun initViewListener() {
        super.initViewListener()

        setClickListener(
            mBinding.clickStart,
            mBinding.clickShare,
            mBinding.clickRate,
            mBinding.clickPolicy,
            mBinding.clickMore
        )
    }

    override fun onClick(v: View) {
        super.onClick(v)
        when (v) {
            mBinding.clickStart -> {
                startActivity(Intent(this@StartActivity, MainActivity::class.java))
            }

            mBinding.clickShare -> {
                singleClick {
                    shareApp()
                }
            }

            mBinding.clickRate -> {
                redirectApp(packageName)
            }

            mBinding.clickPolicy -> {
                policyLink()
            }

            mBinding.clickMore -> {
                moreApp()
            }
        }
    }

    override fun onBackPressed() {
        commonDialog(layoutResId = R.layout.dialog_exit,
            cancelable = true,
            title = getString(R.string.exit),
            message = getString(R.string.want_to_exit),
            positiveClickListener = { dialog, _ ->
                dialog.dismiss()
                finishAffinity()
            },
            negativeClickListener = {
            })
    }
}