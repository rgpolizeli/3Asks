package com.example.myapplication.messages;

public class OpenEditReactionDialogEvent {

    public final int reactionPositionInRecyclerView;

    public OpenEditReactionDialogEvent(int reactionPositionInRecyclerView) {
        this.reactionPositionInRecyclerView = reactionPositionInRecyclerView;
    }
}
