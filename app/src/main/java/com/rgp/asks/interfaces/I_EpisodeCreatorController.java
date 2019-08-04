package com.rgp.asks.interfaces;

import androidx.annotation.NonNull;

public interface I_EpisodeCreatorController {
    void createEpisode(@NonNull String newEpisodeName, @NonNull String newEpisodeDate, @NonNull String newEpisodePeriod);
}
