package com.app.incroyable.lockscreen.adhelper

//enum class BannerEnum(val value: String) {
//    ADMOB("admob"),
//    APPLOVIN("applovin");
//
//    companion object {
//        fun fromString(value: String): BannerEnum? {
//            return values().find { it.value == value }
//        }
//    }
//}
//
//fun Activity.topBanner(adView: ViewGroup, adLayout: ViewGroup) {
//    loadBanner(getAdString(prefBannerTop), adView, adLayout)
//}
//
//fun Activity.bottomBanner(adView: ViewGroup, adLayout: ViewGroup) {
//    loadBanner(getAdString(prefBannerBottom), adView, adLayout)
//}
//
//private fun Activity.loadBanner(value: String, adView: ViewGroup, adLayout: ViewGroup) {
//    when (BannerEnum.fromString(value)) {
//        BannerEnum.ADMOB -> loadAdmobBanner(adView, adLayout)
//        BannerEnum.APPLOVIN -> loadApplovinBanner(adView, adLayout)
//        else -> adLayout.gone
//    }
//}
//
//fun Activity.loadAdmobBanner(adView: ViewGroup, adLayout: ViewGroup) {
//    val placementId = getAdString(prefAdmobBanner)
//    if (getAdBoolean(prefAdmobBannerEnable) && placementId.isNotEmpty() && isOnline()) {
//        adLayout.visible
//        val mAdView = AdView(this)
//        mAdView.setAdSize(getAdSize())
//        mAdView.adUnitId = placementId
//        mAdView.adListener = object : AdListener() {
//            override fun onAdLoaded() {
//                super.onAdLoaded()
//                adView.removeAllViews()
//                adView.addView(mAdView)
//            }
//
//            override fun onAdFailedToLoad(loadAdError: LoadAdError) {
//                super.onAdFailedToLoad(loadAdError)
//            }
//        }
//        val adRequest = AdRequest.Builder().build()
//        mAdView.loadAd(adRequest)
//        adView.removeAllViews()
//    } else adLayout.gone
//}
//
//private fun Activity.getAdSize(): AdSize {
//    val display: Display = windowManager.defaultDisplay
//    val outMetrics = DisplayMetrics()
//    display.getMetrics(outMetrics)
//    val widthPixels = outMetrics.widthPixels.toFloat()
//    val density = outMetrics.density
//    val adWidth = (widthPixels / density).toInt()
//    return AdSize.getCurrentOrientationAnchoredAdaptiveBannerAdSize(this, adWidth)
//}
//
//fun Activity.loadApplovinBanner(adView: ViewGroup, adLayout: ViewGroup) {
//    val placementId = getAdString(prefApplovinBanner)
//    if (getAdBoolean(prefApplovinBannerEnable) && placementId.isNotEmpty() && isOnline()) {
//        adLayout.visible
//        val maxAdView = MaxAdView(placementId, this)
//        maxAdView.setListener(object : MaxAdViewAdListener {
//            override fun onAdExpanded(ad: MaxAd) {
//            }
//
//            override fun onAdCollapsed(ad: MaxAd) {
//            }
//
//            override fun onAdLoaded(ad: MaxAd) {
//            }
//
//            override fun onAdDisplayed(ad: MaxAd) {
//            }
//
//            override fun onAdHidden(ad: MaxAd) {
//            }
//
//            override fun onAdClicked(ad: MaxAd) {
//            }
//
//            override fun onAdLoadFailed(adUnitId: String, error: MaxError) {
//            }
//
//            override fun onAdDisplayFailed(ad: MaxAd, error: MaxError) {
//            }
//        })
//        maxAdView.setRevenueListener { }
//        val width = ViewGroup.LayoutParams.MATCH_PARENT
//        val heightDp = MaxAdFormat.BANNER.getAdaptiveSize(this).height
//        val heightPx = AppLovinSdkUtils.dpToPx(this, heightDp)
//        maxAdView.layoutParams = FrameLayout.LayoutParams(width, heightPx)
//        maxAdView.setBackgroundColor(Color.TRANSPARENT)
//        adView.addView(maxAdView)
//        maxAdView.loadAd()
//    } else adLayout.gone
//}