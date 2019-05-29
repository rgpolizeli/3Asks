package com.rpolizeli.asks.messages;

public class OpenEditReactionDialogEvent {

    public final int reactionPositionInRecyclerView;

    public OpenEditReactionDialogEvent(int reactionPositionInRecyclerView) {
        this.reactionPositionInRecyclerView = reactionPositionInRecyclerView;
    }
}
