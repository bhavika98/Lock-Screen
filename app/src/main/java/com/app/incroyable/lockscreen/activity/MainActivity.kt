package com.app.incroyable.lockscreen.activity

import android.content.Intent
import android.os.Build
import android.provider.Settings
import android.view.View
import androidx.recyclerview.widget.StaggeredGridLayoutManager
import com.app.incroyable.lockscreen.R
import com.app.incroyable.lockscreen.adapter.ThemeAdapter
import com.app.incroyable.lockscreen.adapter.WallpaperAdapter
import com.app.incroyable.lockscreen.base.BaseActivity
import com.app.incroyable.lockscreen.base.BaseBindingActivity
import com.app.incroyable.lockscreen.databinding.ActivityMainBinding
import com.app.incroyable.lockscreen.model.ThemeData
import com.app.incroyable.lockscreen.receiver.BootCompleteReceiver
import com.app.incroyable.lockscreen.service.LockService
import com.app.incroyable.lockscreen.service.RunService
import com.app.incroyable.lockscreen.storage.getDefaultPreferences
import com.app.incroyable.lockscreen.storage.prefLockStatus
import com.app.incroyable.lockscreen.storage.prefTheme
import com.app.incroyable.lockscreen.storage.prefWallpaper
import com.app.incroyable.lockscreen.storage.setPreferences
import com.app.incroyable.lockscreen.storage.themeStorage
import com.app.incroyable.lockscreen.utilities.PermissionUtils
import com.app.incroyable.lockscreen.utilities.firstCharColor
import com.app.incroyable.lockscreen.utilities.networkError
import com.app.incroyable.lockscreen.utilities.rateDialog
import com.app.incroyable.lockscreen.utilities.wallpaperName

class MainActivity : BaseBindingActivity<ActivityMainBinding>() {

    private var themeList = ArrayList<ThemeData>()
    private var wallpaperList = ArrayList<String>()

    private var lockStatusKey = false

    override fun setBinding(): ActivityMainBinding {
        return ActivityMainBinding.inflate(layoutInflater)
    }

    override fun getActivityContext(): BaseActivity {
        return this@MainActivity
    }

    override fun initView() {
        super.initView()
        rateDialog()
        setMainTitle()
        with(mBinding) {
            themeRecyclerView.layoutManager =
                StaggeredGridLayoutManager(3, StaggeredGridLayoutManager.VERTICAL)
            wallpaperRecyclerView.layoutManager =
                StaggeredGridLayoutManager(3, StaggeredGridLayoutManager.VERTICAL)

//            Theme Data
            themeList.clear()
            for (i in 0..2) {
                themeList.add(themeStorage()[i])
            }
            themeRecyclerView.adapter = ThemeAdapter(
                this@MainActivity,
                getDefaultPreferences(prefTheme) as Int,
                themeList
            )

//            Wallpaper Data
            wallpaperList.clear()
            for (i in 1..3)
                wallpaperList.add("$wallpaperName$i")
            wallpaperRecyclerView.adapter = WallpaperAdapter(
                this@MainActivity,
                getDefaultPreferences(prefWallpaper) as String,
                wallpaperList
            )

            lockStatusKey = getDefaultPreferences(prefLockStatus) as Boolean
            if (lockStatusKey) {
                lockStatus.text = getString(R.string.enabled)
                switchLockStatus.setImageResource(R.drawable.switch_enable)
            } else {
                lockStatus.text = getString(R.string.disabled)
                switchLockStatus.setImageResource(R.drawable.switch_disable)
            }
        }
    }

    private fun setMainTitle() {
        with(mBinding) {
            txtTitle.text = firstCharColor(txtTitle.text.toString())
        }
    }

    override fun initViewListener() {
        super.initViewListener()

        setClickListener(
            mBinding.clickSetting,
            mBinding.previewLayout,
            mBinding.setupLayout,
            mBinding.btnThemeAll,
            mBinding.btnWallpaperAll,
            mBinding.switchLockStatus
        )
    }

    override fun onClick(v: View) {
        super.onClick(v)
        when (v) {
            mBinding.clickSetting -> {
                nextActivity(0)
            }

            mBinding.previewLayout -> {
                nextActivity(2)
            }

            mBinding.setupLayout -> {
                nextActivity(1)
            }

            mBinding.btnThemeAll -> {
                nextActivity(3)
            }

            mBinding.btnWallpaperAll -> {
                networkError(
                    onNetworkAvailable = {
                        nextActivity(4)
                    },
                    negativeClickListener = {

                    }
                )
            }

            mBinding.switchLockStatus -> {
                lockStatusKey = getDefaultPreferences(prefLockStatus) as Boolean
                if (!lockStatusKey) {
                    PermissionUtils.checkPhonePermission(this) {
                        continueTask()
                    }
                } else
                    lockServiceStop()
            }
        }
    }

    private fun continueTask() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M && !Settings.canDrawOverlays(this)) {
            PermissionUtils.checkOverlayPermission(this) {
                lockServiceStart()
            }
        } else {
            lockServiceStart()
        }
    }

    private fun lockServiceStop() {
        lockStatusKey = false
        setPreferences(prefLockStatus, lockStatusKey)
        mBinding.lockStatus.text = getString(R.string.disabled)
        mBinding.switchLockStatus.setImageResource(R.drawable.switch_disable)
        stopService(Intent(applicationContext, LockService::class.java))
    }

    private fun lockServiceStart() {
        lockStatusKey = true
        setPreferences(prefLockStatus, lockStatusKey)
        mBinding.lockStatus.text = getString(R.string.enabled)
        mBinding.switchLockStatus.setImageResource(R.drawable.switch_enable)
        startService(Intent(applicationContext, LockService::class.java))
        bootReceiver()
    }

    private fun bootReceiver() {
        val intent: Intent = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M &&
            !RunService.isServiceRunning(this, LockService::class.java)
        ) {
            Intent(this, BootCompleteReceiver::class.java)
        } else if (!RunService.isServiceRunning(this, LockService::class.java)) {
            Intent(this, BootCompleteReceiver::class.java)
        } else {
            return
        }
        sendBroadcast(intent)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        PermissionUtils.handleOverlayPermissionResult(this, requestCode, resultCode)
    }

    private fun nextActivity(position: Int) {
        var intent: Intent? = null
        when (position) {
            0 -> intent = Intent(this, SettingActivity::class.java)
            1 -> intent = Intent(this, SetLockActivity::class.java)
            2 -> intent = Intent(this, PreviewActivity::class.java)
            3 -> intent = Intent(this, ThemeActivity::class.java)
            4 -> intent = Intent(this, WallpaperActivity::class.java)
        }
        startActivity(intent)
    }

    override fun onResume() {
        super.onResume()
        with(mBinding) {
            (themeRecyclerView.adapter as ThemeAdapter).dataChanged()
            (wallpaperRecyclerView.adapter as WallpaperAdapter).dataChanged()
        }
    }
}