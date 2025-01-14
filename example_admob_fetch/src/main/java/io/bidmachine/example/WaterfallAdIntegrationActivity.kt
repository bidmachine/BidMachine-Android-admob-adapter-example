package io.bidmachine.example

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import com.google.android.gms.ads.AdError
import com.google.android.gms.ads.AdListener
import com.google.android.gms.ads.AdLoader
import com.google.android.gms.ads.AdRequest
import com.google.android.gms.ads.AdSize
import com.google.android.gms.ads.AdView
import com.google.android.gms.ads.FullScreenContentCallback
import com.google.android.gms.ads.LoadAdError
import com.google.android.gms.ads.MobileAds
import com.google.android.gms.ads.OnUserEarnedRewardListener
import com.google.android.gms.ads.interstitial.InterstitialAd
import com.google.android.gms.ads.interstitial.InterstitialAdLoadCallback
import com.google.android.gms.ads.nativead.NativeAd
import com.google.android.gms.ads.rewarded.RewardItem
import com.google.android.gms.ads.rewarded.RewardedAd
import com.google.android.gms.ads.rewarded.RewardedAdLoadCallback
import io.bidmachine.BidMachine
import io.bidmachine.example.databinding.ActivityIntegrationBinding
import io.bidmachine.example.databinding.NativeAdBinding
import java.util.concurrent.atomic.AtomicBoolean

class WaterfallAdIntegrationActivity : AppCompatActivity() {

    private lateinit var binding: ActivityIntegrationBinding

    private var bannerAdView: AdView? = null

    private var mrecAdView: AdView? = null

    private var interstitialAd: InterstitialAd? = null

    private var rewardedAd: RewardedAd? = null

    private var nativeAd: NativeAd? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityIntegrationBinding.inflate(layoutInflater).apply {
            setContentView(root)

            bInitialize.setOnClickListener {
                initialize()
            }
            bLoadBanner.setOnClickListener {
                loadBanner()
            }
            bShowBanner.setOnClickListener {
                showBanner()
            }
            bLoadMrec.setOnClickListener {
                loadMrec()
            }
            bShowMrec.setOnClickListener {
                showMrec()
            }
            bLoadInterstitial.setOnClickListener {
                loadInterstitial()
            }
            bShowInterstitial.setOnClickListener {
                showInterstitial()
            }
            bLoadRewarded.setOnClickListener {
                loadRewarded()
            }
            bShowRewarded.setOnClickListener {
                showRewarded()
            }
            bLoadNative.setOnClickListener {
                loadNative()
            }
            bShowNative.setOnClickListener {
                showNative()
            }
        }

        if (isAdMobInitialized.get()) {
            enableButtons()
        }
    }

    override fun onDestroy() {
        super.onDestroy()

        destroyBanner()
        destroyMrec()
        destroyInterstitial()
        destroyRewarded()
        destroyNative()
    }

    private fun initialize() {
        MobileAds.initialize(this) {
            // Optionally, enable test mode and logging
            BidMachine.setTestMode(true)
            BidMachine.setLoggingEnabled(true)
            // Complete initialization
            isAdMobInitialized.set(true)
            enableButtons()
        }
    }

    private fun enableButtons() {
        binding.also {
            it.bInitialize.isEnabled = false
            it.bLoadBanner.isEnabled = true
            it.bLoadMrec.isEnabled = true
            it.bLoadInterstitial.isEnabled = true
            it.bLoadRewarded.isEnabled = true
            it.bLoadNative.isEnabled = true
        }
    }

    private fun loadBanner() {
        binding.bShowBanner.isEnabled = false

        // Destroy previous AdView
        destroyBanner()

        AppLogger.log("Banner", "load")

        // Create new AdRequest instance
        val adRequest = AdRequest.Builder().build()
        // Create new AdView instance and load it
        loadAdMobBanner(BANNER_ID, adRequest, BannerViewListener())
    }

    private fun loadAdMobBanner(
        adUnitId: String,
        adRequest: AdRequest,
        listener: BannerAdListener,
    ) {
        AppLogger.log("Banner", "load AdMob")

        bannerAdView = AdView(this).also {
            it.layoutParams = ViewGroup.LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.MATCH_PARENT,
            )
            it.adUnitId = adUnitId
            it.setAdSize(AdSize.BANNER)
            it.adListener = BannerLoadListener(it, listener)
            it.loadAd(adRequest)
        }
    }

    private fun showBanner() {
        AppLogger.log("Banner", "show")

        binding.bShowBanner.isEnabled = false

        bannerAdView?.let {
            addAdView(binding.adContainer, it)
        } ?: AppLogger.log("show error - banner object is null")
    }

    private fun destroyBanner() {
        AppLogger.log("Banner", "destroy")

        binding.adContainer.removeAllViews()

        bannerAdView?.destroy()
        bannerAdView = null
    }

    private fun loadMrec() {
        binding.bShowMrec.isEnabled = false

        // Destroy previous AdView
        destroyMrec()

        AppLogger.log("Mrec", "load")

        // Create new AdRequest instance
        val adRequest = AdRequest.Builder().build()
        // Create new AdView instance and load it
        loadAdMobMrec(MREC_ID, adRequest, MrecViewListener())
    }

    private fun loadAdMobMrec(adUnitId: String, adRequest: AdRequest, listener: BannerAdListener) {
        AppLogger.log("Mrec", "load AdMob")

        mrecAdView = AdView(this).also {
            it.layoutParams = ViewGroup.LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.MATCH_PARENT,
            )
            it.adUnitId = adUnitId
            it.setAdSize(AdSize.MEDIUM_RECTANGLE)
            it.adListener = MrecLoadListener(it, listener)
            it.loadAd(adRequest)
        }
    }

    private fun showMrec() {
        AppLogger.log("Mrec", "show")

        binding.bShowMrec.isEnabled = false

        mrecAdView?.let {
            addAdView(binding.adContainer, it)
        } ?: AppLogger.log("show error - mrec object is null")
    }

    private fun destroyMrec() {
        AppLogger.log("Mrec", "destroy")

        binding.adContainer.removeAllViews()

        mrecAdView?.destroy()
        mrecAdView = null
    }

    private fun loadInterstitial() {
        binding.bShowInterstitial.isEnabled = false

        // Destroy previous InterstitialAd
        destroyInterstitial()

        AppLogger.log("Interstitial", "load")

        // Create new AdRequest instance
        val adRequest = AdRequest.Builder().build()
        // Load InterstitialAd
        loadAdMobInterstitial(INTERSTITIAL_ID, adRequest, InterstitialListener())
    }

    private fun loadAdMobInterstitial(adUnitId: String, adRequest: AdRequest, listener: InterstitialAdListener) {
        AppLogger.log("Interstitial", "load AdMob")

        InterstitialAd.load(this, adUnitId, adRequest, InterstitialLoadListener(listener))
    }

    private fun showInterstitial() {
        AppLogger.log("Interstitial", "show")

        binding.bShowInterstitial.isEnabled = false

        interstitialAd?.show(this) ?: AppLogger.log("show error - interstitial object not loaded")
    }

    private fun destroyInterstitial() {
        AppLogger.log("Interstitial", "destroy")

        interstitialAd?.fullScreenContentCallback = null
        interstitialAd = null
    }

    private fun loadRewarded() {
        binding.bShowRewarded.isEnabled = false

        // Destroy previous RewardedAd
        destroyRewarded()

        AppLogger.log("Rewarded", "load")

        // Create new AdRequest instance
        val adRequest = AdRequest.Builder().build()
        // Load RewardedAd
        loadAdMobRewarded(REWARDED_ID, adRequest, RewardedListener())
    }

    private fun loadAdMobRewarded(adUnitId: String, adRequest: AdRequest, listener: RewardedAdListener) {
        AppLogger.log("Rewarded", "load AdMob")

        RewardedAd.load(this, adUnitId, adRequest, RewardedLoadListener(listener))
    }

    private fun showRewarded() {
        AppLogger.log("Rewarded", "show")

        binding.bShowRewarded.isEnabled = false

        rewardedAd?.let {
            it.show(this, RewardedEarnedListener(it))
        } ?: AppLogger.log("show error - rewarded object not loaded")
    }

    private fun destroyRewarded() {
        AppLogger.log("Rewarded", "destroy")

        rewardedAd?.fullScreenContentCallback = null
        rewardedAd = null
    }

    private fun loadNative() {
        binding.bShowNative.isEnabled = false

        // Destroy previous NativeAd
        destroyNative()

        AppLogger.log("Native", "load")

        // Create new AdRequest instance
        val adRequest = AdRequest.Builder().build()
        // Create new AdLoader instance and load
        loadAdMobNative(NATIVE_ID, adRequest, NativeListener())
    }

    private fun loadAdMobNative(adUnitId: String, adRequest: AdRequest, listener: NativeAdListener) {
        AppLogger.log("Native", "load AdMob")

        val nativeListener = NativeLoadedListener(listener)
        AdLoader.Builder(this, adUnitId)
                .forNativeAd(nativeListener)
                .withAdListener(nativeListener)
                .build()
                .loadAd(adRequest)
    }

    private fun showNative() {
        AppLogger.log("Native", "show")

        binding.bShowNative.isEnabled = false

        nativeAd?.let { nativeAd ->
            val nativeAdBinding = NativeAdBinding.inflate(
                LayoutInflater.from(binding.adContainer.context),
                binding.adContainer,
                false,
            )
            nativeAdBinding.let {
                it.tvTitle.text = nativeAd.headline
                it.tvDescription.text = nativeAd.body
                it.rbRating.rating = nativeAd.starRating?.toFloat() ?: 0F
                it.bCta.text = nativeAd.callToAction

                it.root.iconView = it.ivIcon
                it.root.mediaView = it.mediaView
                it.root.setNativeAd(nativeAd)
            }
            addAdView(binding.adContainer, nativeAdBinding.root)
        } ?: AppLogger.log("show error - native object not loaded")
    }

    private fun destroyNative() {
        AppLogger.log("Native", "destroy")

        nativeAd?.destroy()
        nativeAd = null
    }

    private fun addAdView(adContainer: ViewGroup, view: View) {
        adContainer.removeAllViews()
        adContainer.safeAddView(view)
    }

    private inner class BannerViewListener : BannerAdListener {

        override fun onAdLoaded(adView: AdView) {
            binding.bShowBanner.isEnabled = true

            AppLogger.logEvent("Banner", "onAdLoaded", adView.responseInfo)
        }

        override fun onAdFailedToLoad(loadAdError: LoadAdError) {
            AppLogger.logEvent("Banner", "onAdFailedToLoad", loadAdError)
        }

        override fun onAdOpened(adView: AdView) {
            AppLogger.logEvent("Banner", "onAdOpened", adView.responseInfo)
        }

        override fun onAdImpression(adView: AdView) {
            AppLogger.logEvent("Banner", "onAdImpression", adView.responseInfo)
        }

        override fun onAdClicked(adView: AdView) {
            AppLogger.logEvent("Banner", "onAdClicked", adView.responseInfo)
        }

        override fun onAdClosed(adView: AdView) {
            AppLogger.logEvent("Banner", "onAdClosed", adView.responseInfo)
        }
    }

    private inner class BannerLoadListener(val adView: AdView, val listener: BannerAdListener) : AdListener() {

        override fun onAdLoaded() {
            listener.onAdLoaded(adView)
        }

        override fun onAdFailedToLoad(loadAdError: LoadAdError) {
            listener.onAdFailedToLoad(loadAdError)
        }

        override fun onAdOpened() {
            listener.onAdOpened(adView)
        }

        override fun onAdImpression() {
            listener.onAdImpression(adView)
        }

        override fun onAdClicked() {
            listener.onAdClicked(adView)
        }

        override fun onAdClosed() {
            listener.onAdClosed(adView)
        }
    }

    private inner class MrecViewListener : BannerAdListener {

        override fun onAdLoaded(adView: AdView) {
            binding.bShowMrec.isEnabled = true

            AppLogger.logEvent("Mrec", "onAdLoaded", adView.responseInfo)
        }

        override fun onAdFailedToLoad(loadAdError: LoadAdError) {
            AppLogger.logEvent("Mrec", "onAdFailedToLoad", loadAdError)
        }

        override fun onAdOpened(adView: AdView) {
            AppLogger.logEvent("Mrec", "onAdOpened", adView.responseInfo)
        }

        override fun onAdImpression(adView: AdView) {
            AppLogger.logEvent("Mrec", "onAdImpression", adView.responseInfo)
        }

        override fun onAdClicked(adView: AdView) {
            AppLogger.logEvent("Mrec", "onAdClicked", adView.responseInfo)
        }

        override fun onAdClosed(adView: AdView) {
            AppLogger.logEvent("Mrec", "onAdClosed", adView.responseInfo)
        }
    }

    private inner class MrecLoadListener(val adView: AdView, val listener: BannerAdListener) : AdListener() {

        override fun onAdLoaded() {
            listener.onAdLoaded(adView)
        }

        override fun onAdFailedToLoad(loadAdError: LoadAdError) {
            listener.onAdFailedToLoad(loadAdError)
        }

        override fun onAdOpened() {
            listener.onAdOpened(adView)
        }

        override fun onAdImpression() {
            listener.onAdImpression(adView)
        }

        override fun onAdClicked() {
            listener.onAdClicked(adView)
        }

        override fun onAdClosed() {
            listener.onAdClosed(adView)
        }
    }

    private inner class InterstitialListener : InterstitialAdListener {

        override fun onAdLoaded(interstitialAd: InterstitialAd) {
            binding.bShowInterstitial.isEnabled = true

            AppLogger.logEvent("Interstitial", "onAdLoaded", interstitialAd.responseInfo)
        }

        override fun onAdFailedToLoad(loadAdError: LoadAdError) {
            AppLogger.logEvent("Interstitial", "onAdFailedToLoad", loadAdError)
        }

        override fun onAdShowed(interstitialAd: InterstitialAd) {
            AppLogger.logEvent("Interstitial", "onAdShowed", interstitialAd.responseInfo)
        }

        override fun onAdFailedToShow(adError: AdError) {
            AppLogger.logEvent("Interstitial", "onAdFailedToShow", adError)
        }

        override fun onAdImpression(interstitialAd: InterstitialAd) {
            AppLogger.logEvent("Interstitial", "onAdImpression", interstitialAd.responseInfo)
        }

        override fun onAdClicked(interstitialAd: InterstitialAd) {
            AppLogger.logEvent("Interstitial", "onAdClicked", interstitialAd.responseInfo)
        }

        override fun onAdDismissed(interstitialAd: InterstitialAd) {
            AppLogger.logEvent("Interstitial", "onAdDismissed", interstitialAd.responseInfo)
        }
    }

    private inner class InterstitialLoadListener(val listener: InterstitialAdListener) : InterstitialAdLoadCallback() {

        override fun onAdLoaded(interstitialAd: InterstitialAd) {
            this@WaterfallAdIntegrationActivity.interstitialAd = interstitialAd.apply {
                fullScreenContentCallback = InterstitialShowListener(this, listener)
            }

            listener.onAdLoaded(interstitialAd)
        }

        override fun onAdFailedToLoad(loadAdError: LoadAdError) {
            listener.onAdFailedToLoad(loadAdError)
        }
    }

    private inner class InterstitialShowListener(
        val interstitialAd: InterstitialAd,
        val listener: InterstitialAdListener,
    ) : FullScreenContentCallback() {

        override fun onAdShowedFullScreenContent() {
            listener.onAdShowed(interstitialAd)
        }

        override fun onAdFailedToShowFullScreenContent(adError: AdError) {
            listener.onAdFailedToShow(adError)
        }

        override fun onAdImpression() {
            listener.onAdImpression(interstitialAd)
        }

        override fun onAdClicked() {
            listener.onAdClicked(interstitialAd)
        }

        override fun onAdDismissedFullScreenContent() {
            listener.onAdDismissed(interstitialAd)
        }
    }

    private inner class RewardedListener : RewardedAdListener {

        override fun onAdLoaded(rewardedAd: RewardedAd) {
            binding.bShowRewarded.isEnabled = true

            AppLogger.logEvent("Rewarded", "onAdLoaded", rewardedAd.responseInfo)
        }

        override fun onAdFailedToLoad(loadAdError: LoadAdError) {
            AppLogger.logEvent("Rewarded", "onAdFailedToLoad", loadAdError)
        }

        override fun onAdShowed(rewardedAd: RewardedAd) {
            AppLogger.logEvent("Rewarded", "onAdShowed", rewardedAd.responseInfo)
        }

        override fun onAdFailedToShow(adError: AdError) {
            AppLogger.logEvent("Rewarded", "onAdFailedToShow", adError)
        }

        override fun onAdImpression(rewardedAd: RewardedAd) {
            AppLogger.logEvent("Rewarded", "onAdImpression", rewardedAd.responseInfo)
        }

        override fun onAdClicked(rewardedAd: RewardedAd) {
            AppLogger.logEvent("Rewarded", "onAdClicked", rewardedAd.responseInfo)
        }

        override fun onAdDismissed(rewardedAd: RewardedAd) {
            AppLogger.logEvent("Rewarded", "onAdDismissed", rewardedAd.responseInfo)
        }
    }

    private inner class RewardedLoadListener(val listener: RewardedAdListener) : RewardedAdLoadCallback() {

        override fun onAdLoaded(rewardedAd: RewardedAd) {
            this@WaterfallAdIntegrationActivity.rewardedAd = rewardedAd.apply {
                fullScreenContentCallback = RewardedShowListener(this, listener)
            }

            listener.onAdLoaded(rewardedAd)
        }

        override fun onAdFailedToLoad(loadAdError: LoadAdError) {
            listener.onAdFailedToLoad(loadAdError)
        }
    }

    private inner class RewardedShowListener(val rewardedAd: RewardedAd, val listener: RewardedAdListener) :
            FullScreenContentCallback() {

        override fun onAdShowedFullScreenContent() {
            listener.onAdShowed(rewardedAd)
        }

        override fun onAdFailedToShowFullScreenContent(adError: AdError) {
            listener.onAdFailedToShow(adError)
        }

        override fun onAdImpression() {
            listener.onAdImpression(rewardedAd)
        }

        override fun onAdClicked() {
            listener.onAdClicked(rewardedAd)
        }

        override fun onAdDismissedFullScreenContent() {
            listener.onAdDismissed(rewardedAd)
        }
    }

    private inner class RewardedEarnedListener(val rewardedAd: RewardedAd) : OnUserEarnedRewardListener {

        override fun onUserEarnedReward(rewardItem: RewardItem) {
            AppLogger.logEvent("Rewarded", "onUserEarnedReward", rewardedAd.responseInfo)
        }
    }

    private inner class NativeLoadedListener(val listener: NativeAdListener) : AdListener(),
            NativeAd.OnNativeAdLoadedListener {

        private lateinit var nativeAd: NativeAd

        override fun onNativeAdLoaded(nativeAd: NativeAd) {
            this.nativeAd = nativeAd
            this@WaterfallAdIntegrationActivity.nativeAd = nativeAd

            listener.onNativeAdLoaded(nativeAd)
        }

        override fun onAdFailedToLoad(loadAdError: LoadAdError) {
            listener.onAdFailedToLoad(loadAdError)
        }

        override fun onAdOpened() {
            listener.onAdOpened(nativeAd)
        }

        override fun onAdImpression() {
            listener.onAdImpression(nativeAd)
        }

        override fun onAdClicked() {
            listener.onAdClicked(nativeAd)
        }
    }

    private inner class NativeListener : NativeAdListener {

        override fun onNativeAdLoaded(nativeAd: NativeAd) {
            binding.bShowNative.isEnabled = true

            AppLogger.logEvent("Native", "onNativeAdLoaded", nativeAd.responseInfo)
        }

        override fun onAdFailedToLoad(loadAdError: LoadAdError) {
            AppLogger.logEvent("Native", "onAdFailedToLoad", loadAdError)
        }

        override fun onAdOpened(nativeAd: NativeAd) {
            AppLogger.logEvent("Native", "onAdOpened", nativeAd.responseInfo)
        }

        override fun onAdImpression(nativeAd: NativeAd) {
            AppLogger.logEvent("Native", "onAdImpression", nativeAd.responseInfo)
        }

        override fun onAdClicked(nativeAd: NativeAd) {
            AppLogger.logEvent("Native", "onAdClicked", nativeAd.responseInfo)
        }
    }

    companion object {

        internal var isAdMobInitialized = AtomicBoolean(false)

        private const val BANNER_ID = "YOUR_BANNER_ID"

        private const val MREC_ID = "YOUR_MREC_ID"

        private const val INTERSTITIAL_ID = "YOUR_INTERSTITIAL_ID"

        private const val REWARDED_ID = "YOUR_REWARDED_ID"

        private const val NATIVE_ID = "YOUR_NATIVE_ID"
    }
}