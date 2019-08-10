package com.rgp.asks.persistence.entity;

import androidx.annotation.NonNull;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

import java.util.Date;

@Entity
public class Episode {

    @PrimaryKey(autoGenerate = true)
    private int id;

    @NonNull
    private String episode;

    @NonNull
    private String description;

    @NonNull
    private Date date;

    @NonNull
    private String period;

    public Episode(@NonNull String episode, @NonNull String description, @NonNull Date date, @NonNull String period) {
        this.episode = episode;
        this.description = description;
        this.date = date;
        this.period = period;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Episode episode1 = (Episode) o;

        if (getId() != episode1.getId()) return false;
        if (!getEpisode().equals(episode1.getEpisode())) return false;
        if (!getDescription().equals(episode1.getDescription())) return false;
        if (!getDate().equals(episode1.getDate())) return false;
        return getPeriod().equals(episode1.getPeriod());
    }

    @Override
    public int hashCode() {
        int result = getId();
        result = 31 * result + getEpisode().hashCode();
        result = 31 * result + getDescription().hashCode();
        result = 31 * result + getDate().hashCode();
        result = 31 * result + getPeriod().hashCode();
        return result;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    @NonNull
    public String getEpisode() {
        return episode;
    }

    public void setEpisode(@NonNull String episode) {
        this.episode = episode;
    }

    @NonNull
    public String getDescription() {
        return description;
    }

    public void setDescription(@NonNull String description) {
        this.description = description;
    }

    @NonNull
    public Date getDate() {
        return date;
    }

    public void setDate(@NonNull Date date) {
        this.date = date;
    }

    @NonNull
    public String getPeriod() {
        return period;
    }

    public void setPeriod(@NonNull String period) {
        this.period = period;
    }
}
