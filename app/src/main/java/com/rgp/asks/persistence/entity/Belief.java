package com.rgp.asks.persistence.entity;

import androidx.annotation.NonNull;
import androidx.room.Entity;
import androidx.room.ForeignKey;
import androidx.room.PrimaryKey;

import static androidx.room.ForeignKey.CASCADE;

@Entity(
        foreignKeys = {
                @ForeignKey(entity = Episode.class, parentColumns = "id", childColumns = "episodeId", onDelete = CASCADE)
        }
)
public class Belief {

    @PrimaryKey(autoGenerate = true)
    private int id;
    @NonNull
    private String belief;
    private int episodeId;

    public Belief(@NonNull String belief, int episodeId) {
        this.belief = belief;
        this.episodeId = episodeId;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    @NonNull
    public String getBelief() {
        return belief;
    }

    public void setBelief(@NonNull String belief) {
        this.belief = belief;
    }

    public int getEpisodeId() {
        return episodeId;
    }

    public void setEpisodeId(int episodeId) {
        this.episodeId = episodeId;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Belief belief1 = (Belief) o;

        if (getId() != belief1.getId()) return false;
        if (getEpisodeId() != belief1.getEpisodeId()) return false;
        return getBelief().equals(belief1.getBelief());
    }

    @Override
    public int hashCode() {
        int result = getId();
        result = 31 * result + getBelief().hashCode();
        result = 31 * result + getEpisodeId();
        return result;
    }
}
