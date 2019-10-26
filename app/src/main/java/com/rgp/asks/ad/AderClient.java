package com.rgp.asks.ad;

import androidx.annotation.NonNull;

public interface AderClient {
    default void requestToShowAd(@NonNull AderCreator aderCreator) {
        aderCreator.requestToShowAd();
    }

    ;
}
