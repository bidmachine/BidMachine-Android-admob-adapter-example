package io.bidmachine.example.integration.adapter

import android.app.Activity
import android.content.Context
import android.view.ViewGroup
import com.google.android.gms.ads.initialization.OnInitializationCompleteListener
import io.bidmachine.example.integration.listener.BannerAdListener
import io.bidmachine.example.integration.listener.InterstitialAdListener
import io.bidmachine.example.integration.listener.NativeAdListener
import io.bidmachine.example.integration.listener.RewardedAdListener

internal interface AdIntegrationAdapter {

    fun initialize(context: Context, listener: OnInitializationCompleteListener)

    fun isInitialized(): Boolean

    fun loadBanner(context: Context, listener: BannerAdListener)

    fun showBanner(adContainer: ViewGroup)

    fun destroyBanner()

    fun loadMrec(context: Context, listener: BannerAdListener)

    fun showMrec(adContainer: ViewGroup)

    fun destroyMrec()

    fun loadInterstitial(context: Context, listener: InterstitialAdListener)

    fun showInterstitial(activity: Activity)

    fun destroyInterstitial()

    fun loadRewarded(context: Context, listener: RewardedAdListener)

    fun showRewarded(activity: Activity)

    fun destroyRewarded()

    fun loadNative(context: Context, listener: NativeAdListener)

    fun showNative(adContainer: ViewGroup)

    fun destroyNative()
}