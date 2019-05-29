package com.rpolizeli.asks.messages;

public class OpenEditBeliefActivityEvent {

    public final int beliefPositionInRecyclerView;

    public OpenEditBeliefActivityEvent(int beliefPositionInRecyclerView) {
        this.beliefPositionInRecyclerView = beliefPositionInRecyclerView;
    }
}
