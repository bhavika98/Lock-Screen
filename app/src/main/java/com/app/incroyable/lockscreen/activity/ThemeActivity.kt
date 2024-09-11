package com.app.incroyable.lockscreen.activity

import android.content.Intent
import android.view.View
import androidx.recyclerview.widget.StaggeredGridLayoutManager
import com.app.incroyable.lockscreen.adapter.ThemeAdapter
import com.app.incroyable.lockscreen.base.BaseActivity
import com.app.incroyable.lockscreen.base.BaseBindingActivity
import com.app.incroyable.lockscreen.databinding.ActivityThemeBinding
import com.app.incroyable.lockscreen.model.ThemeData
import com.app.incroyable.lockscreen.storage.getDefaultPreferences
import com.app.incroyable.lockscreen.storage.prefTheme
import com.app.incroyable.lockscreen.storage.themeStorage

class ThemeActivity : BaseBindingActivity<ActivityThemeBinding>() {

    private var themeList = ArrayList<ThemeData>()

    override fun setBinding(): ActivityThemeBinding {
        return ActivityThemeBinding.inflate(layoutInflater)
    }

    override fun getActivityContext(): BaseActivity {
        return this@ThemeActivity
    }

    override fun initView() {
        super.initView()
        with(mBinding) {
            themeRecyclerView.layoutManager =
                StaggeredGridLayoutManager(3, StaggeredGridLayoutManager.VERTICAL)

            themeList.clear()
            for (i in 3 until themeStorage().size) {
                themeList.add(themeStorage()[i])
            }
            themeRecyclerView.adapter = ThemeAdapter(
                this@ThemeActivity,
                getDefaultPreferences(prefTheme) as Int,
                themeList
            )
        }
    }

    override fun initViewListener() {
        super.initViewListener()

        setClickListener(
            mBinding.clickBack, mBinding.clickPreview
        )
    }

    override fun onClick(v: View) {
        super.onClick(v)
        when (v) {
            mBinding.clickBack -> {
                finish()
            }

            mBinding.clickPreview -> {
                startActivity(Intent(this, PreviewActivity::class.java))
            }
        }
    }
}