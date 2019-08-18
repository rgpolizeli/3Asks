package com.rgp.asks.interfaces;

import androidx.annotation.NonNull;

public interface EpisodeDialogListener {
    void onEpisodeDialogCreateButtonClick(@NonNull String newEpisode, @NonNull String newDate, @NonNull String newPeriod);
}
