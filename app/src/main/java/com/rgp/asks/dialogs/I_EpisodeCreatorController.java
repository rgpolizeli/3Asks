package com.rgp.asks.dialogs;

import androidx.annotation.NonNull;

public interface I_EpisodeCreatorController {
    void createEpisode(@NonNull String newEpisodeName, @NonNull String newEpisodeDate, @NonNull String newEpisodePeriod);
}
