package com.app.incroyable.lockscreen.activity

import android.app.Activity
import android.content.Intent
import android.graphics.Bitmap
import android.net.Uri
import android.provider.MediaStore
import android.view.View
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.viewpager2.adapter.FragmentStateAdapter
import androidx.viewpager2.widget.ViewPager2
import com.google.android.material.tabs.TabLayoutMediator
import com.yalantis.ucrop.UCrop
import com.app.incroyable.lockscreen.R
import com.app.incroyable.lockscreen.base.BaseActivity
import com.app.incroyable.lockscreen.base.BaseBindingActivity
import com.app.incroyable.lockscreen.databinding.ActivityWallpaperBinding
import com.app.incroyable.lockscreen.fragment.WallpaperFragment
import com.app.incroyable.lockscreen.storage.folderTemp
import com.app.incroyable.lockscreen.storage.getImagePath
import com.app.incroyable.lockscreen.storage.prefWallpaper
import com.app.incroyable.lockscreen.storage.setPreferences
import com.app.incroyable.lockscreen.utilities.*
import java.io.File
import java.text.SimpleDateFormat
import java.util.*

open class WallpaperActivity : BaseBindingActivity<ActivityWallpaperBinding>() {

    private lateinit var tabPagerAdapter: TabPagerAdapter

    override fun setBinding(): ActivityWallpaperBinding {
        return ActivityWallpaperBinding.inflate(layoutInflater)
    }

    override fun getActivityContext(): BaseActivity {
        return this@WallpaperActivity
    }

    override fun initView() {
        super.initView()
        with(mBinding) {
            tabPagerAdapter = TabPagerAdapter(this@WallpaperActivity)
            viewPager.adapter = tabPagerAdapter
            TabLayoutMediator(tabLayout, viewPager) { tab, position ->
                tab.text = tabTitle[position]
            }.attach()
            viewPager.registerOnPageChangeCallback(object : ViewPager2.OnPageChangeCallback() {
                override fun onPageScrolled(
                    position: Int,
                    positionOffset: Float,
                    positionOffsetPixels: Int
                ) {
                }

                override fun onPageSelected(position: Int) {
                    if (position == 0) {
                        mBinding.clickAddWallpaper.visible
                    } else mBinding.clickAddWallpaper.gone
                }

                override fun onPageScrollStateChanged(state: Int) {
                }
            })
        }
    }

    override fun initViewListener() {
        super.initViewListener()
        setClickListener(
            mBinding.clickBack, mBinding.clickAddWallpaper
        )
    }

    override fun onClick(v: View) {
        super.onClick(v)
        when (v) {
            mBinding.clickBack -> {
                finish()
            }
            mBinding.clickAddWallpaper -> {
                PermissionUtils.checkStoragePermission(this) {
                    openGallery()
                }
            }
        }
    }

    private fun openGallery() {
        val intent = Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI)
        startActivityForResult(intent, requestCodeImage)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, intent: Intent?) {
        super.onActivityResult(requestCode, resultCode, intent)
        if (resultCode == Activity.RESULT_OK) {
            when (requestCode) {
                requestCodeSetting -> {
                    openGallery()
                }
                requestCodeImage -> {
                    try {
                        imgReplace = false
                        val uri: Uri? = intent?.data
                        val options = UCrop.Options()
                        options.setCompressionFormat(Bitmap.CompressFormat.JPEG)
                        val destinationFile: String =
                            folderTemp() + nameFormat() + wallpaperExtension
                        UCrop.of(uri!!, Uri.fromFile(File(destinationFile)))
                            .withAspectRatio(9f, 16f)
                            .withMaxResultSize(1440, 2560)
                            .withOptions(options)
                            .start(this)
                    } catch (e: Exception) {
                        toastMsg(getString(R.string.pick_another_image))
                    }
                }
                UCrop.REQUEST_CROP -> {
                    mBinding.viewPager.currentItem = 0
                    val resultUri = UCrop.getOutput(intent!!)
                    val path: String = getImagePath(resultUri)!!
                    setPreferences(prefWallpaper, path)
                    tabPagerAdapter.getFragment(0).newImage(path)
                }
            }
        }
    }

    private fun nameFormat(): String {
        return SimpleDateFormat("yyyyMMdd_HH_mm_ss").format(Date())
    }

    class TabPagerAdapter(fragmentActivity: FragmentActivity) :
        FragmentStateAdapter(fragmentActivity) {

        private val fragments = ArrayList<WallpaperFragment>()

        override fun getItemCount(): Int {
            return tabTitle.size
        }

        override fun createFragment(position: Int): Fragment {
            val fragment = WallpaperFragment.newInstance(position)
            fragments.add(fragment)
            return fragment
        }

        fun getFragment(position: Int): WallpaperFragment {
            return fragments[position]
        }
    }
}