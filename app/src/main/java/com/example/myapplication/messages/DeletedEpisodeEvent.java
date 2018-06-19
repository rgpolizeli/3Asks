package com.example.myapplication.messages;

public class DeletedEpisodeEvent {
    public boolean result;
    public int deletedEpisodeId;

    public DeletedEpisodeEvent(boolean result, int deletedEpisodeId) {
        this.result = result;
        this.deletedEpisodeId = deletedEpisodeId;
    }
}
