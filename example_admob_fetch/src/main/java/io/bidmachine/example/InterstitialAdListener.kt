package io.bidmachine.example

import com.google.android.gms.ads.AdError
import com.google.android.gms.ads.LoadAdError
import com.google.android.gms.ads.interstitial.InterstitialAd

internal interface InterstitialAdListener {

    fun onAdLoaded(interstitialAd: InterstitialAd)

    fun onAdFailedToLoad(loadAdError: LoadAdError)

    fun onAdShowed(interstitialAd: InterstitialAd)

    fun onAdFailedToShow(adError: AdError)

    fun onAdImpression(interstitialAd: InterstitialAd)

    fun onAdClicked(interstitialAd: InterstitialAd)

    fun onAdDismissed(interstitialAd: InterstitialAd)
}