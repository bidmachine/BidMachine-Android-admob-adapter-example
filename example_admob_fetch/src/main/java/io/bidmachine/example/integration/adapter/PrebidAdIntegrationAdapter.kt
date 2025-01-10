package io.bidmachine.example.integration.adapter

import android.content.Context
import com.google.ads.mediation.bidmachine.BidMachineUtils
import com.google.ads.mediation.bidmachine.prebid.request.BidMachineAdRequestBuilderFactory
import com.google.android.gms.ads.AdRequest
import com.google.android.gms.ads.MobileAds
import com.google.android.gms.ads.initialization.OnInitializationCompleteListener
import io.bidmachine.BidMachine
import io.bidmachine.banner.BannerRequest
import io.bidmachine.banner.BannerSize
import io.bidmachine.example.AppLogger
import io.bidmachine.example.integration.listener.BannerAdListener
import io.bidmachine.example.integration.listener.InterstitialAdListener
import io.bidmachine.example.integration.listener.NativeAdListener
import io.bidmachine.example.integration.listener.RewardedAdListener
import io.bidmachine.interstitial.InterstitialRequest
import io.bidmachine.models.AuctionResult
import io.bidmachine.nativead.NativeRequest
import io.bidmachine.rewarded.RewardedRequest
import io.bidmachine.utils.BMError

internal class PrebidAdIntegrationAdapter : BaseAdIntegrationAdapter() {

    private var bannerRequest: BannerRequest? = null

    private var mrecRequest: BannerRequest? = null

    private var interstitialRequest: InterstitialRequest? = null

    private var rewardedRequest: RewardedRequest? = null

    private var nativeRequest: NativeRequest? = null

    override fun initialize(context: Context, listener: OnInitializationCompleteListener) {
        BidMachine.setTestMode(true)
        BidMachine.setLoggingEnabled(true)
        BidMachine.initialize(context, BID_MACHINE_SOURCE_ID) {
            MobileAds.initialize(context) {
                isAdMobInitialized.set(true)
                listener.onInitializationComplete(it)
            }
        }
    }

    override fun loadBanner(context: Context, listener: BannerAdListener) {
        loadBidMachineBanner(context, listener)
    }

    private fun loadBidMachineBanner(context: Context, listener: BannerAdListener) {
        AppLogger.log("Banner", "load BidMachine")

        // Create new BidMachine request
        bannerRequest = BidMachineAdRequestBuilderFactory.createBannerRequestBuilder()
                .setSize(BannerSize.Size_320x50)
                .setListener(object : BannerRequest.AdRequestListener {
                    override fun onRequestSuccess(bannerRequest: BannerRequest, auctionResult: AuctionResult) {
                        // Create AdRequest
                        val adRequest = BidMachineUtils.createAdRequest(bannerRequest)
                        // Create new AdView instance and load it
                        loadAdMobBanner(context, BANNER_ID, adRequest, listener)
                    }

                    override fun onRequestFailed(bannerRequest: BannerRequest, bmError: BMError) {
                        // Create AdRequest
                        val adRequest = AdRequest.Builder().build()
                        // Create new AdView instance and load it
                        loadAdMobBanner(context, BANNER_ID, adRequest, listener)
                    }

                    override fun onRequestExpired(bannerRequest: BannerRequest) {

                    }
                })
                .build().also {
                    // Request an ad from BidMachine without loading it
                    it.request(context)
                }
    }

    override fun destroyBanner() {
        super.destroyBanner()

        bannerRequest?.destroy()
        bannerRequest = null
    }

    override fun loadMrec(context: Context, listener: BannerAdListener) {
        loadBidMachineMrec(context, listener)
    }

    private fun loadBidMachineMrec(context: Context, listener: BannerAdListener) {
        AppLogger.log("Mrec", "load BidMachine")

        // Create new BidMachine request
        mrecRequest = BidMachineAdRequestBuilderFactory.createBannerRequestBuilder()
                .setSize(BannerSize.Size_300x250)
                .setListener(object : BannerRequest.AdRequestListener {
                    override fun onRequestSuccess(bannerRequest: BannerRequest, auctionResult: AuctionResult) {
                        // Create AdRequest
                        val adRequest = BidMachineUtils.createAdRequest(bannerRequest)
                        // Create new AdView instance and load it
                        loadAdMobMrec(context, MREC_ID, adRequest, listener)
                    }

                    override fun onRequestFailed(bannerRequest: BannerRequest, bmError: BMError) {
                        // Create AdRequest
                        val adRequest = AdRequest.Builder().build()
                        // Create new AdView instance and load it
                        loadAdMobMrec(context, MREC_ID, adRequest, listener)
                    }

                    override fun onRequestExpired(bannerRequest: BannerRequest) {

                    }
                })
                .build().also {
                    // Request an ad from BidMachine without loading it
                    it.request(context)
                }
    }

    override fun destroyMrec() {
        super.destroyMrec()

        mrecRequest?.destroy()
        mrecRequest = null
    }

    override fun loadInterstitial(context: Context, listener: InterstitialAdListener) {
        loadBidMachineInterstitial(context, listener)
    }

    private fun loadBidMachineInterstitial(context: Context, listener: InterstitialAdListener) {
        AppLogger.log("Interstitial", "load BidMachine")

        // Create new BidMachine request
        interstitialRequest = BidMachineAdRequestBuilderFactory.createInterstitialRequestBuilder()
                .setListener(object : InterstitialRequest.AdRequestListener {
                    override fun onRequestSuccess(
                        interstitialRequest: InterstitialRequest,
                        auctionResult: AuctionResult,
                    ) {
                        // Create AdRequest
                        val adRequest = BidMachineUtils.createAdRequest(interstitialRequest)
                        // Load InterstitialAd
                        loadAdMobInterstitial(context, INTERSTITIAL_ID, adRequest, listener)
                    }

                    override fun onRequestFailed(interstitialRequest: InterstitialRequest, bmError: BMError) {
                        // Create AdRequest
                        val adRequest = AdRequest.Builder().build()
                        // Load InterstitialAd
                        loadAdMobInterstitial(context, INTERSTITIAL_ID, adRequest, listener)
                    }

                    override fun onRequestExpired(interstitialRequest: InterstitialRequest) {

                    }
                })
                .build().also {
                    // Request an ad from BidMachine without loading it
                    it.request(context)
                }
    }

    override fun destroyInterstitial() {
        super.destroyInterstitial()

        interstitialRequest?.destroy()
        interstitialRequest = null
    }

    override fun loadRewarded(context: Context, listener: RewardedAdListener) {
        loadBidMachineRewarded(context, listener)
    }

    private fun loadBidMachineRewarded(context: Context, listener: RewardedAdListener) {
        AppLogger.log("Rewarded", "load BidMachine")

        // Create new BidMachine request
        rewardedRequest = BidMachineAdRequestBuilderFactory.createRewardedRequestBuilder()
                .setListener(object : RewardedRequest.AdRequestListener {
                    override fun onRequestSuccess(rewardedRequest: RewardedRequest, auctionResult: AuctionResult) {
                        // Create AdRequest
                        val adRequest = BidMachineUtils.createAdRequest(rewardedRequest)
                        // Load RewardedAd
                        loadAdMobRewarded(context, REWARDED_ID, adRequest, listener)
                    }

                    override fun onRequestFailed(rewardedRequest: RewardedRequest, bmError: BMError) {
                        // Create AdRequest
                        val adRequest = AdRequest.Builder().build()
                        // Load RewardedAd
                        loadAdMobRewarded(context, REWARDED_ID, adRequest, listener)
                    }

                    override fun onRequestExpired(rewardedRequest: RewardedRequest) {

                    }
                })
                .build().also {
                    // Request an ad from BidMachine without loading it
                    it.request(context)
                }
    }

    override fun destroyRewarded() {
        super.destroyRewarded()

        rewardedRequest?.destroy()
        rewardedRequest = null
    }

    override fun loadNative(context: Context, listener: NativeAdListener) {
        loadBidMachineNative(context, listener)
    }

    private fun loadBidMachineNative(context: Context, listener: NativeAdListener) {
        AppLogger.log("Native", "load BidMachine")

        // Create new BidMachine request
        nativeRequest = BidMachineAdRequestBuilderFactory.createNativeRequestBuilder()
                .setListener(object : NativeRequest.AdRequestListener {
                    override fun onRequestSuccess(nativeRequest: NativeRequest, auctionResult: AuctionResult) {
                        // Create AdRequest
                        val adRequest = BidMachineUtils.createAdRequest(nativeRequest)
                        // Create new AdLoader instance and load
                        loadAdMobNative(context, NATIVE_ID, adRequest, listener)
                    }

                    override fun onRequestFailed(nativeRequest: NativeRequest, bmError: BMError) {
                        // Create AdRequest
                        val adRequest = AdRequest.Builder().build()
                        // Create new AdLoader instance and load
                        loadAdMobNative(context, NATIVE_ID, adRequest, listener)
                    }

                    override fun onRequestExpired(nativeRequest: NativeRequest) {

                    }
                })
                .build().also {
                    // Request an ad from BidMachine without loading it
                    it.request(context)
                }
    }

    override fun destroyNative() {
        super.destroyNative()

        nativeRequest?.destroy()
        nativeRequest = null
    }

    companion object {

        private const val BID_MACHINE_SOURCE_ID = "YOUR_SOURCE_ID"

        private const val BANNER_ID = "YOUR_BANNER_ID"

        private const val MREC_ID = "YOUR_MREC_ID"

        private const val INTERSTITIAL_ID = "YOUR_INTERSTITIAL_ID"

        private const val REWARDED_ID = "YOUR_REWARDED_ID"

        private const val NATIVE_ID = "YOUR_NATIVE_ID"
    }
}