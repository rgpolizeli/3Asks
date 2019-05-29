package com.rpolizeli.asks.messages;

import androidx.annotation.NonNull;

public class CreatedEpisodeEvent {
    public final int episodeId;

    public CreatedEpisodeEvent(@NonNull final int episodeId) {
        this.episodeId = episodeId;
    }
}
