package com.rgp.asks.messages;

import androidx.annotation.NonNull;

public class CreatedEpisodeEvent {
    public final int episodeId;
    public final String episodeName;

    public CreatedEpisodeEvent(final int episodeId, @NonNull final String episodeName) {
        this.episodeId = episodeId;
        this.episodeName = episodeName;
    }
}
