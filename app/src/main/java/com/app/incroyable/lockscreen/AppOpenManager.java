package com.app.incroyable.lockscreen;


/**
 * Prefetches App Open Ads.
 */
//public class AppOpenManager implements LifecycleObserver, Application.ActivityLifecycleCallbacks {
//    private static final String LOG_TAG = "AppOpenManager";
//    private static boolean isShowingAd = false;
//    private final CustomApplication myApplication;
//    String placementId = "";
//    private AppOpenAd appOpenAd = null;
//    private Activity currentActivity;
//    private AppOpenAd.AppOpenAdLoadCallback loadCallback;
//    private long loadTime = 0;
//
//    /**
//     * Constructor
//     */
//    public AppOpenManager(CustomApplication myApplication, String placementId) {
//        this.myApplication = myApplication;
//        this.placementId = placementId;
//        this.myApplication.registerActivityLifecycleCallbacks(this);
//        ProcessLifecycleOwner.get().getLifecycle().addObserver(this);
//    }
//
//    /**
//     * LifecycleObserver methods
//     */
//    @OnLifecycleEvent(ON_START)
//    public void onStart() {
//        if (currentActivity != null) {
//            if (currentActivity instanceof LockActivity) {
//
//            } else {
//                showAdIfAvailable();
//                Log.d(LOG_TAG, "onStart" + "" + getClass().getName() + "" + currentActivity.getCallingPackage());
//            }
//        }
//    }
//
//    @Override
//    public void onActivityCreated(Activity activity, Bundle savedInstanceState) {
//    }
//
//    @Override
//    public void onActivityStarted(Activity activity) {
//        currentActivity = activity;
//    }
//
//    @Override
//    public void onActivityResumed(Activity activity) {
//        currentActivity = activity;
//    }
//
//    @Override
//    public void onActivityStopped(Activity activity) {
//    }
//
//    @Override
//    public void onActivityPaused(Activity activity) {
//    }
//
//    @Override
//    public void onActivitySaveInstanceState(Activity activity, Bundle bundle) {
//    }
//
//    @Override
//    public void onActivityDestroyed(Activity activity) {
//        currentActivity = null;
//    }
//
//    /**
//     * Creates and returns ad request.
//     */
//    private AdRequest getAdRequest() {
//        return new AdRequest.Builder().build();
//    }
//
//    private boolean wasLoadTimeLessThanNHoursAgo(long numHours) {
//        long dateDifference = (new Date()).getTime() - this.loadTime;
//        long numMilliSecondsPerHour = 3600000;
//        return (dateDifference < (numMilliSecondsPerHour * numHours));
//    }
//
//    /**
//     * Utility method that checks if ad exists and can be shown.
//     */
//    public boolean isAdAvailable() {
//        return appOpenAd != null && wasLoadTimeLessThanNHoursAgo(4);
//    }
//
//    public void showAdIfAvailable() {
//        // Only show ad if there is not already an app open ad currently showing
//        // and an ad is available.
//        if (!isShowingAd && isAdAvailable()) {
//            Log.d(LOG_TAG, "Will show ad.");
//
//            FullScreenContentCallback fullScreenContentCallback =
//                    new FullScreenContentCallback() {
//                        @Override
//                        public void onAdDismissedFullScreenContent() {
//                            // Set the reference to null so isAdAvailable() returns false.
//                            AppOpenManager.this.appOpenAd = null;
//                            isShowingAd = false;
//                            fetchAd();
//                        }
//
//                        @Override
//                        public void onAdFailedToShowFullScreenContent(AdError adError) {
//                        }
//
//                        @Override
//                        public void onAdShowedFullScreenContent() {
//                            isShowingAd = true;
//                        }
//                    };
//            appOpenAd.setFullScreenContentCallback(fullScreenContentCallback);
//            appOpenAd.show(currentActivity);
////            appOpenAd.show(currentActivity, fullScreenContentCallback);
//
//        } else {
//            Log.d(LOG_TAG, "Can not show ad.");
//            fetchAd();
//        }
//    }
//
//    /**
//     * Request an ad
//     */
//    public void fetchAd() {
//        // Have unused ad, no need to fetch another.
//        if (isAdAvailable()) {
//            return;
//        }
//
//        loadCallback =
//                new AppOpenAd.AppOpenAdLoadCallback() {
//                    /**
//                     * Called when an app open ad has loaded.
//                     *
//                     * @param ad the loaded app open ad.
//                     */
//                    @Override
//                    public void onAdLoaded(AppOpenAd ad) {
//                        AppOpenManager.this.appOpenAd = ad;
//                        AppOpenManager.this.loadTime = (new Date()).getTime();
//                    }
//
//                    /**
//                     * Called when an app open ad has failed to load.
//                     *
//                     * @param loadAdError the error.
//                     */
//                    @Override
//                    public void onAdFailedToLoad(LoadAdError loadAdError) {
//                        Log.e("ss", "onAdFailedToLoad: " + loadAdError.getCode());
//                        // Handle the error.
//                    }
//
//                };
//        AdRequest request = getAdRequest();
//        AppOpenAd.load(
//                myApplication, placementId, request,
//                AppOpenAd.APP_OPEN_AD_ORIENTATION_PORTRAIT, loadCallback);
//    }
//}
