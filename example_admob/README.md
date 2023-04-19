# Classic AdMob implementation

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

[*Example*](src/main/java/io/bidmachine/examples/MainActivity.java#L100)

## Banner implementation
Server configuration sample:
```json
{
    "coppa": "true",
    "logging_enabled": "true",
    "test_mode": "true",
    "subject_to_gdpr": "true",
    "has_consent": "true",
    "consent_string": "YOUR_GDPR_CONSENT_STRING",
    "user_id": "YOUR_USER_ID",
    "gender": "F",
    "yob": "2000",
    "keywords": "Keyword_1,Keyword_2,Keyword_3,Keyword_4",
    "country": "YOUR_COUNTRY",
    "city": "YOUR_CITY",
    "zip": "YOUR_ZIP",
    "sturl": "https://store_url.com",
    "store_cat": "YOUR_STORE_CATEGORY",
    "store_subcat": "YOUR_STORE_SUB_CATEGORY_1,YOUR_STORE_SUB_CATEGORY_2",
    "fmw_name": "YOUR_FRAMEWORK_NAME",
    "paid": "true",
    "bcat": "IAB-1,IAB-3,IAB-5",
    "badv": "https://domain_1.com,https://domain_2.org",
    "bapps": "com.test.application_1,com.test.application_2,com.test.application_3",
    "price_floors": [{
            "id_1": 300.06
        }, {
            "id_2": 1000
        },
        302.006,
        1002
    ],
    "pubid": "YOUR_PUBLISHER_ID",
    "pubname": "YOUR_PUBLISHER_NAME",
    "pubdomain": "YOUR_PUBLISHER_DOMAIN",
    "pubcat": "YOUR_PUBLISHER_CATEGORIES_1,YOUR_PUBLISHER_CATEGORIES_2"
}
```

Local configuration sample:
```java
private void loadBanner() {
    Bundle bundle = new BidMachineBundleBuilder()
            .setLoggingEnabled(true)
            .setTestMode(true)
            .build();

    // Set bundle to mediation banner ad adapter
    AdRequest adRequest = new AdRequest.Builder()
            .addNetworkExtrasBundle(BidMachineAdapter.class, bundle)
            .build();
    
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
[*Example*](src/main/java/io/bidmachine/examples/MainActivity.java#L129)

## MREC implementation
Server configuration sample:
```json
{
    "coppa": "true",
    "logging_enabled": "true",
    "test_mode": "true",
    "subject_to_gdpr": "true",
    "has_consent": "true",
    "consent_string": "YOUR_GDPR_CONSENT_STRING",
    "user_id": "YOUR_USER_ID",
    "gender": "F",
    "yob": "2000",
    "keywords": "Keyword_1,Keyword_2,Keyword_3,Keyword_4",
    "country": "YOUR_COUNTRY",
    "city": "YOUR_CITY",
    "zip": "YOUR_ZIP",
    "sturl": "https://store_url.com",
    "store_cat": "YOUR_STORE_CATEGORY",
    "store_subcat": "YOUR_STORE_SUB_CATEGORY_1,YOUR_STORE_SUB_CATEGORY_2",
    "fmw_name": "YOUR_FRAMEWORK_NAME",
    "paid": "true",
    "bcat": "IAB-1,IAB-3,IAB-5",
    "badv": "https://domain_1.com,https://domain_2.org",
    "bapps": "com.test.application_1,com.test.application_2,com.test.application_3",
    "price_floors": [{
            "id_1": 300.06
        }, {
            "id_2": 1000
        },
        302.006,
        1002
    ],
    "pubid": "YOUR_PUBLISHER_ID",
    "pubname": "YOUR_PUBLISHER_NAME",
    "pubdomain": "YOUR_PUBLISHER_DOMAIN",
    "pubcat": "YOUR_PUBLISHER_CATEGORIES_1,YOUR_PUBLISHER_CATEGORIES_2"
}
```

Local configuration sample:
```java
private void loadMrec() {
    Bundle bundle = new BidMachineBundleBuilder()
            .setLoggingEnabled(true)
            .setTestMode(true)
            .build();

    // Set bundle to mediation MREC ad adapter
    AdRequest adRequest = new AdRequest.Builder()
            .addNetworkExtrasBundle(BidMachineAdapter.class, bundle)
            .build();
    
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
[*Example*](src/main/java/io/bidmachine/examples/MainActivity.java#L189)

## Interstitial implementation
Server configuration sample:
```json
{
    "coppa": "true",
    "logging_enabled": "true",
    "test_mode": "true",
    "subject_to_gdpr": "true",
    "has_consent": "true",
    "consent_string": "YOUR_CONSENT_STRING",
    "ad_content_type": "All",
    "user_id": "YOUR_USER_ID",
    "gender": "M",
    "yob": "1990",
    "keywords": "Keyword_1,Keyword_2,Keyword_3,Keyword_4",
    "country": "YOUR_COUNTRY",
    "city": "YOUR_CITY",
    "zip": "YOUR_ZIP",
    "sturl": "https://store_url.com",
    "store_cat": "YOUR_STORE_CATEGORY",
    "store_subcat": "YOUR_STORE_SUB_CATEGORY_1,YOUR_STORE_SUB_CATEGORY_2",
    "fmw_name": "YOUR_FRAMEWORK_NAME",
    "paid": "true",
    "bcat": "IAB-1,IAB-3,IAB-5",
    "badv": "https://domain_1.com,https://domain_2.org",
    "bapps": "com.test.application_1,com.test.application_2,com.test.application_3",
    "price_floors": [{
            "id_1": 300.06
        }, {
            "id_2": 1000
        },
        302.006,
        1002
    ],
    "pubid": "YOUR_PUBLISHER_ID",
    "pubname": "YOUR_PUBLISHER_NAME",
    "pubdomain": "YOUR_PUBLISHER_DOMAIN",
    "pubcat": "YOUR_PUBLISHER_CATEGORIES_1,YOUR_PUBLISHER_CATEGORIES_2"
}
```

Local configuration sample:
```java
private void loadInterstitial() {
    Bundle bundle = new BidMachineBundleBuilder()
            .setLoggingEnabled(true)
            .setTestMode(true)
            .setAdContentType(AdContentType.All)
            .build();

    // Set bundle to mediation interstitial ad adapter
    AdRequest adRequest = new AdRequest.Builder()
            .addNetworkExtrasBundle(BidMachineAdapter.class, bundle)
            .build();
    
    // Load InterstitialAd
    InterstitialAd.load(this, INTERSTITIAL_ID, adRequest, new InterstitialLoadListener());
}
```
[*Example*](src/main/java/io/bidmachine/examples/MainActivity.java#L249)

## Rewarded implementation
Server configuration sample:
```json
{
    "coppa": "true",
    "logging_enabled": "true",
    "test_mode": "true",
    "subject_to_gdpr": "true",
    "has_consent": "true",
    "consent_string": "YOUR_GDPR_CONSENT_STRING",
    "user_id": "YOUR_USER_ID",
    "gender": "F",
    "yob": "2000",
    "keywords": "Keyword_1,Keyword_2,Keyword_3,Keyword_4",
    "country": "YOUR_COUNTRY",
    "city": "YOUR_CITY",
    "zip": "YOUR_ZIP",
    "sturl": "https://store_url.com",
    "store_cat": "YOUR_STORE_CATEGORY",
    "store_subcat": "YOUR_STORE_SUB_CATEGORY_1,YOUR_STORE_SUB_CATEGORY_2",
    "fmw_name": "YOUR_FRAMEWORK_NAME",
    "paid": "true",
    "bcat": "IAB-1,IAB-3,IAB-5",
    "badv": "https://domain_1.com,https://domain_2.org",
    "bapps": "com.test.application_1,com.test.application_2,com.test.application_3",
    "price_floors": [{
            "id_1": 300.06
        }, {
            "id_2": 1000
        },
        302.006,
        1002
    ],
    "pubid": "YOUR_PUBLISHER_ID",
    "pubname": "YOUR_PUBLISHER_NAME",
    "pubdomain": "YOUR_PUBLISHER_DOMAIN",
    "pubcat": "YOUR_PUBLISHER_CATEGORIES_1,YOUR_PUBLISHER_CATEGORIES_2"
}
```

Local configuration sample:
```java
private void loadRewarded() {
    Bundle bundle = new BidMachineBundleBuilder()
            .setLoggingEnabled(true)
            .setTestMode(true)
            .build();
    
    // Set bundle to mediation rewarded video ad adapter
    AdRequest adRequest = new AdRequest.Builder()
            .addNetworkExtrasBundle(BidMachineAdapter.class, bundle)
            .build();
    
    // Load RewardedAd
    RewardedAd.load(this, REWARDED_ID, adRequest, new RewardedLoadListener());
}
```
[*Example*](src/main/java/io/bidmachine/examples/MainActivity.java#L304)

## Native implementation
Server configuration sample:
```json
{
    "coppa": "true",
    "logging_enabled": "true",
    "test_mode": "true",
    "subject_to_gdpr": "true",
    "has_consent": "true",
    "consent_string": "YOUR_GDPR_CONSENT_STRING",
    "media_asset_types": "Icon,Video",
    "user_id": "YOUR_USER_ID",
    "gender": "F",
    "yob": "2000",
    "keywords": "Keyword_1,Keyword_2,Keyword_3,Keyword_4",
    "country": "YOUR_COUNTRY",
    "city": "YOUR_CITY",
    "zip": "YOUR_ZIP",
    "sturl": "https://store_url.com",
    "store_cat": "YOUR_STORE_CATEGORY",
    "store_subcat": "YOUR_STORE_SUB_CATEGORY_1,YOUR_STORE_SUB_CATEGORY_2",
    "fmw_name": "YOUR_FRAMEWORK_NAME",
    "paid": "true",
    "bcat": "IAB-1,IAB-3,IAB-5",
    "badv": "https://domain_1.com,https://domain_2.org",
    "bapps": "com.test.application_1,com.test.application_2,com.test.application_3",
    "price_floors": [{
            "id_1": 300.06
        }, {
            "id_2": 1000
        },
        302.006,
        1002
    ],
    "pubid": "YOUR_PUBLISHER_ID",
    "pubname": "YOUR_PUBLISHER_NAME",
    "pubdomain": "YOUR_PUBLISHER_DOMAIN",
    "pubcat": "YOUR_PUBLISHER_CATEGORIES_1,YOUR_PUBLISHER_CATEGORIES_2"
}
```

Local configuration sample:
```java
private void loadNative() {
    Bundle bundle = new BidMachineBundleBuilder()
            .setLoggingEnabled(true)
            .setTestMode(true)
            .setMediaAssetTypes(MediaAssetType.Icon, MediaAssetType.Video)
            .build();
    
    // Set bundle to mediation native ad adapter
    AdRequest adRequest = new AdRequest.Builder()
            .addNetworkExtrasBundle(BidMachineAdapter.class, bundle)
            .build();
    
    // Create new AdLoader instance and load
    NativeListener nativeListener = new NativeListener();
    AdLoader adLoader = new AdLoader.Builder(this, NATIVE_ID)
            .forNativeAd(nativeListener)
            .withAdListener(nativeListener)
            .build();
    adLoader.loadAd(adRequest);
}
```
[*Example*](src/main/java/io/bidmachine/examples/MainActivity.java#L358)

## Utils
For convenience, the [BidMachineBundleBuilder](../bidmachine_android_admob/src/main/java/com/google/ads/mediation/bidmachine/BidMachineBundleBuilder.java) was created. With what you can pass the parameters to the loader of the ad.
```java
Bundle bundle = new BidMachineBundleBuilder()
            .setCoppa(true)
            .setLoggingEnabled(true)
            .setTestMode(true)
            .setSubjectToGDPR(true)
            .setConsentConfig(true, "YOUR_CONSENT_STRING")
            .setMediaAssetTypes(MediaAssetType.Icon, MediaAssetType.Video)
            .setUserId("YOUR_USER_ID")
            .setGender(Gender.Male)
            .setYob(1990)
            .setKeywords("Keyword_1,Keyword_2,Keyword_3,Keyword_4")
            .setCountry("YOUR_COUNTRY")
            .setCity("YOUR_CITY")
            .setZip("YOUR_ZIP")
            .setSturl("https://store_url.com")
            .setStoreCategory("YOUR_STORE_CATEGORY")
            .setStoreSubCategories("YOUR_STORE_SUB_CATEGORY_1,YOUR_STORE_SUB_CATEGORY_2")
            .setFrameworkName(Framework.UNITY)
            .setPaid(true)
            .setBcat("IAB-1,IAB-3,IAB-5")
            .setBadv("https://domain_1.com,https://domain_2.org")
            .setBapps("com.test.application_1,com.test.application_2,com.test.application_3")
            .setPriceFloors(priceFloors)
            .setPublisherId("YOUR_PUBLISHER_ID")
            .setPublisherName("YOUR_PUBLISHER_NAME")
            .setPublisherDomain("YOUR_PUBLISHER_DOMAIN")
            .setPublisherCategories("YOUR_PUBLISHER_CATEGORIES_1,YOUR_PUBLISHER_CATEGORIES_2")
            .build();

AdRequest adRequest = new AdRequest.Builder()
        .addNetworkExtrasBundle(BidMachineAdapter.class, bundle)
        .build();
```