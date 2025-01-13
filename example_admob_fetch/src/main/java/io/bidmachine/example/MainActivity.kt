package io.bidmachine.example

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.google.android.gms.ads.AdError
import com.google.android.gms.ads.AdView
import com.google.android.gms.ads.LoadAdError
import com.google.android.gms.ads.interstitial.InterstitialAd
import com.google.android.gms.ads.nativead.NativeAd
import com.google.android.gms.ads.rewarded.RewardedAd
import io.bidmachine.example.integration.listener.BannerAdListener
import io.bidmachine.example.integration.listener.InterstitialAdListener
import io.bidmachine.example.integration.listener.NativeAdListener
import io.bidmachine.example.integration.adapter.PrebidAdIntegrationAdapter
import io.bidmachine.example.integration.listener.RewardedAdListener
import io.bidmachine.example.integration.adapter.WaterfallAdIntegrationAdapter
import io.bidmachine.example.databinding.ActivityMainBinding
import io.bidmachine.example.integration.adapter.BaseAdIntegrationAdapter

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding

    private lateinit var adIntegrationAdapter: BaseAdIntegrationAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityMainBinding.inflate(layoutInflater).apply {
            setContentView(root)

            cgAdIntegration.setOnCheckedStateChangeListener { _, checkedIds ->
                when (checkedIds[0]) {
                    cWaterfallAdIntegration.id -> {
                        adIntegrationAdapter = WaterfallAdIntegrationAdapter()
                    }

                    cPrebidAdIntegration.id -> {
                        adIntegrationAdapter = PrebidAdIntegrationAdapter()
                    }
                }
            }
            cgAdIntegration.check(cWaterfallAdIntegration.id)
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

        if (adIntegrationAdapter.isInitialized()) {
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
        adIntegrationAdapter.initialize(this@MainActivity) {
            enableButtons()
        }
    }

    private fun enableButtons() {
        binding.also {
            it.cgAdIntegration.isEnabled = false
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

        adIntegrationAdapter.loadBanner(this, BannerViewListener())
    }

    private fun showBanner() {
        AppLogger.log("Banner", "show")

        binding.bShowBanner.isEnabled = false

        adIntegrationAdapter.showBanner(binding.adContainer)
    }

    private fun destroyBanner() {
        AppLogger.log("Banner", "destroy")

        binding.adContainer.removeAllViews()

        adIntegrationAdapter.destroyBanner()
    }

    private fun loadMrec() {
        binding.bShowMrec.isEnabled = false

        // Destroy previous AdView
        destroyMrec()

        AppLogger.log("Mrec", "load")

        adIntegrationAdapter.loadMrec(this, MrecViewListener())
    }

    private fun showMrec() {
        AppLogger.log("Mrec", "show")

        binding.bShowMrec.isEnabled = false

        adIntegrationAdapter.showMrec(binding.adContainer)
    }

    private fun destroyMrec() {
        AppLogger.log("Mrec", "destroy")

        binding.adContainer.removeAllViews()

        adIntegrationAdapter.destroyMrec()
    }

    private fun loadInterstitial() {
        binding.bShowInterstitial.isEnabled = false

        // Destroy previous InterstitialAd
        destroyInterstitial()

        AppLogger.log("Interstitial", "load")

        adIntegrationAdapter.loadInterstitial(this, InterstitialListener())
    }

    private fun showInterstitial() {
        AppLogger.log("Interstitial", "show")

        binding.bShowInterstitial.isEnabled = false

        adIntegrationAdapter.showInterstitial(this)
    }

    private fun destroyInterstitial() {
        AppLogger.log("Interstitial", "destroy")

        adIntegrationAdapter.destroyInterstitial()
    }

    private fun loadRewarded() {
        binding.bShowRewarded.isEnabled = false

        // Destroy previous RewardedAd
        destroyRewarded()

        AppLogger.log("Rewarded", "load")

        adIntegrationAdapter.loadRewarded(this, RewardedListener())
    }

    private fun showRewarded() {
        AppLogger.log("Rewarded", "show")

        binding.bShowRewarded.isEnabled = false

        adIntegrationAdapter.showRewarded(this)
    }

    private fun destroyRewarded() {
        AppLogger.log("Rewarded", "destroy")

        adIntegrationAdapter.destroyRewarded()
    }

    private fun loadNative() {
        binding.bShowNative.isEnabled = false

        // Destroy previous NativeAd
        destroyNative()

        AppLogger.log("Native", "load")

        adIntegrationAdapter.loadNative(this, NativeListener())
    }

    private fun showNative() {
        AppLogger.log("Native", "show")

        binding.bShowNative.isEnabled = false

        adIntegrationAdapter.showNative(binding.adContainer)
    }

    private fun destroyNative() {
        AppLogger.log("Native", "destroy")

        adIntegrationAdapter.destroyNative()
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
}