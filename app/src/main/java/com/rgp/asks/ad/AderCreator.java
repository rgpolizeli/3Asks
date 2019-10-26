package com.rgp.asks.ad;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;

public interface AderCreator {
    void createAder();

    void requestToShowAd();

    @NonNull
    AlertDialog createAdLoadErrorDialog();

    void openAdLoadingErrorDialog();
}
