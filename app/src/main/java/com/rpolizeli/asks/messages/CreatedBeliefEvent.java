package com.rpolizeli.asks.messages;

import androidx.annotation.NonNull;

public class CreatedBeliefEvent {
    public final int beliefId;

    public CreatedBeliefEvent(@NonNull final int beliefId) {
        this.beliefId = beliefId;
    }
}
