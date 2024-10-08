package com.app.incroyable.lockscreen.adtemplate.view;

//public class TemplateView extends FrameLayout {
//
//    private int templateType;
//    private NativeTemplateStyle styles;
//    private NativeAdView nativeAdView;
//
//    private TextView primaryView;
//    private TextView secondaryView;
//    private RatingBar ratingBar;
//    private TextView tertiaryView;
//    private ImageView iconView;
//    private MediaView mediaView;
//    private Button callToActionView;
//    private RelativeLayout background;
//
//    public TemplateView(Context context) {
//        super(context);
//    }
//
//    public TemplateView(Context context, @Nullable AttributeSet attrs) {
//        super(context, attrs);
//        initView(context, attrs);
//    }
//
//    public TemplateView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
//        super(context, attrs, defStyleAttr);
//        initView(context, attrs);
//    }
//
//    public void setStyles(NativeTemplateStyle styles) {
//        this.styles = styles;
//        this.applyStyles();
//    }
//
//    private void applyStyles() {
//
//        Drawable mainBackground = styles.getMainBackgroundColor();
//        if (mainBackground != null) {
//            background.setBackground(mainBackground);
//            if (primaryView != null) {
//                primaryView.setBackground(mainBackground);
//            }
//            if (secondaryView != null) {
//                secondaryView.setBackground(mainBackground);
//            }
//            if (tertiaryView != null) {
//                tertiaryView.setBackground(mainBackground);
//            }
//        }
//
//        Typeface primary = styles.getPrimaryTextTypeface();
//        if (primary != null && primaryView != null) {
//            primaryView.setTypeface(primary);
//        }
//
//        Typeface secondary = styles.getSecondaryTextTypeface();
//        if (secondary != null && secondaryView != null) {
//            secondaryView.setTypeface(secondary);
//        }
//
//        Typeface tertiary = styles.getTertiaryTextTypeface();
//        if (tertiary != null && tertiaryView != null) {
//            tertiaryView.setTypeface(tertiary);
//        }
//
//        Typeface ctaTypeface = styles.getCallToActionTextTypeface();
//        if (ctaTypeface != null && callToActionView != null) {
//            callToActionView.setTypeface(ctaTypeface);
//        }
//
//        int primaryTypefaceColor = styles.getPrimaryTextTypefaceColor();
//        if (primaryTypefaceColor > 0 && primaryView != null) {
//            primaryView.setTextColor(primaryTypefaceColor);
//        }
//
//        int secondaryTypefaceColor = styles.getSecondaryTextTypefaceColor();
//        if (secondaryTypefaceColor > 0 && secondaryView != null) {
//            secondaryView.setTextColor(secondaryTypefaceColor);
//        }
//
//        int tertiaryTypefaceColor = styles.getTertiaryTextTypefaceColor();
//        if (tertiaryTypefaceColor > 0 && tertiaryView != null) {
//            tertiaryView.setTextColor(tertiaryTypefaceColor);
//        }
//
//        int ctaTypefaceColor = styles.getCallToActionTypefaceColor();
//        if (ctaTypefaceColor > 0 && callToActionView != null) {
//            callToActionView.setTextColor(ctaTypefaceColor);
//        }
//
//        float ctaTextSize = styles.getCallToActionTextSize();
//        if (ctaTextSize > 0 && callToActionView != null) {
//            callToActionView.setTextSize(ctaTextSize);
//        }
//
//        float primaryTextSize = styles.getPrimaryTextSize();
//        if (primaryTextSize > 0 && primaryView != null) {
////      primaryView.setTextSize(primaryTextSize);
//        }
//
//        float secondaryTextSize = styles.getSecondaryTextSize();
//        if (secondaryTextSize > 0 && secondaryView != null) {
////      secondaryView.setTextSize(secondaryTextSize);
//        }
//
//        float tertiaryTextSize = styles.getTertiaryTextSize();
//        if (tertiaryTextSize > 0 && tertiaryView != null) {
//            tertiaryView.setTextSize(tertiaryTextSize);
//        }
//
//        Drawable ctaBackground = styles.getCallToActionBackgroundColor();
//        if (ctaBackground != null && callToActionView != null) {
//            callToActionView.setBackground(ctaBackground);
//        }
//
//        Drawable primaryBackground = styles.getPrimaryTextBackgroundColor();
//        if (primaryBackground != null && primaryView != null) {
//            primaryView.setBackground(primaryBackground);
//        }
//
//        Drawable secondaryBackground = styles.getSecondaryTextBackgroundColor();
//        if (secondaryBackground != null && secondaryView != null) {
//            secondaryView.setBackground(secondaryBackground);
//        }
//
//        Drawable tertiaryBackground = styles.getTertiaryTextBackgroundColor();
//        if (tertiaryBackground != null && tertiaryView != null) {
//            tertiaryView.setBackground(tertiaryBackground);
//        }
//
//        invalidate();
//        requestLayout();
//    }
//
//    private boolean adHasOnlyStore(NativeAd nativeAd) {
//        String store = nativeAd.getStore();
//        String advertiser = nativeAd.getAdvertiser();
//        return !TextUtils.isEmpty(store) && TextUtils.isEmpty(advertiser);
//    }
//
//    public void setNativeAd(NativeAd nativeAd) {
//
//        String store = nativeAd.getStore();
//        String advertiser = nativeAd.getAdvertiser();
//        String headline = nativeAd.getHeadline();
//        String body = nativeAd.getBody();
//        String cta = nativeAd.getCallToAction();
//        Double starRating = nativeAd.getStarRating();
//        NativeAd.Image icon = nativeAd.getIcon();
//
//        String secondaryText;
//
//        nativeAdView.setCallToActionView(callToActionView);
//        nativeAdView.setHeadlineView(primaryView);
//        nativeAdView.setMediaView(mediaView);
//        secondaryView.setVisibility(VISIBLE);
//        if (adHasOnlyStore(nativeAd)) {
//            nativeAdView.setStoreView(secondaryView);
//            secondaryText = store;
//        } else if (!TextUtils.isEmpty(advertiser)) {
//            nativeAdView.setAdvertiserView(secondaryView);
//            secondaryText = advertiser;
//        } else {
//            secondaryText = "";
//        }
//
//        primaryView.setText(headline);
//        callToActionView.setText(cta);
//
//        //  Set the secondary view to be the star rating if available.
//        if (starRating != null && starRating > 0) {
//            secondaryView.setVisibility(GONE);
//            ratingBar.setVisibility(VISIBLE);
//            ratingBar.setMax(5);
//            nativeAdView.setStarRatingView(ratingBar);
//        } else {
//            secondaryView.setText(secondaryText);
//            secondaryView.setVisibility(VISIBLE);
//            ratingBar.setVisibility(GONE);
//        }
//
//        if (icon != null) {
//            iconView.setVisibility(VISIBLE);
//            iconView.setImageDrawable(icon.getDrawable());
//        } else {
//            iconView.setVisibility(GONE);
//        }
//
//        if (tertiaryView != null) {
//            tertiaryView.setText(body);
//            nativeAdView.setBodyView(tertiaryView);
//        }
//
//        nativeAdView.setNativeAd(nativeAd);
//    }
//
//    private void initView(Context context, AttributeSet attributeSet) {
//
//        TypedArray attributes =
//                context.getTheme().obtainStyledAttributes(attributeSet, R.styleable.TemplateView, 0, 0);
//
//        try {
//            templateType =
//                    attributes.getResourceId(
//                            R.styleable.TemplateView_gnt_template_type, R.layout.ad_native_admob_grid);
//        } finally {
//            attributes.recycle();
//        }
//        LayoutInflater inflater =
//                (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
//        inflater.inflate(templateType, this);
//    }
//
//    @Override
//    public void onFinishInflate() {
//        super.onFinishInflate();
//        nativeAdView = findViewById(R.id.native_ad_view);
//        primaryView = findViewById(R.id.primary);
//        secondaryView = findViewById(R.id.secondary);
//        tertiaryView = findViewById(R.id.body);
//
//        ratingBar = findViewById(R.id.rating_bar);
//        ratingBar.setEnabled(false);
//
//        callToActionView = findViewById(R.id.cta);
//        iconView = findViewById(R.id.icon);
//        mediaView = findViewById(R.id.media_view);
//        background = findViewById(R.id.background);
//    }
//}
