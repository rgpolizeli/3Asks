package com.example.myapplication.messages;

public class OpenEditObjectionDialogEvent {

    public final int objectionPositionInRecyclerView;

    public OpenEditObjectionDialogEvent(int objectionPositionInRecyclerView) {
        this.objectionPositionInRecyclerView = objectionPositionInRecyclerView;
    }
}
