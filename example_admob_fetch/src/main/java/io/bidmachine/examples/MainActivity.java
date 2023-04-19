package io.bidmachine.examples;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.google.ads.mediation.bidmachine.BidMachineUtils;
import com.google.android.gms.ads.AdError;
import com.google.android.gms.ads.AdListener;
import com.google.android.gms.ads.AdLoader;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdSize;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.ads.FullScreenContentCallback;
import com.google.android.gms.ads.LoadAdError;
import com.google.android.gms.ads.MobileAds;
import com.google.android.gms.ads.OnUserEarnedRewardListener;
import com.google.android.gms.ads.interstitial.InterstitialAd;
import com.google.android.gms.ads.interstitial.InterstitialAdLoadCallback;
import com.google.android.gms.ads.nativead.MediaView;
import com.google.android.gms.ads.nativead.NativeAd;
import com.google.android.gms.ads.nativead.NativeAdView;
import com.google.android.gms.ads.rewarded.RewardItem;
import com.google.android.gms.ads.rewarded.RewardedAd;
import com.google.android.gms.ads.rewarded.RewardedAdLoadCallback;

import io.bidmachine.BidMachine;
import io.bidmachine.banner.BannerRequest;
import io.bidmachine.banner.BannerSize;
import io.bidmachine.examples.databinding.ActivityMainBinding;
import io.bidmachine.interstitial.InterstitialRequest;
import io.bidmachine.models.AuctionResult;
import io.bidmachine.nativead.NativeRequest;
import io.bidmachine.rewarded.RewardedRequest;
import io.bidmachine.utils.BMError;

public class MainActivity extends AppCompatActivity {

    private static final String TAG = MainActivity.class.getSimpleName();
    private static final String BID_MACHINE_SELLER_ID = "5";
    private static final String BANNER_ID = "YOUR_BANNER_ID";
    private static final String MREC_ID = "YOUR_MREC_ID";
    private static final String INTERSTITIAL_ID = "YOUR_INTERSTITIAL_ID";
    private static final String REWARDED_ID = "YOUR_REWARDED_ID";
    private static final String NATIVE_ID = "YOUR_NATIVE_ID";

    private static boolean isAdMobInitialized = false;

    private ActivityMainBinding binding;

    private BannerRequest bannerRequest;
    private AdView bannerAdView;
    private BannerRequest mrecRequest;
    private AdView mrecAdView;
    private InterstitialRequest interstitialRequest;
    private InterstitialAd interstitialAd;
    private RewardedRequest rewardedRequest;
    private RewardedAd rewardedAd;
    private NativeRequest nativeRequest;
    private NativeAd nativeAd;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        binding.bInitialize.setOnClickListener(v -> initialize());
        binding.bLoadBanner.setOnClickListener(v -> loadBanner());
        binding.bShowBanner.setOnClickListener(v -> showBanner());
        binding.bLoadMrec.setOnClickListener(v -> loadMrec());
        binding.bShowMrec.setOnClickListener(v -> showMrec());
        binding.bLoadInterstitial.setOnClickListener(v -> loadInterstitial());
        binding.bShowInterstitial.setOnClickListener(v -> showInterstitial());
        binding.bLoadRewarded.setOnClickListener(v -> loadRewarded());
        binding.bShowRewarded.setOnClickListener(v -> showRewarded());
        binding.bLoadNative.setOnClickListener(v -> loadNative());
        binding.bShowNative.setOnClickListener(v -> showNative());

        if (isAdMobInitialized) {
            binding.bInitialize.setEnabled(false);
            enableButton();
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();

        destroyBanner();
        destroyMrec();
        destroyInterstitial();
        destroyRewarded();
        destroyNative();
    }

    private void initialize() {
        // Initialize BidMachine SDK first
        BidMachine.setTestMode(true);
        BidMachine.setLoggingEnabled(true);
        BidMachine.initialize(this, BID_MACHINE_SELLER_ID,
                              () -> MobileAds.initialize(this, initializationStatus -> {
                                  isAdMobInitialized = true;

                                  binding.bInitialize.setEnabled(false);
                                  enableButton();
                              }));
    }

    private void enableButton() {
        binding.bLoadBanner.setEnabled(true);
        binding.bLoadMrec.setEnabled(true);
        binding.bLoadInterstitial.setEnabled(true);
        binding.bLoadRewarded.setEnabled(true);
        binding.bLoadNative.setEnabled(true);
    }

    private void addAdView(View view) {
        binding.adContainer.removeAllViews();
        binding.adContainer.addView(view);
    }

    /**
     * Method for load BannerRequest
     */
    private void loadBanner() {
        binding.bShowBanner.setEnabled(false);

        // Destroy previous AdView
        destroyBanner();

        // Create new BidMachine request
        bannerRequest = new BannerRequest.Builder()
                .setSize(BannerSize.Size_320x50)
                .setListener(new BannerRequest.AdRequestListener() {
                    @Override
                    public void onRequestSuccess(@NonNull BannerRequest bannerRequest,
                                                 @NonNull AuctionResult auctionResult) {
                        runOnUiThread(() -> loadAdMobBanner(bannerRequest));
                    }

                    @Override
                    public void onRequestFailed(@NonNull BannerRequest bannerRequest,
                                                @NonNull BMError bmError) {
                        runOnUiThread(() -> {
                            Log.d(TAG, "BannerRequestListener - onRequestFailed");
                            Toast.makeText(MainActivity.this,
                                           "BannerFetchFailed",
                                           Toast.LENGTH_SHORT).show();
                        });
                    }

                    @Override
                    public void onRequestExpired(@NonNull BannerRequest bannerRequest) {
                        //ignore
                    }
                })
                .build();

        // Request an ad from BidMachine without loading it
        bannerRequest.request(this);

        Log.d(TAG, "loadBanner");
    }

    /**
     * Method for load AdView
     */
    private void loadAdMobBanner(@NonNull BannerRequest bannerRequest) {
        Log.d(TAG, "loadAdMobBanner");

        // Create AdRequest
        AdRequest adRequest = BidMachineUtils.createAdRequest(bannerRequest);

        // Create new AdView instance and load it
        bannerAdView = new AdView(this);
        bannerAdView.setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,
                                                                ViewGroup.LayoutParams.MATCH_PARENT));
        bannerAdView.setAdUnitId(BANNER_ID);
        bannerAdView.setAdSize(AdSize.BANNER);
        bannerAdView.setAdListener(new BannerViewListener());
        bannerAdView.loadAd(adRequest);
    }

    /**
     * Method for show AdView
     */
    private void showBanner() {
        Log.d(TAG, "showBanner");

        binding.bShowBanner.setEnabled(false);

        if (bannerAdView != null && bannerAdView.getParent() == null) {
            addAdView(bannerAdView);
        } else {
            Log.d(TAG, "show error - banner object is null");
        }
    }

    /**
     * Method for destroy AdView
     */
    private void destroyBanner() {
        Log.d(TAG, "destroyBanner");

        binding.adContainer.removeAllViews();
        if (bannerAdView != null) {
            bannerAdView.destroy();
            bannerAdView = null;
        }
        if (bannerRequest != null) {
            bannerRequest.destroy();
            bannerRequest = null;
        }
    }

    /**
     * Method for load BannerRequest
     */
    private void loadMrec() {
        binding.bShowMrec.setEnabled(false);

        // Destroy previous AdView
        destroyMrec();

        // Create new BidMachine request
        mrecRequest = new BannerRequest.Builder()
                .setSize(BannerSize.Size_300x250)
                .setListener(new BannerRequest.AdRequestListener() {
                    @Override
                    public void onRequestSuccess(@NonNull BannerRequest bannerRequest,
                                                 @NonNull AuctionResult auctionResult) {
                        runOnUiThread(() -> loadAdMobMrec(bannerRequest));
                    }

                    @Override
                    public void onRequestFailed(@NonNull BannerRequest bannerRequest,
                                                @NonNull BMError bmError) {
                        runOnUiThread(() -> {
                            Log.d(TAG, "BannerRequestListener - onRequestFailed");
                            Toast.makeText(MainActivity.this,
                                           "MrecFetchFailed",
                                           Toast.LENGTH_SHORT).show();
                        });
                    }

                    @Override
                    public void onRequestExpired(@NonNull BannerRequest bannerRequest) {
                        //ignore
                    }
                })
                .build();

        // Request an ad from BidMachine without loading it
        mrecRequest.request(this);

        Log.d(TAG, "loadMrec");
    }

    /**
     * Method for load AdView
     */
    private void loadAdMobMrec(@NonNull BannerRequest bannerRequest) {
        Log.d(TAG, "loadAdMobMrec");

        // Create AdRequest
        AdRequest adRequest = BidMachineUtils.createAdRequest(bannerRequest);

        // Create new AdView instance and load it
        mrecAdView = new AdView(this);
        mrecAdView.setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,
                                                              ViewGroup.LayoutParams.MATCH_PARENT));
        mrecAdView.setAdUnitId(MREC_ID);
        mrecAdView.setAdSize(AdSize.MEDIUM_RECTANGLE);
        mrecAdView.setAdListener(new MrecViewListener());
        mrecAdView.loadAd(adRequest);
    }

    /**
     * Method for show AdView
     */
    private void showMrec() {
        Log.d(TAG, "showMrec");

        binding.bShowMrec.setEnabled(false);

        if (mrecAdView != null && mrecAdView.getParent() == null) {
            addAdView(mrecAdView);
        } else {
            Log.d(TAG, "show error - mrec object is null");
        }
    }

    /**
     * Method for destroy AdView
     */
    private void destroyMrec() {
        Log.d(TAG, "destroyMrec");

        binding.adContainer.removeAllViews();
        if (mrecAdView != null) {
            mrecAdView.destroy();
            mrecAdView = null;
        }
        if (mrecRequest != null) {
            mrecRequest.destroy();
            mrecRequest = null;
        }
    }

    /**
     * Method for load InterstitialRequest
     */
    private void loadInterstitial() {
        binding.bShowInterstitial.setEnabled(false);

        // Destroy previous InterstitialAd
        destroyInterstitial();

        // Create new BidMachine request
        interstitialRequest = new InterstitialRequest.Builder()
                .setListener(new InterstitialRequest.AdRequestListener() {
                    @Override
                    public void onRequestSuccess(@NonNull InterstitialRequest interstitialRequest,
                                                 @NonNull AuctionResult auctionResult) {
                        runOnUiThread(() -> loadAdMobInterstitial(interstitialRequest));
                    }

                    @Override
                    public void onRequestFailed(@NonNull InterstitialRequest interstitialRequest,
                                                @NonNull BMError bmError) {
                        runOnUiThread(() -> {
                            Log.d(TAG, "InterstitialRequestListener - onRequestFailed");
                            Toast.makeText(MainActivity.this,
                                           "InterstitialFetchFailed",
                                           Toast.LENGTH_SHORT).show();
                        });
                    }

                    @Override
                    public void onRequestExpired(@NonNull InterstitialRequest interstitialRequest) {
                        //ignore
                    }
                })
                .build();

        // Request an ad from BidMachine without loading it
        interstitialRequest.request(this);

        Log.d(TAG, "loadInterstitial");
    }

    /**
     * Method for load InterstitialAd
     */
    private void loadAdMobInterstitial(@NonNull InterstitialRequest interstitialRequest) {
        Log.d(TAG, "loadAdMobInterstitial");

        // Create AdRequest
        AdRequest adRequest = BidMachineUtils.createAdRequest(interstitialRequest);

        // Load InterstitialAd
        InterstitialAd.load(this, INTERSTITIAL_ID, adRequest, new InterstitialLoadListener());
    }

    /**
     * Method for show InterstitialAd
     */
    private void showInterstitial() {
        Log.d(TAG, "showInterstitial");

        binding.bShowInterstitial.setEnabled(false);

        if (interstitialAd != null) {
            interstitialAd.setFullScreenContentCallback(new InterstitialShowListener());
            interstitialAd.show(this);
        } else {
            Log.d(TAG, "show error - interstitial object not loaded");
        }
    }

    /**
     * Method for destroy InterstitialAd
     */
    private void destroyInterstitial() {
        Log.d(TAG, "destroyInterstitial");

        if (interstitialAd != null) {
            interstitialAd.setFullScreenContentCallback(null);
            interstitialAd = null;
        }
        if (interstitialRequest != null) {
            interstitialRequest.destroy();
            interstitialRequest = null;
        }
    }

    /**
     * Method for load RewardedRequest
     */
    private void loadRewarded() {
        binding.bShowRewarded.setEnabled(false);

        // Destroy previous RewardedAd
        destroyRewarded();

        // Create new BidMachine request
        rewardedRequest = new RewardedRequest.Builder()
                .setListener(new RewardedRequest.AdRequestListener() {
                    @Override
                    public void onRequestSuccess(@NonNull RewardedRequest rewardedRequest,
                                                 @NonNull AuctionResult auctionResult) {
                        runOnUiThread(() -> loadAdMobRewarded(rewardedRequest));
                    }

                    @Override
                    public void onRequestFailed(@NonNull RewardedRequest rewardedRequest,
                                                @NonNull BMError bmError) {
                        runOnUiThread(() -> {
                            Log.d(TAG, "RewardedRequestListener - onRequestFailed");
                            Toast.makeText(MainActivity.this,
                                           "RewardedFetchFailed",
                                           Toast.LENGTH_SHORT).show();
                        });
                    }

                    @Override
                    public void onRequestExpired(@NonNull RewardedRequest rewardedRequest) {
                        //ignore
                    }
                })
                .build();

        // Request an ad from BidMachine without loading it
        rewardedRequest.request(this);

        Log.d(TAG, "loadRewarded");
    }

    /**
     * Method for load RewardedAd
     */
    private void loadAdMobRewarded(@NonNull RewardedRequest rewardedRequest) {
        Log.d(TAG, "loadAdMobRewarded");

        // Create AdRequest
        AdRequest adRequest = BidMachineUtils.createAdRequest(rewardedRequest);

        // Load RewardedAd
        RewardedAd.load(this, REWARDED_ID, adRequest, new RewardedLoadListener());
    }

    /**
     * Method for show RewardedAd
     */
    private void showRewarded() {
        Log.d(TAG, "showRewarded");

        binding.bShowRewarded.setEnabled(false);

        if (rewardedAd != null) {
            rewardedAd.setFullScreenContentCallback(new RewardedShowListener());
            rewardedAd.show(this, new RewardedEarnedListener());
        } else {
            Log.d(TAG, "show error - rewarded object not loaded");
        }
    }

    /**
     * Method for destroy RewardedAd
     */
    private void destroyRewarded() {
        Log.d(TAG, "destroyRewarded");

        if (rewardedAd != null) {
            rewardedAd.setFullScreenContentCallback(null);
            rewardedAd = null;
        }
        if (rewardedRequest != null) {
            rewardedRequest.destroy();
            rewardedRequest = null;
        }
    }

    /**
     * Method for load NativeRequest
     */
    private void loadNative() {
        binding.bShowNative.setEnabled(false);

        // Destroy previous NativeAd
        destroyNative();

        // Create new BidMachine request
        nativeRequest = new NativeRequest.Builder()
                .setListener(new NativeRequest.AdRequestListener() {
                    @Override
                    public void onRequestSuccess(@NonNull NativeRequest nativeRequest,
                                                 @NonNull AuctionResult auctionResult) {
                        runOnUiThread(() -> loadAdMobNative(nativeRequest));
                    }

                    @Override
                    public void onRequestFailed(@NonNull NativeRequest nativeRequest,
                                                @NonNull BMError bmError) {
                        runOnUiThread(() -> {
                            Log.d(TAG, "NativeRequestListener - onRequestFailed");
                            Toast.makeText(MainActivity.this,
                                           "NativeFetchFailed",
                                           Toast.LENGTH_SHORT).show();
                        });
                    }

                    @Override
                    public void onRequestExpired(@NonNull NativeRequest nativeRequest) {
                        //ignore
                    }
                })
                .build();

        // Request an ad from BidMachine without loading it
        nativeRequest.request(this);

        Log.d(TAG, "loadNative");
    }

    /**
     * Method for load NativeAd
     */
    private void loadAdMobNative(@NonNull NativeRequest nativeRequest) {
        Log.d(TAG, "loadAdMobNative");

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

    /**
     * Method for show NativeAd
     */
    private void showNative() {
        Log.d(TAG, "showNative");

        binding.bShowNative.setEnabled(false);

        if (nativeAd == null) {
            Log.d(TAG, "show error - native object not loaded");
            return;
        }

        NativeAdView nativeAdView = (NativeAdView) LayoutInflater.from(this)
                .inflate(R.layout.native_ad, binding.adContainer, false);
        fillNative(nativeAdView, nativeAd);
        addAdView(nativeAdView);
    }

    /**
     * Method sets the text, images and the native ad, etc into the ad view
     *
     * @param nativeAdView container what will be filled by assets from NativeAd
     * @param nativeAd     data storage which contains title, description, etc
     */
    private void fillNative(NativeAdView nativeAdView, NativeAd nativeAd) {
        Log.d(TAG, "fillNative");

        TextView titleView = nativeAdView.findViewById(R.id.txtTitle);
        titleView.setText(nativeAd.getHeadline());

        TextView descriptionView = nativeAdView.findViewById(R.id.txtDescription);
        descriptionView.setText(nativeAd.getBody());

        float rating = nativeAd.getStarRating() != null
                ? nativeAd.getStarRating().floatValue()
                : 0;
        RatingBar ratingBar = nativeAdView.findViewById(R.id.ratingBar);
        ratingBar.setRating(rating);

        Button ctaView = nativeAdView.findViewById(R.id.btnCta);
        ctaView.setText(nativeAd.getCallToAction());

        ImageView iconView = nativeAdView.findViewById(R.id.icon);
        nativeAdView.setIconView(iconView);

        MediaView mediaView = nativeAdView.findViewById(R.id.mediaView);
        nativeAdView.setMediaView(mediaView);

        nativeAdView.setNativeAd(nativeAd);
    }

    /**
     * Method for destroy NativeAd
     */
    private void destroyNative() {
        Log.d(TAG, "destroyNative");

        if (nativeAd != null) {
            nativeAd.destroy();
            nativeAd = null;
        }
        if (nativeRequest != null) {
            nativeRequest.destroy();
            nativeRequest = null;
        }
    }


    /**
     * Class for definition behavior AdView
     */
    private class BannerViewListener extends AdListener {

        @Override
        public void onAdLoaded() {
            binding.bShowBanner.setEnabled(true);

            Log.d(TAG, "BannerViewListener - onAdLoaded");
            Toast.makeText(MainActivity.this,
                           "BannerLoaded",
                           Toast.LENGTH_SHORT).show();
        }

        @Override
        public void onAdFailedToLoad(@NonNull LoadAdError loadAdError) {
            Log.d(TAG,
                  String.format("BannerViewListener - onAdFailedToLoad with message: %s",
                                loadAdError.getMessage()));
            Toast.makeText(MainActivity.this,
                           "BannerFailedToLoad",
                           Toast.LENGTH_SHORT).show();
        }

        @Override
        public void onAdOpened() {
            Log.d(TAG, "BannerViewListener - onAdOpened");
        }

        @Override
        public void onAdImpression() {
            Log.d(TAG, "BannerViewListener - onAdImpression");
        }

        @Override
        public void onAdClicked() {
            Log.d(TAG, "BannerViewListener - onAdClicked");
        }

        @Override
        public void onAdClosed() {
            Log.d(TAG, "BannerViewListener - onAdClosed");
        }

    }

    /**
     * Class for definition behavior AdView
     */
    private class MrecViewListener extends AdListener {

        @Override
        public void onAdLoaded() {
            binding.bShowMrec.setEnabled(true);

            Log.d(TAG, "MrecViewListener - onAdLoaded");
            Toast.makeText(MainActivity.this,
                           "MrecLoaded",
                           Toast.LENGTH_SHORT).show();
        }

        @Override
        public void onAdFailedToLoad(@NonNull LoadAdError loadAdError) {
            Log.d(TAG,
                  String.format("MrecViewListener - onAdFailedToLoad with message: %s",
                                loadAdError.getMessage()));
            Toast.makeText(MainActivity.this,
                           "MrecFailedToLoad",
                           Toast.LENGTH_SHORT).show();
        }

        @Override
        public void onAdOpened() {
            Log.d(TAG, "MrecViewListener - onAdOpened");
        }

        @Override
        public void onAdImpression() {
            Log.d(TAG, "MrecViewListener - onAdImpression");
        }

        @Override
        public void onAdClicked() {
            Log.d(TAG, "MrecViewListener - onAdClicked");
        }

        @Override
        public void onAdClosed() {
            Log.d(TAG, "MrecViewListener - onAdClosed");
        }

    }

    /**
     * Class for definition behavior InterstitialAd
     */
    private class InterstitialLoadListener extends InterstitialAdLoadCallback {

        @Override
        public void onAdLoaded(@NonNull InterstitialAd interstitialAd) {
            MainActivity.this.interstitialAd = interstitialAd;

            binding.bShowInterstitial.setEnabled(true);

            Log.d(TAG, "InterstitialLoadListener - onAdLoaded");
            Toast.makeText(MainActivity.this,
                           "InterstitialLoaded",
                           Toast.LENGTH_SHORT).show();
        }

        @Override
        public void onAdFailedToLoad(@NonNull LoadAdError loadAdError) {
            Log.d(TAG,
                  String.format("InterstitialLoadListener - onAdFailedToLoad with message: %s",
                                loadAdError.getMessage()));
            Toast.makeText(MainActivity.this,
                           "InterstitialFailedToLoad",
                           Toast.LENGTH_SHORT).show();
        }

    }

    private static class InterstitialShowListener extends FullScreenContentCallback {

        @Override
        public void onAdShowedFullScreenContent() {
            Log.d(TAG, "InterstitialShowListener - onAdShowedFullScreenContent");
        }

        @Override
        public void onAdFailedToShowFullScreenContent(@NonNull AdError adError) {
            Log.d(TAG, "InterstitialShowListener - onAdFailedToShowFullScreenContent");
        }

        @Override
        public void onAdImpression() {
            Log.d(TAG, "InterstitialShowListener - onAdImpression");
        }

        @Override
        public void onAdDismissedFullScreenContent() {
            Log.d(TAG, "InterstitialShowListener - onAdDismissedFullScreenContent");
        }

    }

    /**
     * Class for definition behavior RewardedAd
     */
    private class RewardedLoadListener extends RewardedAdLoadCallback {

        @Override
        public void onAdLoaded(@NonNull RewardedAd rewardedAd) {
            MainActivity.this.rewardedAd = rewardedAd;

            binding.bShowRewarded.setEnabled(true);

            Log.d(TAG, "RewardedLoadListener - onAdLoaded");
            Toast.makeText(MainActivity.this,
                           "RewardedLoaded",
                           Toast.LENGTH_SHORT).show();
        }

        @Override
        public void onAdFailedToLoad(@NonNull LoadAdError loadAdError) {
            Log.d(TAG,
                  String.format("RewardedLoadListener - onAdFailedToLoad with message: %s",
                                loadAdError.getMessage()));
            Toast.makeText(MainActivity.this,
                           "RewardedFailedToLoad",
                           Toast.LENGTH_SHORT).show();
        }

    }

    private static class RewardedShowListener extends FullScreenContentCallback {

        @Override
        public void onAdShowedFullScreenContent() {
            Log.d(TAG, "RewardedShowListener - onAdShowedFullScreenContent");
        }

        @Override
        public void onAdFailedToShowFullScreenContent(@NonNull AdError adError) {
            Log.d(TAG, "RewardedShowListener - onAdFailedToShowFullScreenContent");
        }

        @Override
        public void onAdImpression() {
            Log.d(TAG, "RewardedShowListener - onAdImpression");
        }

        @Override
        public void onAdDismissedFullScreenContent() {
            Log.d(TAG, "RewardedShowListener - onAdDismissedFullScreenContent");
        }

    }

    private static class RewardedEarnedListener implements OnUserEarnedRewardListener {

        @Override
        public void onUserEarnedReward(@NonNull RewardItem rewardItem) {
            Log.d(TAG, "RewardedEarnedListener - onUserEarnedReward");
        }

    }

    /**
     * Class for definition behavior UnifiedNativeAd
     */
    private class NativeListener extends AdListener implements NativeAd.OnNativeAdLoadedListener {

        @Override
        public void onNativeAdLoaded(@NonNull NativeAd nativeAd) {
            MainActivity.this.nativeAd = nativeAd;

            binding.bShowNative.setEnabled(true);

            Log.d(TAG, "NativeListener - onNativeAdLoaded");
            Toast.makeText(MainActivity.this,
                           "NativeLoaded",
                           Toast.LENGTH_SHORT).show();
        }

        @Override
        public void onAdFailedToLoad(@NonNull LoadAdError loadAdError) {
            Log.d(TAG,
                  String.format("NativeListener - onAdFailedToLoad with message: %s",
                                loadAdError.getMessage()));
            Toast.makeText(MainActivity.this,
                           "NativeFailedToLoad",
                           Toast.LENGTH_SHORT).show();
        }

        @Override
        public void onAdOpened() {
            Log.d(TAG, "NativeListener - onAdOpened");
        }

        @Override
        public void onAdImpression() {
            Log.d(TAG, "NativeListener - onAdImpression");
        }

        @Override
        public void onAdClicked() {
            Log.d(TAG, "NativeListener - onAdClicked");
        }

    }

}