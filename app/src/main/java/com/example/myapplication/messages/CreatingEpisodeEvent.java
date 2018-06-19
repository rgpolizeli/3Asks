package com.example.myapplication.messages;

import android.support.annotation.NonNull;

public class CreatingEpisodeEvent {

    public final String message;

    public CreatingEpisodeEvent(@NonNull final String message) {
        this.message = message;
    }
}
