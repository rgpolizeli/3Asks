package com.rgp.asks.messages;

public class DeletedBeliefEvent {
    public boolean result;
    public int deletedBeliefId;

    public DeletedBeliefEvent(boolean result, int deletedBeliefId) {
        this.result = result;
        this.deletedBeliefId = deletedBeliefId;
    }
}
