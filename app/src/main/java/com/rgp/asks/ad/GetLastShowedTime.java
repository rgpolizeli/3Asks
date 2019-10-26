package com.rgp.asks.ad;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import java.util.Date;

public interface GetLastShowedTime {
    @Nullable
    Date getLastShowedTime();

    void setLastShowedTime(@NonNull Date newLastShowedTime);
}
