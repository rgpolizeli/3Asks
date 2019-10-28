package com.rgp.asks.ad;

import android.content.Context;

import androidx.annotation.NonNull;

import com.google.android.gms.ads.AdListener;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.InterstitialAd;
import com.google.android.gms.ads.MobileAds;

import java.util.Date;

public class Ader {
    private InterstitialAd mInterstitialAd;
    private GetLastShowedTime getLastShowedTime;

    private final long intervalInMiliseconds = 1800000; // 1800000 ms - 30 min
    private final int numberOfIntervals = 1;
    // for test "ca-app-pub-3940256099942544/8691691433"
    //for build "ca-app-pub-0779487197855100/3480090569"
    private final String adUnitId = "ca-app-pub-3940256099942544/8691691433";

    public Ader(Context context, OnAdLoadingErrorListener onAdLoadingErrorListener, GetLastShowedTime getLastShowedTime) {
        this.getLastShowedTime = getLastShowedTime;

        MobileAds.initialize(context,
                adUnitId);
        mInterstitialAd = new InterstitialAd(context);
        mInterstitialAd.setAdUnitId(adUnitId);
        mInterstitialAd.loadAd(new AdRequest.Builder().build());

        mInterstitialAd.setAdListener(new AdListener() {
            @Override
            public void onAdFailedToLoad(int errorCode) {
                onAdLoadingErrorListener.onAdLoadingError();
            }

            @Override
            public void onAdClosed() {
                // Load the next interstitial.
                mInterstitialAd.loadAd(new AdRequest.Builder().build());
            }
        });
    }

    public void requestToShowAd() {
        Date currentTime = new Date();
        if ((this.getLastShowedTime.getLastShowedTime() == null || computeIntervals(this.getLastShowedTime.getLastShowedTime(), currentTime) == this.numberOfIntervals) && this.mInterstitialAd.isLoaded()) {
            this.mInterstitialAd.show();
            this.getLastShowedTime.setLastShowedTime(currentTime);
        }
    }

    private int computeIntervals(@NonNull Date startDate, @NonNull Date endDate) {
        long difference = endDate.getTime() - startDate.getTime();
        return (int) (difference / this.intervalInMiliseconds);
    }

    public void forceRequestToAdLoad() {
        mInterstitialAd.loadAd(new AdRequest.Builder().build());
    }
}
