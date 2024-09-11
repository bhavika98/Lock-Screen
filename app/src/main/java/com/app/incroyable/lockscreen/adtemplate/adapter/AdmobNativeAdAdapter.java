package com.app.incroyable.lockscreen.adtemplate.adapter;

//public class AdmobNativeAdAdapter extends RecyclerViewAdapterWrapper {
//
//    private static final int TYPE_FB_NATIVE_ADS = 900;
//    private static final int DEFAULT_AD_ITEM_INTERVAL = 4;
//
//    private final Param mParam;
//
//    private AdmobNativeAdAdapter(Param param) {
//        super(param.adapter);
//        this.mParam = param;
//
//        assertConfig();
//        setSpanAds();
//    }
//
//    private void assertConfig() {
//        if (mParam.gridLayoutManager != null) {
//            //if user set span ads
//            int nCol = mParam.gridLayoutManager.getSpanCount();
//            if (mParam.adItemInterval % nCol != 0) {
//                throw new IllegalArgumentException(String.format("The adItemInterval (%d) is not divisible by number of columns in GridLayoutManager (%d)", mParam.adItemInterval, nCol));
//            }
//        }
//    }
//
//    private int convertAdPosition2OrgPosition(int position) {
//
//        return position - (position + 1) / (mParam.adItemInterval + 1);
//    }
//
//    @Override
//    public int getItemCount() {
//        int realCount = super.getItemCount();
//        return realCount + realCount / mParam.adItemInterval;
//    }
//
//    @Override
//    public int getItemViewType(int position) {
//        if (isAdPosition(position)) {
//            return TYPE_FB_NATIVE_ADS;
//        }
//        return super.getItemViewType(convertAdPosition2OrgPosition(position));
//    }
//
//    private boolean isAdPosition(int position) {
//        /*if(position==1|| position==4)return true;*/
//        return (position + 1) % (mParam.adItemInterval + 1) == 0;
//    }
//
//    private void onBindAdViewHolder(final RecyclerView.ViewHolder holder) {
//        final AdViewHolder adHolder = (AdViewHolder) holder;
//        if (mParam.forceReloadAdOnBind || !adHolder.loaded) {
//            AdLoader adLoader = new AdLoader.Builder(adHolder.getContext(), mParam.admobNativeId)
//                    .forNativeAd(NativeAd -> {
//                        NativeTemplateStyle.Builder builder = new NativeTemplateStyle.Builder();
//                        builder.withPrimaryTextSize(11f);
//                        builder.withSecondaryTextSize(10f);
//                        builder.withTertiaryTextSize(06f);
//                        builder.withCallToActionTextSize(11f);
//
//                        switch (mParam.layout) {
//                            case 0: {
//                                adHolder.templateList.setVisibility(View.VISIBLE);
//                                adHolder.templateList.setStyles(builder.build());
//                                adHolder.templateList.setNativeAd(NativeAd);
//                                break;
//                            }
//                            case 1: {
//                                adHolder.templateGrid.setVisibility(View.VISIBLE);
//                                adHolder.templateGrid.setStyles(builder.build());
//                                adHolder.templateGrid.setNativeAd(NativeAd);
//                                break;
//                            }
//                        }
//
//                        adHolder.loaded = true;
//
//                    })
//                    .withAdListener(new AdListener() {
//                        @Override
//                        public void onAdFailedToLoad(@NonNull LoadAdError loadAdError) {
//                            super.onAdFailedToLoad(loadAdError);
//                            adHolder.adContainer.setVisibility(View.GONE);
//                        }
//                    })
//                    .withNativeAdOptions(new NativeAdOptions.Builder()
//                            // Methods in the NativeAdOptions.Builder class can be
//                            // used here to specify individual options settings.
//                            .build())
//                    .build();
//            adLoader.loadAd(new AdRequest.Builder().build());
//        }
//    }
//
//    @Override
//    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
//        if (getItemViewType(position) == TYPE_FB_NATIVE_ADS) {
//            onBindAdViewHolder(holder);
//        } else {
//            super.onBindViewHolder(holder, convertAdPosition2OrgPosition(position));
//        }
//    }
//
//    private RecyclerView.ViewHolder onCreateAdViewHolder(ViewGroup parent) {
//        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
//        View adLayoutOutline = inflater
//                .inflate(mParam.itemContainerLayoutRes, parent, false);
//        ViewGroup vg = adLayoutOutline.findViewById(mParam.itemContainerId);
//
//        LinearLayout adLayoutContent = (LinearLayout) inflater
//                .inflate(R.layout.ad_item_custom_admob_native, parent, false);
//        vg.addView(adLayoutContent);
//        return new AdViewHolder(adLayoutOutline);
//    }
//
//    @Override
//    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
//        if (viewType == TYPE_FB_NATIVE_ADS) {
//            return onCreateAdViewHolder(parent);
//        }
//        return super.onCreateViewHolder(parent, viewType);
//    }
//
//    private void setSpanAds() {
//        if (mParam.gridLayoutManager == null) {
//            return;
//        }
//        final GridLayoutManager.SpanSizeLookup spl = mParam.gridLayoutManager.getSpanSizeLookup();
//        mParam.gridLayoutManager.setSpanSizeLookup(new GridLayoutManager.SpanSizeLookup() {
//            @Override
//            public int getSpanSize(int position) {
//                if (isAdPosition(position)) {
//                    return 3;
//                }
//                return 1;
//            }
//        });
//    }
//
//    private static class Param {
//        String admobNativeId;
//        RecyclerView.Adapter adapter;
//        int adItemInterval;
//        boolean forceReloadAdOnBind;
//
//        int layout;
//
//        @LayoutRes
//        int itemContainerLayoutRes;
//
//        @IdRes
//        int itemContainerId;
//
//        GridLayoutManager gridLayoutManager;
//    }
//
//    public static class Builder {
//        private final Param mParam;
//
//        private Builder(Param param) {
//            mParam = param;
//        }
//
//        public static Builder with(String placementId, RecyclerView.Adapter wrapped, String layout) {
//            Param param = new Param();
//            param.admobNativeId = placementId;
//            param.adapter = wrapped;
//
//            switch (layout) {
//                case "list": {
//                    param.layout = 0;
//                    break;
//                }
//                case "grid": {
//                    param.layout = 1;
//                    break;
//                }
//            }
//
//            //default value
//            param.adItemInterval = DEFAULT_AD_ITEM_INTERVAL;
//            param.itemContainerLayoutRes = R.layout.ad_item_custom_admob_native_outline;
//            param.itemContainerId = R.id.ad_container;
//            param.forceReloadAdOnBind = true;
//            return new Builder(param);
//        }
//
//        public Builder adItemInterval(int interval) {
//            mParam.adItemInterval = interval;
//            return this;
//        }
//
//        public Builder adLayout(@LayoutRes int layoutContainerRes, @IdRes int itemContainerId) {
//            mParam.itemContainerLayoutRes = layoutContainerRes;
//            mParam.itemContainerId = itemContainerId;
//            return this;
//        }
//
//        public AdmobNativeAdAdapter build() {
//            return new AdmobNativeAdAdapter(mParam);
//        }
//
//        public Builder enableSpanRow(GridLayoutManager layoutManager) {
//            mParam.gridLayoutManager = layoutManager;
//            return this;
//        }
//
//        public Builder adItemIterval(int i) {
//            mParam.adItemInterval = i;
//            return this;
//        }
//
//        public Builder forceReloadAdOnBind(boolean forced) {
//            mParam.forceReloadAdOnBind = forced;
//            return this;
//        }
//    }
//
//    private static class AdViewHolder extends RecyclerView.ViewHolder {
//
//        TemplateView templateList, templateGrid;
//        LinearLayout adContainer;
//        boolean loaded;
//
//        AdViewHolder(View view) {
//            super(view);
//            templateList = view.findViewById(R.id.my_template_list);
//            templateGrid = view.findViewById(R.id.my_template_grid);
//            loaded = false;
//            adContainer = (LinearLayout) view.findViewById(R.id.native_ad_container);
//        }
//
//        Context getContext() {
//            return adContainer.getContext();
//        }
//    }
//}
