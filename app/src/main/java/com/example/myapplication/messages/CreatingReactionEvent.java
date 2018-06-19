package com.example.myapplication.messages;

import android.support.annotation.NonNull;

public class CreatingReactionEvent {
    public final String message;

    public CreatingReactionEvent(@NonNull final String message) {
        this.message = message;
    }
}
