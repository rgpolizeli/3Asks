package com.rgp.asks.messages;

public class OpenEditObjectionDialogEvent {

    public final int objectionPositionInRecyclerView;

    public OpenEditObjectionDialogEvent(int objectionPositionInRecyclerView) {
        this.objectionPositionInRecyclerView = objectionPositionInRecyclerView;
    }
}
