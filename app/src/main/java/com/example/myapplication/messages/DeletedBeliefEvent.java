package com.example.myapplication.messages;

public class DeletedBeliefEvent {
    public boolean result;
    public int deletedBeliefId;

    public DeletedBeliefEvent(boolean result, int deletedBeliefId) {
        this.result = result;
        this.deletedBeliefId = deletedBeliefId;
    }
}
