package com.app.incroyable.lockscreen.adhelper

//private var nativeAdMob: NativeAd? = null
//
//fun Activity.loadAdmobNative(frameLayout: ViewGroup) {
//    val placementId = getAdString(prefAdmobNative)
//    if (getAdBoolean(prefAdmobNativeEnable) && placementId.isNotEmpty() && isOnline()) {
//        val builder = AdLoader.Builder(this, placementId)
//        builder.forNativeAd { nativeAd ->
//            if (nativeAdMob != null)
//                nativeAdMob!!.destroy()
//            nativeAdMob = nativeAd
//            val adView = layoutInflater.inflate(
//                R.layout.ad_native_admob,
//                null
//            ) as NativeAdView
//            populateNativeAdView(nativeAd, adView)
//            frameLayout.removeAllViews()
//            frameLayout.addView(adView)
//        }
//        val adLoader = builder.withAdListener(object : AdListener() {
//            override fun onAdFailedToLoad(loadAdError: LoadAdError) {}
//        }).build()
//        adLoader.loadAd(AdRequest.Builder().build())
//    }
//}
//
//fun populateNativeAdView(nativeAd: NativeAd, adView: NativeAdView) {
//    adView.mediaView = adView.findViewById<View>(R.id.ad_media) as MediaView
//    adView.headlineView = adView.findViewById<View>(R.id.ad_headline)
//    adView.bodyView = adView.findViewById<View>(R.id.ad_body)
//    adView.callToActionView = adView.findViewById<View>(R.id.ad_call_to_action)
//    adView.iconView = adView.findViewById<View>(R.id.ad_app_icon)
//    adView.priceView = adView.findViewById<View>(R.id.ad_price)
//    adView.starRatingView = adView.findViewById<View>(R.id.ad_stars)
//    adView.storeView = adView.findViewById<View>(R.id.ad_store)
//    adView.advertiserView = adView.findViewById<View>(R.id.ad_advertiser)
//    (adView.headlineView as TextView?)!!.text = nativeAd.headline
//    adView.mediaView!!.mediaContent = nativeAd.mediaContent
//    if (nativeAd.body == null) {
//        adView.bodyView!!.invisible
//    } else {
//        adView.bodyView!!.visible
//        (adView.bodyView as TextView?)!!.text = nativeAd.body
//    }
//    if (nativeAd.callToAction == null) {
//        adView.callToActionView!!.invisible
//    } else {
//        adView.callToActionView!!.visible
//        (adView.callToActionView as Button?)!!.text = nativeAd.callToAction
//    }
//    if (nativeAd.icon == null) {
//        adView.iconView!!.gone
//    } else {
//        (adView.iconView as ImageView?)!!.setImageDrawable(nativeAd.icon!!.drawable)
//        adView.iconView!!.visible
//    }
//    if (nativeAd.price == null) {
//        adView.priceView!!.invisible
//    } else {
//        adView.priceView!!.visible
//        (adView.priceView as TextView?)!!.text = nativeAd.price
//    }
//    if (nativeAd.store == null) {
//        adView.storeView!!.invisible
//    } else {
//        adView.storeView!!.visible
//        (adView.storeView as TextView?)!!.text = nativeAd.store
//    }
//    if (nativeAd.starRating == null) {
//        adView.starRatingView!!.invisible
//    } else {
//        (adView.starRatingView as RatingBar?)!!.rating = nativeAd.starRating!!.toFloat()
//        adView.starRatingView!!.visible
//    }
//    if (nativeAd.advertiser == null) {
//        adView.advertiserView!!.invisible
//    } else {
//        (adView.advertiserView as TextView?)!!.text = nativeAd.advertiser
//        adView.advertiserView!!.visible
//    }
//    adView.setNativeAd(nativeAd)
//}