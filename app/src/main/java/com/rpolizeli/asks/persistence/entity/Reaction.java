package com.rpolizeli.asks.persistence.entity;

import androidx.room.Entity;
import androidx.room.ForeignKey;
import androidx.room.PrimaryKey;
import androidx.annotation.NonNull;

import static androidx.room.ForeignKey.CASCADE;

@Entity
public class Reaction {

    @NonNull
    @PrimaryKey(autoGenerate = true)
    private int id;

    @NonNull
    private String reaction;

    @NonNull
    private String reactionCategory;

    @NonNull
    @ForeignKey(entity = Episode.class,parentColumns = "id",childColumns = "episodeId",onDelete = CASCADE)
    private int episodeId;

    public Reaction(@NonNull String reaction, @NonNull String reactionCategory, @NonNull int episodeId) {
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

    @NonNull
    public int getEpisodeId() {
        return episodeId;
    }

    public void setEpisodeId(@NonNull int episodeId) {
        this.episodeId = episodeId;
    }
}
