package com.example.myapplication.persistence.entity;

import android.arch.persistence.room.Entity;
import android.arch.persistence.room.PrimaryKey;
import android.support.annotation.NonNull;

import java.util.Date;

@Entity
public class Episode {

    @NonNull
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

    @NonNull
    public int getId() {
        return id;
    }

    public void setId(@NonNull int id) {
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
