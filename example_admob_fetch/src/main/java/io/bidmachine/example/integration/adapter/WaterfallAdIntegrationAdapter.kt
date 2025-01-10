package io.bidmachine.example.integration.adapter

import android.content.Context
import com.google.android.gms.ads.AdRequest
import com.google.android.gms.ads.MobileAds
import com.google.android.gms.ads.initialization.OnInitializationCompleteListener
import io.bidmachine.BidMachine
import io.bidmachine.example.integration.listener.BannerAdListener
import io.bidmachine.example.integration.listener.InterstitialAdListener
import io.bidmachine.example.integration.listener.NativeAdListener
import io.bidmachine.example.integration.listener.RewardedAdListener

internal class WaterfallAdIntegrationAdapter : BaseAdIntegrationAdapter() {

    override fun initialize(context: Context, listener: OnInitializationCompleteListener) {
        MobileAds.initialize(context) {
            // Optionally, enable test mode and logging
            BidMachine.setTestMode(true)
            BidMachine.setLoggingEnabled(true)
            // Complete initialization
            isAdMobInitialized.set(true)
            listener.onInitializationComplete(it)
        }
    }

    override fun loadBanner(context: Context, listener: BannerAdListener) {
        // Create new AdRequest instance
        val adRequest = AdRequest.Builder().build()
        // Create new AdView instance and load it
        loadAdMobBanner(context, BANNER_ID, adRequest, listener)
    }

    override fun loadMrec(context: Context, listener: BannerAdListener) {
        // Create new AdRequest instance
        val adRequest = AdRequest.Builder().build()
        // Create new AdView instance and load it
        loadAdMobMrec(context, MREC_ID, adRequest, listener)
    }

    override fun loadInterstitial(context: Context, listener: InterstitialAdListener) {
        // Create new AdRequest instance
        val adRequest = AdRequest.Builder().build()
        // Load InterstitialAd
        loadAdMobInterstitial(context, INTERSTITIAL_ID, adRequest, listener)
    }

    override fun loadRewarded(context: Context, listener: RewardedAdListener) {
        // Create new AdRequest instance
        val adRequest = AdRequest.Builder().build()
        // Load RewardedAd
        loadAdMobRewarded(context, REWARDED_ID, adRequest, listener)
    }

    override fun loadNative(context: Context, listener: NativeAdListener) {
        // Create new AdRequest instance
        val adRequest = AdRequest.Builder().build()
        // Create new AdLoader instance and load
        loadAdMobNative(context, NATIVE_ID, adRequest, listener)
    }

    companion object {

        private const val BANNER_ID = "YOUR_BANNER_ID"

        private const val MREC_ID = "YOUR_MREC_ID"

        private const val INTERSTITIAL_ID = "YOUR_INTERSTITIAL_ID"

        private const val REWARDED_ID = "YOUR_REWARDED_ID"

        private const val NATIVE_ID = "YOUR_NATIVE_ID"
    }
}