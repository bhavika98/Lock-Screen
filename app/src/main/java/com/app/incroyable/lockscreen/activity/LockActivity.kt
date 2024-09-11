package com.app.incroyable.lockscreen.activity

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.graphics.Bitmap
import android.graphics.PorterDuff
import android.graphics.drawable.Drawable
import android.os.Build
import android.os.Bundle
import android.os.Handler
import android.view.*
import android.view.inputmethod.InputMethodManager
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.viewpager.widget.ViewPager
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.romainpiel.shimmer.Shimmer
import com.romainpiel.shimmer.ShimmerTextView
import com.app.incroyable.lockscreen.R
import com.app.incroyable.lockscreen.adapter.CustomPagerAdapter
import com.app.incroyable.lockscreen.enums.UnlockBar
import com.app.incroyable.lockscreen.lock.pattern.ForgottenView
import com.app.incroyable.lockscreen.lock.pattern.PatternLockView
import com.app.incroyable.lockscreen.lock.pin.listener.PinEntryAuthenticationListener
import com.app.incroyable.lockscreen.lock.pin.view.PinView
import com.app.incroyable.lockscreen.model.QuestionData
import com.app.incroyable.lockscreen.model.ThemeData
import com.app.incroyable.lockscreen.storage.*
import com.app.incroyable.lockscreen.utilities.*
import com.app.incroyable.lockscreen.widgets.BlurView
import com.app.incroyable.lockscreen.widgets.SlideToActView
import com.app.incroyable.lockscreen.widgets.ViewPager3
import java.util.*

class LockActivity : AppCompatActivity(), PinEntryAuthenticationListener {

    lateinit var mThemeData: ThemeData

    private lateinit var sliderLayout: RelativeLayout
    private lateinit var slideToUnlock: SlideToActView
    private lateinit var viewPager: ViewPager3
    private lateinit var txtForgetPass: TextView

    private lateinit var windowLayoutParams: WindowManager.LayoutParams
    private lateinit var windowManager: WindowManager
    var rootView: RelativeLayout? = null
    private lateinit var contentView: View

    var mainView = 0
    private var pinEnable = false
    private var patternEnable = false
    var customPagerAdapter: CustomPagerAdapter? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        supportRequestWindowFeature(Window.FEATURE_NO_TITLE)
        window.setFlags(
            WindowManager.LayoutParams.FLAG_FULLSCREEN,
            WindowManager.LayoutParams.FLAG_FULLSCREEN
        )

        contentView = View.inflate(this, R.layout.activity_lock, null)

        windowLayoutParams = if (Build.VERSION.SDK_INT < 26) {
            WindowManager.LayoutParams(-1, -1, 2002, 262696, -3)
        } else {
            WindowManager.LayoutParams(-1, -1, 2038, 262696, -3)
        }
        windowLayoutParams.flags = (WindowManager.LayoutParams.FLAG_LAYOUT_IN_SCREEN
                or WindowManager.LayoutParams.FLAG_LAYOUT_INSET_DECOR
                or WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON
                or WindowManager.LayoutParams.FLAG_FULLSCREEN)

        windowManager = applicationContext.getSystemService(Context.WINDOW_SERVICE) as WindowManager
        rootView?.let {
            if (it.windowToken != null) {
                it.removeAllViews()
            }
        }
        rootView = RelativeLayout(this)
        rootView?.layoutParams = windowLayoutParams
        rootView?.addView(contentView)
        windowManager.addView(rootView, windowLayoutParams)

        val index: Int = getDefaultPreferences(prefTheme) as Int
        mThemeData = themeStorage()[index]

        val blurView = contentView.findViewById<BlurView>(R.id.blurView)
        val imagePreview = contentView.findViewById<ImageView>(R.id.imagePreview)

        sliderLayout = contentView.findViewById<View>(R.id.sliderLayout) as RelativeLayout
        slideToUnlock = contentView.findViewById<View>(R.id.slideToUnlock) as SlideToActView
        viewPager = contentView.findViewById<View>(R.id.viewPager) as ViewPager3
        txtForgetPass = contentView.findViewById<View>(R.id.txtForgetPass) as TextView

        pinEnable = getDefaultPreferences(prefPinEnable) as Boolean
        patternEnable = getDefaultPreferences(prefPatternEnable) as Boolean
        mainView =
            if (pinEnable && !patternEnable) 2 else if (patternEnable && !pinEnable) 3 else 1

        setLockBar()
        setPreviewImage(imagePreview)
        setPager(blurView)
        forgetPassword()

        blurView.setBlurredView(imagePreview)
    }

    private lateinit var inputMethodManager: InputMethodManager

    private fun forgetPassword() {
        inputMethodManager = getSystemService(INPUT_METHOD_SERVICE) as InputMethodManager

        val viewForgetPass = contentView.findViewById<RelativeLayout>(R.id.viewForgetPass)
        val patternHint = contentView.findViewById<LinearLayout>(R.id.patternHint)
        val btnDone = contentView.findViewById<LinearLayout>(R.id.btnDone)
        val btnBack = contentView.findViewById<ImageView>(R.id.btnBack)
        val edtQue = contentView.findViewById<TextView>(R.id.edtQue)
        val txtPass = contentView.findViewById<TextView>(R.id.txtPass)
        val edtPass = contentView.findViewById<EditText>(R.id.edtPass)
        val passLayout = contentView.findViewById<LinearLayout>(R.id.passLayout)
        val imagePreviewPass = contentView.findViewById<ImageView>(R.id.imagePreviewPass)
        val blurView1 = contentView.findViewById<BlurView>(R.id.blurView1)
        val forgottenView = contentView.findViewById<ForgottenView>(R.id.forgottenView)

        setPreviewImage(imagePreviewPass)
        blurView1.setBlurredView(imagePreviewPass)

        for (i in 0 until getPrefQuestionData().size) {
            val questionData: QuestionData = getPrefQuestionData()[i]
            if (getDefaultPreferences(prefSavedQuestion) == questionData.position) {
                edtQue.text = questionData.question
                break
            }
        }

        btnDone.setOnClickListener {
            val pinPass = getDefaultPreferences(prefPinPassword) as String
            val pinPattern = getDefaultPreferences(prefPatternPassword) as String
            if (edtPass.text.toString() == getDefaultPreferences(prefSavedAnswer)) {
                passLayout.gone
                val stringBuilder = StringBuilder()
                stringBuilder.append(resources.getString(R.string.your_password))
                if (pinPass.isNotEmpty()) {
                    stringBuilder
                        .append(resources.getString(R.string.pin_value))
                        .append(" ")
                        .append(pinPass)
                        .append("\n")
                }
                if (pinPattern.isNotEmpty()) {
                    stringBuilder
                        .append(resources.getString(R.string.pattern_value))
                        .append(" ")
                        .append(incValue(pinPattern))
                        .append("\n")
                    patternHint.visible
                    val forgottenPatternIds = pinPattern.map { it.toString().toInt() }
                    forgottenView.showForgottenPattern(forgottenPatternIds)
                }
                txtPass.text = stringBuilder
                edtPass.text.clear()
                dismissSoftKeyboard(inputMethodManager, edtPass)
            } else {
                edtPass.text.clear()
            }
        }

        btnBack.setOnClickListener {
            viewPager.setPagingEnabled(true)
            viewForgetPass.gone
            dismissSoftKeyboard(inputMethodManager, edtPass)
        }

        txtForgetPass.setOnClickListener {
            val currentPager = viewPager.currentItem
            if (mainView == 3) {
                if (currentPager == 0) blurView1.invisible else if (currentPager == 1) blurView1.visible
            } else {
                when (currentPager) {
                    0, 2 -> blurView1.visible
                    1 -> blurView1.invisible
                }
            }
            viewPager.setPagingEnabled(false)
            viewForgetPass.visible
            if (passLayout.visibility == View.GONE)
                dismissSoftKeyboard(inputMethodManager, edtPass)
            else
                showSoftKeyboard(inputMethodManager, edtPass)
        }
    }

    private fun incValue(input: String): String {
        var output = ""

        for (char in input) {
            val incrementedChar = (char.toInt() + 1).toChar()
            output += incrementedChar
        }
        return output
    }

    private fun setPager(blurView: BlurView) {
        customPagerAdapter = CustomPagerAdapter(this, mainView)
        viewPager.adapter = customPagerAdapter
        if (mainView == 3) viewPager.currentItem = 0 else viewPager.currentItem = 1

        if (!pinEnable && !patternEnable)
            viewPager.setPagingEnabled(false)

        if (pinEnable || patternEnable)
            slideToUnlock.gone

        viewPager.setOnPageChangeListener(object : ViewPager.OnPageChangeListener {
            override fun onPageScrolled(
                position: Int,
                positionOffset: Float,
                positionOffsetPixels: Int
            ) {
                if (mainView == 3) {
                    if (position == 0) blurView.invisible else if (position == 1) blurView.visible
                } else {
                    when (position) {
                        0, 2 -> blurView.visible
                        1 -> blurView.invisible
                    }
                }
            }

            override fun onPageSelected(position: Int) {}
            override fun onPageScrollStateChanged(state: Int) {}
        })
    }

    private fun setPreviewImage(preview: ImageView) {
        val image = getDefaultPreferences(prefWallpaper) as String
        var compareIt = wallpaperName
        compareIt = compareIt.replace("_", "")
        when {
            image in listOf("${wallpaperName}1", "${wallpaperName}2", "${wallpaperName}3") -> {
                val bitmap: Bitmap =
                    loadBitmapFromAssets("${wallpaperFolder}/$image${wallpaperExtension}")!!
                preview.setImageBitmap(bitmap)
            }

            image.contains(customWallpaperName) -> {
                val bitmap: Bitmap =
                    loadBitmapFromAssets("${wallpaperFolder}/$image${wallpaperExtension}")!!
                preview.setImageBitmap(bitmap)
            }

            image.contains(mainFolder) -> {
                val bitmap: Bitmap =
                    loadBitmapFromAssets("$image$wallpaperExtension")!!
                preview.setImageBitmap(bitmap)
            }

            image.contains(compareIt) -> {
                val sharePath = folderPath() + "$image${wallpaperExtension}"
                loadImageWithGlide(sharePath, preview)
            }

            else -> {
                loadImageWithGlide(image, preview)
            }
        }
    }

    private fun setLockBar() {
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
        slideToUnlock.gravity = Gravity.CENTER
        slideToUnlock.setOnUnlockListener(object : SlideToActView.OnUnlockListener {
            override fun onUnlock() {
                Handler().postDelayed({
                    unlockScreen()
                }, 100)
            }
        })
    }

    private val respectiveIds = intArrayOf(
        R.id.pinNum0,
        R.id.pinNum1,
        R.id.pinNum2,
        R.id.pinNum3,
        R.id.pinNum4,
        R.id.pinNum5,
        R.id.pinNum6,
        R.id.pinNum7,
        R.id.pinNum8,
        R.id.pinNum9
    )
    private lateinit var txtArray: Array<TextView>

    private val respectiveDotIds = intArrayOf(
        R.id.pe0,
        R.id.pe1,
        R.id.pe2,
        R.id.pe3
    )
    private lateinit var imgArray: Array<ImageView>

    fun screenOne(layout: ViewGroup) {
        val pinView = layout.findViewById<PinView>(R.id.pinView)
        pinView.setModeAuthenticate(this)

        imgArray = respectiveDotIds.map { id ->
            val imageView = layout.findViewById<ImageView>(id)
            imageView.setImageDrawable(
                getTintedSelectorDrawable()
            )
            imageView
        }.toTypedArray()

        val pinNumDel = layout.findViewById<TextView>(R.id.pinNumDel)
        applyPinNumberDeleteStyle(pinNumDel)

        val tvMessage = layout.findViewById<TextView>(R.id.tvMessage)
        applyPinNumberDeleteStyle(tvMessage)

        txtArray = respectiveIds.map { id ->
            val textView = layout.findViewById<TextView>(id)
            applyPinNumberLockStyle(textView)
            textView
        }.toTypedArray()
    }

    private fun getTintedSelectorDrawable(): Drawable? {
        val selectorDrawable = ContextCompat.getDrawable(this, R.drawable.pin_selector)
        return selectorDrawable?.mutate()?.apply {
            setColorFilter(resources.getColor(R.color.regularColor), PorterDuff.Mode.SRC_IN)
        }
    }

    private fun applyPinNumberLockStyle(textView: TextView) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            textView.setTextAppearance(R.style.PinNumberLock)
        } else {
            @Suppress("DEPRECATION")
            textView.setTextAppearance(textView.context, R.style.PinNumberLock)
        }
    }

    private fun applyPinNumberDeleteStyle(textView: TextView) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            textView.setTextAppearance(R.style.PinNumberDelete)
        } else {
            @Suppress("DEPRECATION")
            textView.setTextAppearance(textView.context, R.style.PinNumberDelete)
        }
    }

    fun screenTwo(layout: ViewGroup) {
        val insideLayout = layout.findViewById<View>(R.id.insideLayout) as RelativeLayout
        val layoutView = layoutInflater.inflate(mThemeData.layout, insideLayout, false)
        insideLayout.addView(layoutView)

        val hintView = layout.findViewById<RelativeLayout>(R.id.hintView)
        val btnHintPin = layout.findViewById<LinearLayout>(R.id.btnHintPin)
        val btnHintPattern = layout.findViewById<LinearLayout>(R.id.btnHintPattern)
        val shimmerPin = layout.findViewById<ShimmerTextView>(R.id.shimmerPin)
        val shimmerPattern = layout.findViewById<ShimmerTextView>(R.id.shimmerPattern)

        if (getDefaultPreferences(prefLockHint) as Boolean) {
            hintView.visible
            if (!pinEnable) btnHintPin.gone else {
                val shimmer = Shimmer()
                shimmer.direction = Shimmer.ANIMATION_DIRECTION_LTR
                shimmer.duration = 1000
                shimmer.start(shimmerPin)
                btnHintPin.visible
            }
            if (!patternEnable) btnHintPattern.gone else {
                val shimmer = Shimmer()
                shimmer.direction = Shimmer.ANIMATION_DIRECTION_RTL
                shimmer.duration = 1000
                shimmer.start(shimmerPattern)
                btnHintPattern.visible
            }
        } else hintView.gone

        btnHintPin.setOnClickListener { viewPager.currentItem = 0 }

        btnHintPattern.setOnClickListener {
            if (mainView == 3) viewPager.currentItem = 1 else viewPager.currentItem = 2
        }

        val txtTimeMeridian = layoutView.findViewById<TextView>(R.id.txt_time_meridian)
        val txtTime = layoutView.findViewById<TextView>(R.id.txt_time)
        val txtDay = layoutView.findViewById<TextView>(R.id.txt_day)
        val txtDate = layoutView.findViewById<TextView>(R.id.txt_date)
        val viewBackground = layoutView.findViewById<RelativeLayout>(R.id.view_background)
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

    private lateinit var patternTips: TextView
    fun screenThree(layout: ViewGroup) {
        val patternView = layout.findViewById<View>(R.id.patternView) as PatternLockView
        patternTips = layout.findViewById<View>(R.id.patternTips) as TextView
        patternView.setOnPatternListener(listener)
        patternTips.text = resources.getString(R.string.draw_pattern_to_unlock)
    }

    private var isWrong = false
    private var listener = object : PatternLockView.OnPatternListener {

        override fun onStarted() {
            viewPager.setPagingEnabled(false)
            super.onStarted()
        }

        override fun onProgress(ids: ArrayList<Int>) {
            super.onProgress(ids)
        }

        override fun onComplete(ids: ArrayList<Int>): Boolean {
            viewPager.setPagingEnabled(true)
            val resultAsString = getPatternString(ids)
            val oldPass = getDefaultPreferences(prefPatternPassword) as String
            if (resultAsString == oldPass) {
                unlockScreen()
                isWrong = false
            } else {
                passwordWrong()
                patternTips.text = getString(R.string.wrong_pattern_try_again)
                isWrong = true
            }
            return !isWrong
        }
    }

    var wrongPassCounter = 0
    private fun passwordWrong() {
        wrongPassCounter++
        if (wrongPassCounter == 3) txtForgetPass.visible
    }

    fun unlockScreen() {
        if (rootView != null) {
            windowManager.removeView(rootView)
            rootView = null
        }
        setPreferences(prefAlreadyLock, false)
        finishAffinity()
    }

    private fun loadImageWithGlide(imagePath: String, preview: ImageView) {
        Glide.with(this@LockActivity)
            .load(imagePath)
            .apply(RequestOptions().centerCrop())
            .into(preview)
    }

    private val callReceived: BroadcastReceiver = object : BroadcastReceiver() {
        override fun onReceive(context: Context, intent: Intent) {
            if (intent.getBooleanExtra("isCallReceived", false) && rootView != null) {
                windowManager.removeView(rootView)
                rootView = null
            }
        }
    }

    override fun onStart() {
        super.onStart()
        val filter = IntentFilter("PhoneStateLocalBroadcast")
        registerReceiver(callReceived, filter)
    }

    override fun onStop() {
        super.onStop()
        unregisterReceiver(callReceived)
    }

    override fun onPinCorrect() {
        unlockScreen()
    }

    override fun onPinWrong() {
        passwordWrong()
    }
}