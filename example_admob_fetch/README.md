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

Before requesting ad, you need to initialize the SDKs.

### Waterfall

Wait for AdMob ```OnInitializationCompleteListener``` to fire. This mean SDK is initialized.

Java:

```java
MobileAds.initialize(context, new OnInitializationCompleteListener() {
    @Override
    public void onInitializationComplete (InitializationStatus initializationStatus){
        // Optionally, enable test mode and logging
        BidMachine.setTestMode(true);
        BidMachine.setLoggingEnabled(true);
    }
});
```

Kotlin:

```kotlin
MobileAds.initialize(context) {
    // Optionally, enable test mode and logging
    BidMachine.setTestMode(true)
    BidMachine.setLoggingEnabled(true)
}
```

### Prebid

1. Wait for BidMachine ```InitializationCallback``` and AdMob ```OnInitializationCompleteListener``` to fire. This mean
   SDKs are initialized.

2. Make ad requests.

> **Warning** If BidMachine is not initialized, then ads will not be loaded!

Java:

```java
BidMachine.setTestMode(true);
BidMachine.setLoggingEnabled(true);
BidMachine.initialize(context, BID_MACHINE_SOURCE_ID, new InitializationCallback() {
    @Override
    public void onInitialized () {
        MobileAds.initialize(context, new OnInitializationCompleteListener() {
            @Override
            public void onInitializationComplete(InitializationStatus initializationStatus) {
                isAdMobInitialized.set(true);
            }
        });
    }
});
```

Kotlin:

```kotlin
BidMachine.setTestMode(true)
BidMachine.setLoggingEnabled(true)
BidMachine.initialize(context, BID_MACHINE_SOURCE_ID) {
    MobileAds.initialize(context) {
        isAdMobInitialized.set(true)
        listener.onInitializationComplete(it)
    }
}
```

[*Example*](src/main/java/io/bidmachine/example/integration/adapter/PrebidAdIntegrationAdapter.kt#L35)

## Banner Implementation

### Waterfall

No action required.

### Prebid

Java:

```java
private void loadBanner() {
    // Create new BidMachine request
    BannerRequest bannerRequest = new BannerRequest.Builder()
            .setSize(BannerSize.Size_320x50)
            .setListener(new BannerRequest.AdRequestListener() {
                @Override
                public void onRequestSuccess(@NonNull BannerRequest bannerRequest,
                                             @NonNull AuctionResult auctionResult) {
                    // Create AdRequest
                    AdRequest adRequest = BidMachineUtils.createAdRequest(bannerRequest);
                    // Load AdMob Banner
                    loadAdMobBanner(adRequest);
                }
            })
            .build();
    // Request an ad from BidMachine without loading it
    bannerRequest.request(this);
}
```

Kotlin:

```kotlin
private fun loadBanner() {
// Create new BidMachine request
val bannerRequest = BidMachineAdRequestBuilderFactory.createBannerRequestBuilder()
        .setSize(BannerSize.Size_320x50)
        .setListener(object : BannerRequest.AdRequestListener {
            override fun onRequestSuccess(bannerRequest: BannerRequest, auctionResult: AuctionResult) {
                // Create AdRequest
                val adRequest = BidMachineUtils.createAdRequest(bannerRequest)
                // Load AdMob Banner
                loadAdMobMrec(adRequest)
            }
        })
        .build().also {
            // Request an ad from BidMachine without loading it
            it.request(context)
        }
}
```

[*Example*](src/main/java/io/bidmachine/example/integration/PrebidAdIntegrationAdapter.kt#L54)

## MREC Implementation

### Waterfall

No action required.

### Prebid

Java:

```java
private void loadMrec() {
    // Create new BidMachine request
    BannerRequest bannerRequest = new BannerRequest.Builder()
            .setSize(BannerSize.Size_300x250)
            .setListener(new BannerRequest.AdRequestListener() {
                @Override
                public void onRequestSuccess(@NonNull BannerRequest bannerRequest,
                                             @NonNull AuctionResult auctionResult) {
                    // Create AdRequest
                    AdRequest adRequest = BidMachineUtils.createAdRequest(bannerRequest);
                    // Load AdMob MREC
                    loadAdMobMrec(adRequest);
                }
            })
            .build();
    // Request an ad from BidMachine without loading it
    bannerRequest.request(this);
}
```

Kotlin:

```kotlin
private fun loadMrec() {
    // Create new BidMachine request
    val bannerRequest = BidMachineAdRequestBuilderFactory.createBannerRequestBuilder()
            .setSize(BannerSize.Size_300x250)
            .setListener(object : BannerRequest.AdRequestListener {
                override fun onRequestSuccess(bannerRequest: BannerRequest, auctionResult: AuctionResult) {
                    // Create AdRequest
                    val adRequest = BidMachineUtils.createAdRequest(bannerRequest)
                    // Load AdMob MREC
                    loadAdMobMrec(adRequest)
                }
            })
            .build().also {
                // Request an ad from BidMachine without loading it
                it.request(context)
            }
}
```

[*Example*](src/main/java/io/bidmachine/example/integration/PrebidAdIntegrationAdapter.kt#L92)

## Interstitial Implementation

### Waterfall

No action required.

### Prebid

Java:

```java
private void loadInterstitial() {
    // Create new BidMachine request
    InterstitialRequest interstitialRequest = new InterstitialRequest.Builder()
            .setListener(new InterstitialRequest.AdRequestListener() {
                @Override
                public void onRequestSuccess(@NonNull InterstitialRequest interstitialRequest,
                                             @NonNull AuctionResult auctionResult) {
                    // Create AdRequest
                    AdRequest adRequest = BidMachineUtils.createAdRequest(interstitialRequest);
                    // Load AdMob Interstitial
                    loadAdMobInterstitial(adRequest);
                }
            })
            .build();
    // Request an ad from BidMachine without loading it
    interstitialRequest.request(this);
}
```

Kotlin:

```kotlin
private fun loadInterstitial() {
    // Create new BidMachine request
    val interstitialRequest = BidMachineAdRequestBuilderFactory.createInterstitialRequestBuilder()
            .setListener(object : InterstitialRequest.AdRequestListener {
                override fun onRequestSuccess(interstitialRequest: InterstitialRequest, auctionResult: AuctionResult) {
                    // Create AdRequest
                    val adRequest = BidMachineUtils.createAdRequest(interstitialRequest)
                    // Load AdMob Interstitial
                    loadAdMobInterstitial(adRequest)
                }
            })
            .build().also {
                // Request an ad from BidMachine without loading it
                it.request(context)
            }
}
```

[*Example*](src/main/java/io/bidmachine/example/integration/adapter/PrebidAdIntegrationAdapter.kt#L134)

## Rewarded Implementation

### Waterfall

No action required.

### Prebid

Java:

```java
private void loadRewarded() {
    // Create new BidMachine request
    RewardedRequest rewardedRequest = new RewardedRequest.Builder()
            .setListener(new RewardedRequest.AdRequestListener() {
                @Override
                public void onRequestSuccess(@NonNull RewardedRequest rewardedRequest,
                                             @NonNull AuctionResult auctionResult) {
                    // Create AdRequest
                    AdRequest adRequest = BidMachineUtils.createAdRequest(rewardedRequest);
                    // Load AdMob Rewarded
                    loadAdMobRewarded(adRequest);
                }
            })
            .build();
    // Request an ad from BidMachine without loading it
    rewardedRequest.request(this);
}
```

Kotlin:

```kotlin
private fun loadRewarded() {
    // Create new BidMachine request
    val rewardedRequest = BidMachineAdRequestBuilderFactory.createRewardedRequestBuilder()
            .setListener(object : RewardedRequest.AdRequestListener {
                override fun onRequestSuccess(rewardedRequest: RewardedRequest, auctionResult: AuctionResult) {
                    // Create AdRequest
                    val adRequest = BidMachineUtils.createAdRequest(rewardedRequest)
                    // Load AdMob Rewarded
                    loadAdMobRewarded(adRequest)
                }
            })
            .build().also {
                // Request an ad from BidMachine without loading it
                it.request(context)
            }
}
```

[*Example*](src/main/java/io/bidmachine/example/integration/adapter/PrebidAdIntegrationAdapter.kt#L178)

## Native Implementation

### Waterfall

No action required.

### Prebid

Java:

```java
private void loadNative() {
    // Create new BidMachine request
    NativeRequest nativeRequest = BidMachineAdRequestBuilderFactory.createNativeRequestBuilder()
            .setListener(new NativeRequest.AdRequestListener() {
                @Override
                public void onRequestSuccess(@NonNull NativeRequest nativeRequest,
                                             @NonNull AuctionResult auctionResult) {
                    // Create AdRequest
                    AdRequest adRequest = BidMachineUtils.createAdRequest(nativeRequest);
                    // Load AdMob Native
                    loadAdMobNative(adRequest);
                }
            })
            .build();
    // Request an ad from BidMachine without loading it
    nativeRequest.request(this);
}
```

Kotlin:

```kotlin
private void loadNative() {
    // Create new BidMachine request
    val nativeRequest = BidMachineAdRequestBuilderFactory.createNativeRequestBuilder()
            .setListener(object : NativeRequest.AdRequestListener {
                override fun onRequestSuccess(nativeRequest: NativeRequest, auctionResult: AuctionResult) {
                    // Create AdRequest
                    val adRequest = BidMachineUtils.createAdRequest(nativeRequest)
                    // Load AdMob Native
                    loadAdMobNative(adRequest)
                }
            })
            .build().also {
                // Request an ad from BidMachine without loading it
                it.request(context)
            }
}
```

[*Example*](src/main/java/io/bidmachine/example/integration/adapter/PrebidAdIntegrationAdapter.kt#L219)

## Utils

Ways to set up AdRequest by BidMachine AdRequest:

* Create new AdRequest instance:
  Java:
    ```java
    AdRequest adRequest = BidMachineUtils.createAdRequest(bidMachineAdRequest);
    ```

  Kotlin:
    ```kotlin
    val adRequest = BidMachineUtils.createAdRequest(bidMachineAdRequest)
    ```

* Create new AdRequest.Builder instance:
  Java:
    ```java
    AdRequest.Builder adRequestBuilder = BidMachineUtils.createAdRequestBuilder(bidMachineAdRequest);
    ```

  Kotlin:
    ```kotlin
    val adRequestBuilder = BidMachineUtils.createAdRequestBuilder(bidMachineAdRequest)
    ```

* Fill existing AdRequest.Builder by BidMachine AdRequest:
  Java:
    ```java
   BidMachineUtils.appendRequest(adRequestBuilder, bidMachineAdRequest);
    ```

  Kotlin:
   ```kotlin
   BidMachineUtils.appendRequest(adRequestBuilder, bidMachineAdRequest)
   ```