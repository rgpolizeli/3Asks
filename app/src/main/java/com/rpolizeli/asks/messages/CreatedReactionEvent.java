package com.rpolizeli.asks.messages;

import androidx.annotation.NonNull;

public class CreatedReactionEvent {
    public final int reactionId;

    public CreatedReactionEvent(@NonNull final int reactionId) {
        this.reactionId = reactionId;
    }
}
