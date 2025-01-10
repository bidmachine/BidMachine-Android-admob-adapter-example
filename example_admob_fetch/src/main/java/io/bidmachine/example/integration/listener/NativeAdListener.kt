package io.bidmachine.example.integration.listener

import com.google.android.gms.ads.LoadAdError
import com.google.android.gms.ads.nativead.NativeAd

internal interface NativeAdListener {

    fun onNativeAdLoaded(nativeAd: NativeAd)

    fun onAdFailedToLoad(loadAdError: LoadAdError)

    fun onAdOpened(nativeAd: NativeAd)

    fun onAdImpression(nativeAd: NativeAd)

    fun onAdClicked(nativeAd: NativeAd)
}