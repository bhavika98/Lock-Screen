package com.app.incroyable.lockscreen.storage

import android.app.Activity
import android.content.Context
import com.app.incroyable.lockscreen.model.AppData

private const val adPreferences = "adPreferences"
const val adCounter = "adCounter"

const val prefPub = "prefPub"
const val prefPrivacy = "prefPrivacy"

const val prefAdmobNative = "prefAdmobNative"
const val prefAdmobNativeEnable = "prefAdmobNativeEnable"

const val prefAdmobBanner = "prefAdmobBanner"
const val prefAdmobBannerEnable = "prefAdmobBannerEnable"

const val prefAdmobInterstitial = "prefAdmobInterstitial"
const val prefAdmobInterstitialEnable = "prefAdmobInterstitialEnable"

const val prefAdmobAppOpen = "prefAdmobAppOpen"
const val prefAdmobAppOpenEnable = "prefAdmobAppOpenEnable"

const val prefApplovinBanner = "prefApplovinBanner"
const val prefApplovinBannerEnable = "prefApplovinBannerEnable"

const val prefApplovinInterstitial = "prefApplovinInterstitial"
const val prefApplovinInterstitialEnable = "prefApplovinInterstitialEnable"

const val prefBannerTop = "prefBannerTop"
const val prefBannerBottom = "prefBannerBottom"

const val prefNativeList = "prefNativeList"
const val prefNativeListDefault = 20

const val prefNativeGrid = "prefNativeGrid"
const val prefNativeGridDefault = 12

const val prefFirstAd = "prefFirstAd"
const val prefFirstAdDefault = 4

const val prefSecondAd = "prefSecondAd"
const val prefSecondAdDefault = 2

fun Context.setTestAdPreferences() {
    val sharedPreferences = this.getSharedPreferences(
        adPreferences,
        Activity.MODE_PRIVATE
    )
    val editor = sharedPreferences.edit()

    editor.putString(prefPub, "")
    editor.putString(prefPrivacy, "")

    editor.putString(prefAdmobNative, "ca-app-pub-3940256099942544/2247696110")
    editor.putBoolean(prefAdmobNativeEnable, true)

    editor.putString(prefAdmobBanner, "ca-app-pub-3940256099942544/6300978111")
    editor.putBoolean(prefAdmobBannerEnable, true)

    editor.putString(prefAdmobInterstitial, "ca-app-pub-3940256099942544/1033173712")
    editor.putBoolean(prefAdmobInterstitialEnable, true)

    editor.putString(prefAdmobAppOpen, "ca-app-pub-3940256099942544/3419835294")
    editor.putBoolean(prefAdmobAppOpenEnable, true)

    editor.putString(prefApplovinBanner, "YOUR_AD_UNIT_ID")
    editor.putBoolean(prefApplovinBannerEnable, true)

    editor.putString(prefApplovinInterstitial, "YOUR_AD_UNIT_ID")
    editor.putBoolean(prefApplovinInterstitialEnable, true)

    editor.putString(prefBannerTop, "applovin")
    editor.putString(prefBannerBottom, "admob")

    editor.putInt(prefNativeList, prefNativeListDefault)
    editor.putInt(prefNativeGrid, prefNativeGridDefault)

    editor.putInt(prefFirstAd, prefFirstAdDefault)
    editor.putInt(prefSecondAd, prefSecondAdDefault)

    editor.commit()
}

fun Context.setAdPreferences(appData: AppData) {
    val sharedPreferences = this.getSharedPreferences(
        adPreferences,
        Activity.MODE_PRIVATE
    )
    val editor = sharedPreferences.edit()

    editor.putString(prefPub, appData.pub)
    editor.putString(prefPrivacy, appData.privacy)

    editor.putString(prefAdmobNative, appData.admobNative)
    editor.putBoolean(prefAdmobNativeEnable, appData.admobNativeEnable)

    editor.putString(prefAdmobBanner, appData.admobBanner)
    editor.putBoolean(prefAdmobBannerEnable, appData.admobBannerEnable)

    editor.putString(prefAdmobInterstitial, appData.admobInterstitial)
    editor.putBoolean(prefAdmobInterstitialEnable, appData.admobInterstitialEnable)

    editor.putString(prefAdmobAppOpen, appData.admobAppOpen)
    editor.putBoolean(prefAdmobAppOpenEnable, appData.admobAppOpenEnable)

    editor.putString(prefApplovinBanner, appData.applovinBanner)
    editor.putBoolean(prefApplovinBannerEnable, appData.applovinBannerEnable)

    editor.putString(prefApplovinInterstitial, appData.applovinInterstitial)
    editor.putBoolean(prefApplovinInterstitialEnable, appData.applovinInterstitialEnable)

    editor.putString(prefBannerTop, appData.bannerTop)
    editor.putString(prefBannerBottom, appData.bannerBottom)

    editor.putInt(prefNativeList, appData.nativeList)
    editor.putInt(prefNativeGrid, appData.nativeGrid)

    editor.putInt(prefFirstAd, appData.firstAd)
    editor.putInt(prefSecondAd, appData.secondAd)

    editor.commit()
}

const val initialValue = 0
fun Context.incrementCounter() {
    val sharedPreferences = this.getSharedPreferences(
        adPreferences,
        Activity.MODE_PRIVATE
    )
    var value = getAdInt(adCounter, initialValue)
    value = if (value >= 1000) initialValue else value + 1
    val editor = sharedPreferences.edit()
    editor.putInt(adCounter, value)
    editor.apply()
}

fun Context.resetCounter() {
    val sharedPreferences = this.getSharedPreferences(
        adPreferences,
        Activity.MODE_PRIVATE
    )
    val editor = sharedPreferences.edit()
    editor.putInt(adCounter, initialValue)
    editor.apply()
}

fun Context.getAdString(
    key: String
): String {
    val sharedPreferences = this.getSharedPreferences(
        adPreferences,
        Activity.MODE_PRIVATE
    )
    return sharedPreferences.getString(key, "")!!
}

fun Context.getAdInt(
    key: String,
    value: Int
): Int {
    val sharedPreferences = this.getSharedPreferences(
        adPreferences,
        Activity.MODE_PRIVATE
    )
    return sharedPreferences.getInt(key, value)
}

fun Context.getAdBoolean(
    key: String
): Boolean {
    val sharedPreferences = this.getSharedPreferences(
        adPreferences,
        Activity.MODE_PRIVATE
    )
    return sharedPreferences.getBoolean(key, false)
}

fun Context.clearAdPreferences() {
    val sharedPreferences = this.getSharedPreferences(
        adPreferences,
        Activity.MODE_PRIVATE
    )
    sharedPreferences.edit().clear().apply()
}
