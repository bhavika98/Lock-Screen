package com.app.incroyable.lockscreen.model

import com.google.gson.annotations.SerializedName

data class AppData(

    @SerializedName("admobNative")
    val admobNative: String,

    @SerializedName("admobNativeEnable")
    val admobNativeEnable: Boolean,

    @SerializedName("admobBanner")
    val admobBanner: String,

    @SerializedName("admobBannerEnable")
    val admobBannerEnable: Boolean,

    @SerializedName("admobInterstitial")
    val admobInterstitial: String,

    @SerializedName("admobInterstitialEnable")
    val admobInterstitialEnable: Boolean,

    @SerializedName("admobAppOpen")
    val admobAppOpen: String,

    @SerializedName("admobAppOpenEnable")
    val admobAppOpenEnable: Boolean,

    @SerializedName("applovinBanner")
    val applovinBanner: String,

    @SerializedName("applovinBannerEnable")
    val applovinBannerEnable: Boolean,

    @SerializedName("applovinInterstitial")
    val applovinInterstitial: String,

    @SerializedName("applovinInterstitialEnable")
    val applovinInterstitialEnable: Boolean,

    @SerializedName("bannerTop")
    val bannerTop: String,

    @SerializedName("bannerBottom")
    val bannerBottom: String,

    @SerializedName("nativeList")
    val nativeList: Int,

    @SerializedName("nativeGrid")
    val nativeGrid: Int,

    @SerializedName("firstAd")
    val firstAd: Int,

    @SerializedName("secondAd")
    val secondAd: Int,

    @SerializedName("pub")
    val pub: String,

    @SerializedName("privacy")
    val privacy: String,
)