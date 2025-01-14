package io.bidmachine.example

import com.google.android.gms.ads.AdError
import com.google.android.gms.ads.LoadAdError
import com.google.android.gms.ads.rewarded.RewardedAd

internal interface RewardedAdListener {

    fun onAdLoaded(rewardedAd: RewardedAd)

    fun onAdFailedToLoad(loadAdError: LoadAdError)

    fun onAdShowed(rewardedAd: RewardedAd)

    fun onAdFailedToShow(adError: AdError)

    fun onAdImpression(rewardedAd: RewardedAd)

    fun onAdClicked(rewardedAd: RewardedAd)

    fun onAdDismissed(rewardedAd: RewardedAd)
}