package com.example.myapplication.messages;

import android.support.annotation.NonNull;

public class CreatedReactionEvent {
    public final int reactionId;

    public CreatedReactionEvent(@NonNull final int reactionId) {
        this.reactionId = reactionId;
    }
}
