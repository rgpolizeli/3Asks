package com.rpolizeli.asks.messages;

import androidx.annotation.NonNull;

public class CreatingBeliefEvent {
    public final String message;

    public CreatingBeliefEvent(@NonNull final String message) {
        this.message = message;
    }
}
