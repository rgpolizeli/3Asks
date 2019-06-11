package com.rgp.asks.messages;

import androidx.annotation.NonNull;

public class CreatingReactionEvent {
    public final String message;

    public CreatingReactionEvent(@NonNull final String message) {
        this.message = message;
    }
}
