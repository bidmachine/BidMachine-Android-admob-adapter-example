package io.bidmachine.example.integration.listener

import com.google.android.gms.ads.AdView
import com.google.android.gms.ads.LoadAdError

internal interface BannerAdListener {

    fun onAdLoaded(adView: AdView)

    fun onAdFailedToLoad(loadAdError: LoadAdError)

    fun onAdOpened(adView: AdView)

    fun onAdImpression(adView: AdView)

    fun onAdClicked(adView: AdView)

    fun onAdClosed(adView: AdView)
}