package com.rpolizeli.asks.messages;

public class OpenEditArgumentDialogEvent {

    public final int argumentPositionInRecyclerView;

    public OpenEditArgumentDialogEvent(int argumentPositionInRecyclerView) {
        this.argumentPositionInRecyclerView = argumentPositionInRecyclerView;
    }
}
