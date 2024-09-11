package com.app.incroyable.lockscreen.adhelper

//private var mInterstitialAdMob: InterstitialAd? = null
//private var interstitialAdAL: MaxInterstitialAd? = null
//
//fun Activity.loadInterstitialAd() {
//    loadAdmobInterstitial()
//    loadApplovinInterstitial()
//}
//
//fun Activity.loadAdmobInterstitial() {
//    val placementId = getAdString(prefAdmobInterstitial)
//    if (getAdBoolean(prefAdmobInterstitialEnable) && placementId.isNotEmpty() && isOnline()) {
//        val adRequest = AdRequest.Builder().build()
//        InterstitialAd.load(
//            this,
//            placementId,
//            adRequest,
//            object : InterstitialAdLoadCallback() {
//                override fun onAdLoaded(interstitialAd: InterstitialAd) {
//                    mInterstitialAdMob = interstitialAd
//                    interstitialAd.fullScreenContentCallback =
//                        object : FullScreenContentCallback() {
//                            override fun onAdDismissedFullScreenContent() {
//                                mInterstitialAdMob = null
//                                loadAdmobInterstitial()
//                            }
//
//                            override fun onAdFailedToShowFullScreenContent(adError: AdError) {
//                                mInterstitialAdMob = null
//                            }
//
//                            override fun onAdShowedFullScreenContent() {}
//                        }
//                }
//
//                override fun onAdFailedToLoad(loadAdError: LoadAdError) {
//                    mInterstitialAdMob = null
//                }
//            })
//    }
//}
//
//fun Activity.loadApplovinInterstitial() {
//    val placementId = getAdString(prefApplovinInterstitial)
//    if (getAdBoolean(prefApplovinInterstitialEnable) && placementId.isNotEmpty() && isOnline()) {
//        interstitialAdAL = MaxInterstitialAd(placementId, this)
//        interstitialAdAL!!.setListener(object : MaxAdListener {
//            override fun onAdLoaded(ad: MaxAd) {}
//            override fun onAdDisplayed(ad: MaxAd) {}
//            override fun onAdHidden(ad: MaxAd) {
//                if (interstitialAdAL != null) {
//                    interstitialAdAL!!.loadAd()
//                }
//            }
//
//            override fun onAdClicked(ad: MaxAd) {}
//            override fun onAdLoadFailed(adUnitId: String, error: MaxError) {}
//            override fun onAdDisplayFailed(ad: MaxAd, error: MaxError) {}
//        })
//        if (interstitialAdAL != null) {
//            interstitialAdAL!!.loadAd()
//        }
//    }
//}
//
//fun Activity.showInterstitialAd() {
//    val adCounterPref = getAdInt(adCounter, 1).coerceAtLeast(1)
//    val firstAd = getAdInt(prefFirstAd, prefFirstAdDefault)
//    val secondAd = getAdInt(prefSecondAd, prefSecondAdDefault)
//    val getValue = when {
//        adCounterPref % firstAd == 0 -> 0
//        adCounterPref % secondAd == 0 -> 1
//        else -> 2
//    }
//    when (getValue) {
//        0 -> showApplovinInterstitial()
//        1 -> showAdmobInterstitial()
//    }
//    incrementCounter()
//}
//
//fun Activity.showAdmobInterstitial() {
//    if (mInterstitialAdMob != null) mInterstitialAdMob!!.show(this)
//}
//
//fun showApplovinInterstitial() {
//    if (interstitialAdAL != null && interstitialAdAL!!.isReady) {
//        interstitialAdAL!!.showAd()
//    }
//}