package com.example.myapplication.messages;

import android.support.annotation.NonNull;

public class CreatedEpisodeEvent {
    public final int episodeId;

    public CreatedEpisodeEvent(@NonNull final int episodeId) {
        this.episodeId = episodeId;
    }
}
