# HeaderBidding AdMob implementation

* [Useful links](#useful-links)
* [Initializing SDKs](#initializing-sdks)
* [Banner implementation](#banner-implementation)
* [MREC implementation](#mrec-implementation)
* [Interstitial implementation](#interstitial-implementation)
* [Rewarded implementation](#rewarded-implementation)
* [Native implementation](#native-implementation)
* [Utils](#utils)

## Useful links
* [AdMob documentation](https://developers.google.com/admob/android/quick-start)

## Initializing SDKs
1) Before requesting ad, you need to initialize the SDKs.
2) Wait for BidMachine ```InitializationCallback``` and AdMob ```OnInitializationCompleteListener``` to fire. This mean SDKs are initialized.
```java
BidMachine.initialize(this, BID_MACHINE_SELLER_ID, new InitializationCallback() {
    @Override
    public void onInitialized() {
        // BidMachine is initialized
    }
});

MobileAds.initialize(this, new OnInitializationCompleteListener() {
    @Override
    public void onInitializationComplete(@NonNull InitializationStatus initializationStatus) {
        // AdMob is initialized
    }
});
```
3) Make ad requests.

> **Warning** If BidMachine is not initialized, then ads will not be loaded!

[*Example*](src/main/java/io/bidmachine/example/MainActivity.java#L109)

## Banner implementation
```java
private void loadBanner() {
    // Create new BidMachine request
    BannerRequest bannerRequest = new BannerRequest.Builder()
            .setSize(...)
            .setListener(new BannerRequest.AdRequestListener() {
                @Override
                public void onRequestSuccess(@NonNull BannerRequest bannerRequest,
                                             @NonNull AuctionResult auctionResult) {
                    runOnUiThread(() -> loadAdMobBanner(bannerRequest));
                }
            })
            .build();

    // Request an ad from BidMachine without loading it
    bannerRequest.request(this);
}

private void loadAdMobBanner(@NonNull BannerRequest bannerRequest) {
    // Create AdRequest
    AdRequest adRequest = BidMachineUtils.createAdRequest(bannerRequest);

    // Create new AdView instance and load it
    AdView adView = new AdView(this);
    adView.setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,
                                                      ViewGroup.LayoutParams.MATCH_PARENT));
    adView.setAdUnitId(BANNER_ID);
    adView.setAdSize(AdSize.BANNER);
    adView.setAdListener(new BannerViewListener());
    adView.loadAd(adRequest);
}
```
[*Example*](src/main/java/io/bidmachine/example/MainActivity.java#L138)

## MREC implementation
```java
private void loadMrec() {
    // Create new BidMachine request
    BannerRequest bannerRequest = new BannerRequest.Builder()
            .setSize(BannerSize.Size_300x250)
            .setListener(new BannerRequest.AdRequestListener() {
                @Override
                public void onRequestSuccess(@NonNull BannerRequest bannerRequest,
                                             @NonNull AuctionResult auctionResult) {
                    runOnUiThread(() -> loadAdMobMrec(bannerRequest));
                }
            })
            .build();

    // Request an ad from BidMachine without loading it
    bannerRequest.request(this);
}

private void loadAdMobMrec(@NonNull BannerRequest bannerRequest) {
    // Create AdRequest
    AdRequest adRequest = BidMachineUtils.createAdRequest(bannerRequest);

    // Create new AdView instance and load it
    AdView adView = new AdView(this);
    adView.setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,
                                                      ViewGroup.LayoutParams.MATCH_PARENT));
    adView.setAdUnitId(MREC_ID);
    adView.setAdSize(AdSize.MEDIUM_RECTANGLE);
    adView.setAdListener(new MrecViewListener());
    adView.loadAd(adRequest);
}
```
[*Example*](src/main/java/io/bidmachine/example/MainActivity.java#L232)

## Interstitial implementation
```java
private void loadInterstitial() {
    // Create new BidMachine request
    InterstitialRequest interstitialRequest = new InterstitialRequest.Builder()
            .setAdContentType(...)
            .setListener(new InterstitialRequest.AdRequestListener() {
                @Override
                public void onRequestSuccess(@NonNull InterstitialRequest interstitialRequest,
                                             @NonNull AuctionResult auctionResult) {
                    runOnUiThread(() -> loadAdMobInterstitial(interstitialRequest));
                }
            })
            .build();

    // Request an ad from BidMachine without loading it
    interstitialRequest.request(this);
}

private void loadAdMobInterstitial(@NonNull InterstitialRequest interstitialRequest) {
    // Create AdRequest
    AdRequest adRequest = BidMachineUtils.createAdRequest(interstitialRequest);

    // Load InterstitialAd
    InterstitialAd.load(this, INTERSTITIAL_ID, adRequest, new InterstitialLoadListener());
}
```
[*Example*](src/main/java/io/bidmachine/example/MainActivity.java#L326)

## Rewarded implementation
```java
private void loadRewarded() {
    // Create new BidMachine request
    RewardedRequest rewardedRequest = new RewardedRequest.Builder()
            .setListener(new RewardedRequest.AdRequestListener() {
                @Override
                public void onRequestSuccess(@NonNull RewardedRequest rewardedRequest,
                                             @NonNull AuctionResult auctionResult) {
                    runOnUiThread(() -> loadAdMobRewarded(rewardedRequest));
                }
            })
            .build();

    // Request an ad from BidMachine without loading it
    rewardedRequest.request(this);
}

private void loadAdMobRewarded(@NonNull RewardedRequest rewardedRequest) {
    // Create AdRequest
    AdRequest adRequest = BidMachineUtils.createAdRequest(rewardedRequest);

    // Load RewardedAd
    RewardedAd.load(this, REWARDED_ID, adRequest, new RewardedLoadListener());
}
```
[*Example*](src/main/java/io/bidmachine/example/MainActivity.java#L413)

## Native implementation
```java
private void loadNative() {
    // Create new BidMachine request
    NativeRequest nativeRequest = new NativeRequest.Builder()
            .setListener(new NativeRequest.AdRequestListener() {
                @Override
                public void onRequestSuccess(@NonNull NativeRequest nativeRequest,
                                             @NonNull AuctionResult auctionResult) {
                    runOnUiThread(() -> loadAdMobNative(nativeRequest));
                }
            })
            .build();

    // Request an ad from BidMachine without loading it
    nativeRequest.request(this);
}

private void loadAdMobNative(@NonNull NativeRequest nativeRequest) {
    // Create AdRequest
    AdRequest adRequest = BidMachineUtils.createAdRequest(nativeRequest);

    // Create new AdLoader instance and load
    NativeListener nativeListener = new NativeListener();
    AdLoader adLoader = new AdLoader.Builder(this, NATIVE_ID)
            .forNativeAd(nativeListener)
            .withAdListener(nativeListener)
            .build();
    adLoader.loadAd(adRequest);
}
```
[*Example*](src/main/java/io/bidmachine/example/MainActivity.java#L500)

## Utils
Ways to set up AdRequest by BidMachine AdRequest:
1. Create new AdRequest instance
```java
    AdRequest adRequest = BidMachineUtils.createAdRequest(bidMachineAdRequest);
```
2. Create new AdRequest.Builder instance
```java
    AdRequest.Builder adRequestBuilder = BidMachineUtils.createAdRequestBuilder(bidMachineAdRequest);
```
3. Fill existing AdRequest.Builder by BidMachine AdRequest
```java
    BidMachineUtils.appendRequest(adRequestBuilder, bidMachineAdRequest);
```