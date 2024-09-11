package com.app.incroyable.lockscreen.adhelper

//fun Activity.loadAdmobAppOpen(adLoaded: () -> Unit = {}) {
//    val placementId = getAdString(prefAdmobAppOpen)
//    if (getAdBoolean(prefAdmobAppOpenEnable) && placementId.isNotEmpty() && isOnline()) {
//        AppOpenAd.load(
//            this,
//            placementId,
//            AdRequest.Builder().build(),
//            1,
//            object : AppOpenAdLoadCallback() {
//                override fun onAdLoaded(appOpenAd: AppOpenAd) {
//                    super.onAdLoaded(appOpenAd)
//                    appOpenAd.fullScreenContentCallback = object : FullScreenContentCallback() {
//                        override fun onAdShowedFullScreenContent() {}
//                        override fun onAdDismissedFullScreenContent() {
//                            adLoaded()
//                        }
//
//                        override fun onAdFailedToShowFullScreenContent(adError: AdError) {
//                            adLoaded()
//                        }
//                    }
//                    appOpenAd.show(this@loadAdmobAppOpen)
//                }
//
//                override fun onAdFailedToLoad(loadAdError: LoadAdError) {
//                    super.onAdFailedToLoad(loadAdError)
//                    adLoaded()
//                }
//            })
//    } else adLoaded()
//}