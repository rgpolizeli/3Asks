package com.rgp.asks.persistence.entity;

import androidx.annotation.NonNull;
import androidx.room.Entity;
import androidx.room.ForeignKey;
import androidx.room.PrimaryKey;

import static androidx.room.ForeignKey.CASCADE;

@Entity
public class Reaction {

    @PrimaryKey(autoGenerate = true)
    private int id;

    @NonNull
    private String reaction;

    @NonNull
    private String reactionCategory;

    @ForeignKey(entity = Episode.class, parentColumns = "id", childColumns = "episodeId", onDelete = CASCADE)
    private int episodeId;

    public Reaction(@NonNull String reaction, @NonNull String reactionCategory, int episodeId) {
        this.reaction = reaction;
        this.reactionCategory = reactionCategory;
        this.episodeId = episodeId;
    }

    public Reaction(int reactionId, @NonNull String reaction, @NonNull String reactionCategory, @NonNull int episodeId) {
        this.id = reactionId;
        this.reaction = reaction;
        this.reactionCategory = reactionCategory;
        this.episodeId = episodeId;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    @NonNull
    public String getReaction() {
        return reaction;
    }

    public void setReaction(@NonNull String reaction) {
        this.reaction = reaction;
    }

    @NonNull
    public String getReactionCategory() {
        return reactionCategory;
    }

    public void setReactionCategory(@NonNull String reactionCategory) {
        this.reactionCategory = reactionCategory;
    }

    public int getEpisodeId() {
        return episodeId;
    }

    public void setEpisodeId(int episodeId) {
        this.episodeId = episodeId;
    }
}
