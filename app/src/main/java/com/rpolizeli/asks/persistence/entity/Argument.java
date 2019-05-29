package com.rpolizeli.asks.persistence.entity;

import androidx.room.Entity;
import androidx.room.ForeignKey;
import androidx.room.PrimaryKey;
import androidx.annotation.NonNull;

import static androidx.room.ForeignKey.CASCADE;

@Entity
public class Argument {

    @NonNull
    @PrimaryKey(autoGenerate = true)
    private int id;

    @NonNull
    private String argument;

    @NonNull
    @ForeignKey(entity = Belief.class,parentColumns = "id",childColumns = "beliefId",onDelete = CASCADE)
    private int beliefId;

    public Argument(@NonNull String argument, @NonNull int beliefId) {
        this.argument = argument;
        this.beliefId = beliefId;
    }

    @NonNull
    public int getId() {
        return id;
    }

    public void setId(@NonNull int id) {
        this.id = id;
    }

    @NonNull
    public String getArgument() {
        return argument;
    }

    public void setArgument(@NonNull String argument) {
        this.argument = argument;
    }

    @NonNull
    public int getBeliefId() {
        return beliefId;
    }

    public void setBeliefId(@NonNull int beliefId) {
        this.beliefId = beliefId;
    }
}
