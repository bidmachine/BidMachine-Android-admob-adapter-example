package io.bidmachine.example.integration.adapter

import android.app.Activity
import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.CallSuper
import com.google.android.gms.ads.AdError
import com.google.android.gms.ads.AdListener
import com.google.android.gms.ads.AdLoader
import com.google.android.gms.ads.AdRequest
import com.google.android.gms.ads.AdSize
import com.google.android.gms.ads.AdView
import com.google.android.gms.ads.FullScreenContentCallback
import com.google.android.gms.ads.LoadAdError
import com.google.android.gms.ads.OnUserEarnedRewardListener
import com.google.android.gms.ads.initialization.OnInitializationCompleteListener
import com.google.android.gms.ads.interstitial.InterstitialAd
import com.google.android.gms.ads.interstitial.InterstitialAdLoadCallback
import com.google.android.gms.ads.nativead.NativeAd
import com.google.android.gms.ads.rewarded.RewardItem
import com.google.android.gms.ads.rewarded.RewardedAd
import com.google.android.gms.ads.rewarded.RewardedAdLoadCallback
import io.bidmachine.example.AppLogger
import io.bidmachine.example.databinding.NativeAdBinding
import io.bidmachine.example.integration.listener.BannerAdListener
import io.bidmachine.example.integration.listener.InterstitialAdListener
import io.bidmachine.example.integration.listener.NativeAdListener
import io.bidmachine.example.integration.listener.RewardedAdListener
import io.bidmachine.example.onUiThread
import io.bidmachine.utils.ViewHelper
import java.util.concurrent.atomic.AtomicBoolean

internal abstract class BaseAdIntegrationAdapter {

    private var bannerAdView: AdView? = null

    private var mrecAdView: AdView? = null

    private var interstitialAd: InterstitialAd? = null

    private var rewardedAd: RewardedAd? = null

    private var nativeAd: NativeAd? = null

    abstract fun initialize(context: Context, listener: OnInitializationCompleteListener)

    fun isInitialized() = isAdMobInitialized.get()

    abstract fun loadBanner(context: Context, listener: BannerAdListener)

    protected fun loadAdMobBanner(
        context: Context,
        adUnitId: String,
        adRequest: AdRequest,
        listener: BannerAdListener,
    ) {
        AppLogger.log("Banner", "load AdMob")

        onUiThread {
            bannerAdView = AdView(context).also {
                it.layoutParams = ViewGroup.LayoutParams(
                    ViewGroup.LayoutParams.MATCH_PARENT,
                    ViewGroup.LayoutParams.MATCH_PARENT,
                )
                it.adUnitId = adUnitId
                it.setAdSize(AdSize.BANNER)
                it.adListener = BannerViewListener(it, listener)
                it.loadAd(adRequest)
            }
        }
    }

    fun showBanner(adContainer: ViewGroup) {
        bannerAdView?.let {
            onUiThread {
                addAdView(adContainer, it)
            }
        } ?: AppLogger.log("show error - banner object is null")
    }

    @CallSuper
    open fun destroyBanner() {
        bannerAdView?.destroy()
        bannerAdView = null
    }

    abstract fun loadMrec(context: Context, listener: BannerAdListener)

    protected fun loadAdMobMrec(context: Context, adUnitId: String, adRequest: AdRequest, listener: BannerAdListener) {
        AppLogger.log("Mrec", "load AdMob")

        onUiThread {
            mrecAdView = AdView(context).also {
                it.layoutParams = ViewGroup.LayoutParams(
                    ViewGroup.LayoutParams.MATCH_PARENT,
                    ViewGroup.LayoutParams.MATCH_PARENT,
                )
                it.adUnitId = adUnitId
                it.setAdSize(AdSize.MEDIUM_RECTANGLE)
                it.adListener = MrecViewListener(it, listener)
                it.loadAd(adRequest)
            }
        }
    }

    fun showMrec(adContainer: ViewGroup) {
        mrecAdView?.let {
            onUiThread {
                addAdView(adContainer, it)
            }
        } ?: AppLogger.log("show error - mrec object is null")
    }

    @CallSuper
    open fun destroyMrec() {
        mrecAdView?.destroy()
        mrecAdView = null
    }

    abstract fun loadInterstitial(context: Context, listener: InterstitialAdListener)

    protected fun loadAdMobInterstitial(context: Context,
                                        adUnitId: String,
                                        adRequest: AdRequest,
                                        listener: InterstitialAdListener) {
        AppLogger.log("Interstitial", "load AdMob")

        onUiThread {
            InterstitialAd.load(context, adUnitId, adRequest, InterstitialLoadListener(listener))
        }
    }

    fun showInterstitial(activity: Activity) {
        interstitialAd?.show(activity) ?: AppLogger.log("show error - interstitial object not loaded")
    }

    @CallSuper
    open fun destroyInterstitial() {
        interstitialAd?.fullScreenContentCallback = null
        interstitialAd = null
    }

    abstract fun loadRewarded(context: Context, listener: RewardedAdListener)

    protected fun loadAdMobRewarded(context: Context,
                                    adUnitId: String,
                                    adRequest: AdRequest,
                                    listener: RewardedAdListener) {
        AppLogger.log("Rewarded", "load AdMob")

        onUiThread {
            RewardedAd.load(context, adUnitId, adRequest, RewardedLoadListener(listener))
        }
    }

    fun showRewarded(activity: Activity) {
        rewardedAd?.let {
            it.show(activity, RewardedEarnedListener(it))
        } ?: AppLogger.log("show error - rewarded object not loaded")
    }

    @CallSuper
    open fun destroyRewarded() {
        rewardedAd?.fullScreenContentCallback = null
        rewardedAd = null
    }

    abstract fun loadNative(context: Context, listener: NativeAdListener)

    protected fun loadAdMobNative(
        context: Context,
        adUnitId: String,
        adRequest: AdRequest,
        listener: NativeAdListener,
    ) {
        AppLogger.log("Native", "load AdMob")

        val nativeListener = NativeListener(listener)
        AdLoader.Builder(context, adUnitId)
                .forNativeAd(nativeListener)
                .withAdListener(nativeListener)
                .build()
                .loadAd(adRequest)
    }

    fun showNative(adContainer: ViewGroup) {
        nativeAd?.let { nativeAd ->
            val nativeAdBinding = NativeAdBinding.inflate(
                LayoutInflater.from(adContainer.context),
                adContainer,
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
            addAdView(adContainer, nativeAdBinding.root)
        } ?: AppLogger.log("show error - native object not loaded")
    }

    @CallSuper
    open fun destroyNative() {
        nativeAd?.destroy()
        nativeAd = null
    }

    private fun addAdView(adContainer: ViewGroup, view: View) {
        adContainer.removeAllViews()
        ViewHelper.safeAddView(adContainer, view)
    }

    private class BannerViewListener(
        val adView: AdView,
        val listener: BannerAdListener,
    ) : AdListener() {

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

    private class MrecViewListener(val adView: AdView, val listener: BannerAdListener) : AdListener() {

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

    private inner class InterstitialLoadListener(val listener: InterstitialAdListener) : InterstitialAdLoadCallback() {

        override fun onAdLoaded(interstitialAd: InterstitialAd) {
            this@BaseAdIntegrationAdapter.interstitialAd = interstitialAd.apply {
                fullScreenContentCallback = InterstitialShowListener(this, listener)
            }

            listener.onAdLoaded(interstitialAd)
        }

        override fun onAdFailedToLoad(loadAdError: LoadAdError) {
            listener.onAdFailedToLoad(loadAdError)
        }
    }

    private class InterstitialShowListener(
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

    private inner class RewardedLoadListener(val listener: RewardedAdListener) : RewardedAdLoadCallback() {

        override fun onAdLoaded(rewardedAd: RewardedAd) {
            this@BaseAdIntegrationAdapter.rewardedAd = rewardedAd.apply {
                fullScreenContentCallback = RewardedShowListener(this, listener)
            }

            listener.onAdLoaded(rewardedAd)
        }

        override fun onAdFailedToLoad(loadAdError: LoadAdError) {
            listener.onAdFailedToLoad(loadAdError)
        }
    }

    private class RewardedShowListener(
        val rewardedAd: RewardedAd,
        val listener: RewardedAdListener,
    ) : FullScreenContentCallback() {

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

    private class RewardedEarnedListener(val rewardedAd: RewardedAd) : OnUserEarnedRewardListener {

        override fun onUserEarnedReward(rewardItem: RewardItem) {
            AppLogger.logEvent("Rewarded", "onUserEarnedReward", rewardedAd.responseInfo)
        }
    }

    private inner class NativeListener(
        val listener: NativeAdListener,
    ) : AdListener(), NativeAd.OnNativeAdLoadedListener {

        private lateinit var nativeAd: NativeAd

        override fun onNativeAdLoaded(nativeAd: NativeAd) {
            this.nativeAd = nativeAd
            this@BaseAdIntegrationAdapter.nativeAd = nativeAd

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

    companion object {

        internal var isAdMobInitialized = AtomicBoolean(false)
    }
}