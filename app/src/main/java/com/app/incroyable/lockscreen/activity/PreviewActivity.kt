package com.app.incroyable.lockscreen.activity

import android.app.Activity
import android.graphics.Bitmap
import android.os.Handler
import android.view.View
import android.widget.RelativeLayout
import android.widget.TextView
import androidx.viewbinding.ViewBinding
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.app.incroyable.lockscreen.R
import com.app.incroyable.lockscreen.base.BaseActivity
import com.app.incroyable.lockscreen.base.BaseBindingActivity
import com.app.incroyable.lockscreen.databinding.*
import com.app.incroyable.lockscreen.enums.UnlockBar
import com.app.incroyable.lockscreen.model.ThemeData
import com.app.incroyable.lockscreen.storage.*
import com.app.incroyable.lockscreen.utilities.*
import com.app.incroyable.lockscreen.widgets.SlideToActView
import java.util.*

class PreviewActivity : BaseBindingActivity<ActivityPreviewBinding>() {

    lateinit var mThemeData: ThemeData
    lateinit var txtTimeMeridian: TextView
    lateinit var txtTime: TextView
    lateinit var txtDay: TextView
    lateinit var txtDate: TextView
    lateinit var viewBackground: RelativeLayout

    override fun setBinding(): ActivityPreviewBinding {
        return ActivityPreviewBinding.inflate(layoutInflater)
    }

    override fun getActivityContext(): BaseActivity {
        return this@PreviewActivity
    }

    override fun initView() {
        super.initView()
        setLockView()
        setPreviewImage()
    }

    private fun setLockView() {
        with(mBinding) {
            val index: Int = getDefaultPreferences(prefTheme) as Int
            mThemeData = themeStorage()[index]

            setLockBar()

            val layoutView = layoutInflater.inflate(mThemeData.layout, lockView, false)
            lockView.addView(layoutView)

            val viewBindingClass = when (mThemeData.layout) {
                R.layout.layout_lockview_1 -> LayoutLockview1Binding::class
                R.layout.layout_lockview_2 -> LayoutLockview2Binding::class
                R.layout.layout_lockview_3 -> LayoutLockview3Binding::class
                R.layout.layout_lockview_4 -> LayoutLockview4Binding::class
                R.layout.layout_lockview_5 -> LayoutLockview5Binding::class
                R.layout.layout_lockview_6 -> LayoutLockview6Binding::class
                R.layout.layout_lockview_7 -> LayoutLockview7Binding::class
                R.layout.layout_lockview_8 -> LayoutLockview8Binding::class
                R.layout.layout_lockview_9 -> LayoutLockview9Binding::class
                R.layout.layout_lockview_10 -> LayoutLockview10Binding::class
                R.layout.layout_lockview_11 -> LayoutLockview11Binding::class
                R.layout.layout_lockview_12 -> LayoutLockview12Binding::class
                R.layout.layout_lockview_13 -> LayoutLockview13Binding::class
                R.layout.layout_lockview_14 -> LayoutLockview14Binding::class
                R.layout.layout_lockview_15 -> LayoutLockview15Binding::class
                else -> throw IllegalArgumentException("Invalid layout resource ID: ${mThemeData.layout}")
            }

            val binding = viewBindingClass.java.getMethod("bind", View::class.java)
                .invoke(null, layoutView) as ViewBinding

            txtTimeMeridian = binding.root.findViewById<TextView>(R.id.txt_time_meridian)
            txtTime = binding.root.findViewById<TextView>(R.id.txt_time)
            txtDay = binding.root.findViewById<TextView>(R.id.txt_day)
            txtDate = binding.root.findViewById<TextView>(R.id.txt_date)
            viewBackground = binding.root.findViewById<RelativeLayout>(R.id.view_background)
            viewBackground.gone

            val timer = Timer()
            val t = object : TimerTask() {
                override fun run() {
                    runOnUiThread {
                        val hrsFormat = getDefaultPreferences(prefTimeFormat) as Boolean
                        if (mThemeData.position == 8) {
                            txtTimeMeridian.visible
                            txtTimeMeridian.text = timeFormat("mm")
                            if (hrsFormat) {
                                txtTime.text = timeFormat("hh")
                            } else {
                                txtTime.text = timeFormat("HH")
                            }
                        } else {
                            if (hrsFormat) {
                                if (mThemeData.position == 12)
                                    txtTimeMeridian.gone
                                else {
                                    txtTimeMeridian.visible
                                    txtTimeMeridian.text = timeFormat("a")
                                }
                                txtTime.text = timeFormat("hh:mm")
                            } else {
                                if (mThemeData.position == 1)
                                    txtTimeMeridian.invisible
                                else
                                    txtTimeMeridian.gone
                                txtTime.text = timeFormat("HH:mm")
                            }
                        }

                        txtDay.text = timeFormat(mThemeData.dayFormat)
                        txtDate.text = timeFormat(mThemeData.dateFormat)
                        viewBackground.visible
                    }
                }
            }
            timer.scheduleAtFixedRate(t, 500, 500)
        }
    }

    internal class UnlockListener(private val activity: Activity) :
        SlideToActView.OnUnlockListener {
        override fun onUnlock() {
            Handler().postDelayed({ activity.finish() }, 100)
        }
    }

    private fun setLockBar() {
        with(mBinding) {
            val layoutParams = RelativeLayout.LayoutParams(
                RelativeLayout.LayoutParams.MATCH_PARENT,
                RelativeLayout.LayoutParams.MATCH_PARENT
            )
            sliderLayout.layoutParams = layoutParams

            val slideToUnlockParams = RelativeLayout.LayoutParams(
                RelativeLayout.LayoutParams.MATCH_PARENT,
                RelativeLayout.LayoutParams.WRAP_CONTENT
            )

            when (mThemeData.unlockBar) {
                UnlockBar.TOP -> {
                    slideToUnlockParams.addRule(RelativeLayout.ALIGN_PARENT_TOP)
                    slideToUnlockParams.topMargin =
                        resources.getDimensionPixelSize(R.dimen.top_margin)
                }

                UnlockBar.CENTER -> {
                    slideToUnlockParams.addRule(RelativeLayout.CENTER_IN_PARENT)
                }

                UnlockBar.BOTTOM -> {
                    slideToUnlockParams.addRule(RelativeLayout.ALIGN_PARENT_BOTTOM)
                    slideToUnlockParams.bottomMargin =
                        resources.getDimensionPixelSize(R.dimen.bottom_margin)
                }

                UnlockBar.BOTTOM_UP -> {
                    slideToUnlockParams.addRule(RelativeLayout.ALIGN_PARENT_BOTTOM)
                    slideToUnlockParams.bottomMargin =
                        resources.getDimensionPixelSize(R.dimen.bottom_up_margin)
                }
            }
            slideToUnlock.layoutParams = slideToUnlockParams
            slideToUnlock.gravity = android.view.Gravity.CENTER
            slideToUnlock.setOnUnlockListener(UnlockListener(this@PreviewActivity))
        }
    }

    private fun setPreviewImage() {
        with(mBinding) {
            val image = getDefaultPreferences(prefWallpaper) as String
            var compareIt = wallpaperName
            compareIt = compareIt.replace("_", "")
            when {
                image in listOf("${wallpaperName}1", "${wallpaperName}2", "${wallpaperName}3") -> {
                    val bitmap: Bitmap =
                        loadBitmapFromAssets("$wallpaperFolder/$image$wallpaperExtension")!!
                    imagePreview.setImageBitmap(bitmap)
                }

                image.contains(customWallpaperName) -> {
                    val bitmap: Bitmap =
                        loadBitmapFromAssets("$wallpaperFolder/$image$wallpaperExtension")!!
                    imagePreview.setImageBitmap(bitmap)
                }

                image.contains(mainFolder) -> {
                    val bitmap: Bitmap =
                        loadBitmapFromAssets("$image$wallpaperExtension")!!
                    imagePreview.setImageBitmap(bitmap)
                }

                image.contains(compareIt) -> {
                    val sharePath = folderPath() + "$image$wallpaperExtension"
                    loadImageWithGlide(sharePath)
                }

                else -> {
                    loadImageWithGlide(image)
                }
            }
        }
    }

    private fun loadImageWithGlide(imagePath: String) {
        with(mBinding) {
            Glide.with(this@PreviewActivity)
                .load(imagePath)
                .apply(RequestOptions().centerCrop())
                .into(imagePreview)
        }
    }
}