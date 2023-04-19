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

import com.google.ads.mediation.bidmachine.BidMachineAdapter;
import com.google.ads.mediation.bidmachine.BidMachineBundleBuilder;
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

import io.bidmachine.AdContentType;
import io.bidmachine.BidMachine;
import io.bidmachine.MediaAssetType;
import io.bidmachine.examples.databinding.ActivityMainBinding;

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

    private AdView bannerAdView;
    private AdView mrecAdView;
    private InterstitialAd interstitialAd;
    private RewardedAd rewardedAd;
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
                              () -> MobileAds.initialize(MainActivity.this, initializationStatus -> {
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
     * Method for load AdView
     */
    private void loadBanner() {
        binding.bShowBanner.setEnabled(false);

        // Destroy previous AdView
        destroyBanner();

        // Prepare bundle for set to AdRequest
        Bundle bundle = new BidMachineBundleBuilder()
                .setLoggingEnabled(true)
                .setTestMode(true)
                .build();

        // Set bundle to mediation banner ad adapter
        AdRequest adRequest = new AdRequest.Builder()
                .addNetworkExtrasBundle(BidMachineAdapter.class, bundle)
                .build();

        // Create new AdView instance and load it
        bannerAdView = new AdView(this);
        bannerAdView.setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,
                                                                ViewGroup.LayoutParams.MATCH_PARENT));
        bannerAdView.setAdUnitId(BANNER_ID);
        bannerAdView.setAdSize(AdSize.BANNER);
        bannerAdView.setAdListener(new BannerViewListener());
        bannerAdView.loadAd(adRequest);

        Log.d(TAG, "loadBanner");
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
    }

    /**
     * Method for load AdView
     */
    private void loadMrec() {
        binding.bShowMrec.setEnabled(false);

        // Destroy previous AdView
        destroyMrec();

        // Prepare bundle for set to AdRequest
        Bundle bundle = new BidMachineBundleBuilder()
                .setLoggingEnabled(true)
                .setTestMode(true)
                .build();

        // Set bundle to mediation MREC ad adapter
        AdRequest adRequest = new AdRequest.Builder()
                .addNetworkExtrasBundle(BidMachineAdapter.class, bundle)
                .build();

        // Create new AdView instance and load it
        mrecAdView = new AdView(this);
        mrecAdView.setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,
                                                              ViewGroup.LayoutParams.MATCH_PARENT));
        mrecAdView.setAdUnitId(MREC_ID);
        mrecAdView.setAdSize(AdSize.MEDIUM_RECTANGLE);
        mrecAdView.setAdListener(new MrecViewListener());
        mrecAdView.loadAd(adRequest);

        Log.d(TAG, "loadMrec");
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
    }

    /**
     * Method for load InterstitialAd
     */
    private void loadInterstitial() {
        binding.bShowInterstitial.setEnabled(false);

        // Destroy previous InterstitialAd
        destroyInterstitial();

        // Prepare bundle for set to AdRequest
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

        Log.d(TAG, "loadInterstitial");
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
    }

    /**
     * Method for load RewardedAd
     */
    private void loadRewarded() {
        binding.bShowRewarded.setEnabled(false);

        // Destroy previous RewardedAd
        destroyRewarded();

        // Prepare bundle for set to AdRequest
        Bundle bundle = new BidMachineBundleBuilder()
                .setLoggingEnabled(true)
                .setTestMode(true)
                .build();

        // Set bundle to mediation rewarded ad adapter
        AdRequest adRequest = new AdRequest.Builder()
                .addNetworkExtrasBundle(BidMachineAdapter.class, bundle)
                .build();

        // Load RewardedAd
        RewardedAd.load(this, REWARDED_ID, adRequest, new RewardedLoadListener());

        Log.d(TAG, "loadRewarded");
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
    }

    /**
     * Method for load NativeAd
     */
    private void loadNative() {
        binding.bShowNative.setEnabled(false);

        // Destroy previous NativeAd
        destroyNative();

        // Prepare bundle for set to AdRequest
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

        Log.d(TAG, "loadNative");
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