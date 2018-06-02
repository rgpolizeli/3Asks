package com.example.myapplication.persistence.entity;

import android.arch.persistence.room.Entity;
import android.arch.persistence.room.ForeignKey;
import android.arch.persistence.room.PrimaryKey;
import android.support.annotation.NonNull;

import static android.arch.persistence.room.ForeignKey.CASCADE;

@Entity
public class Belief {

    @NonNull
    @PrimaryKey(autoGenerate = true)
    private int id;

    @NonNull
    private String belief;

    @NonNull
    @ForeignKey(entity = Episode.class,parentColumns = "id",childColumns = "episodeId",onDelete = CASCADE)
    private int episodeId;

    public Belief(@NonNull String belief, @NonNull int episodeId) {
        this.belief = belief;
        this.episodeId = episodeId;
    }

    @NonNull
    public int getId() {
        return id;
    }

    public void setId(@NonNull int id) {
        this.id = id;
    }

    @NonNull
    public String getBelief() {
        return belief;
    }

    public void setBelief(@NonNull String belief) {
        this.belief = belief;
    }

    @NonNull
    public int getEpisodeId() {
        return episodeId;
    }

    public void setEpisodeId(@NonNull int episodeId) {
        this.episodeId = episodeId;
    }
}
