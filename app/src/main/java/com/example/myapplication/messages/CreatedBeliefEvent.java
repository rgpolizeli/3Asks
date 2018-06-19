package com.example.myapplication.messages;

import android.support.annotation.NonNull;

public class CreatedBeliefEvent {
    public final int beliefId;

    public CreatedBeliefEvent(@NonNull final int beliefId) {
        this.beliefId = beliefId;
    }
}
