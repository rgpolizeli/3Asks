package com.rgp.asks.messages;

import androidx.annotation.NonNull;

public class CreatedBeliefEvent {
    public final int beliefId;
    public final String thought;

    public CreatedBeliefEvent(int beliefId, @NonNull String thought) {
        this.beliefId = beliefId;
        this.thought = thought;
    }
}
