package com.example.myapplication.messages;

import android.support.annotation.NonNull;

public class CreatingBeliefEvent {
    public final String message;

    public CreatingBeliefEvent(@NonNull final String message) {
        this.message = message;
    }
}
