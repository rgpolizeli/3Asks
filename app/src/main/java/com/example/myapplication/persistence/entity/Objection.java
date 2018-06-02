package com.example.myapplication.persistence.entity;

import android.arch.persistence.room.Entity;
import android.arch.persistence.room.ForeignKey;
import android.arch.persistence.room.PrimaryKey;
import android.support.annotation.NonNull;

import static android.arch.persistence.room.ForeignKey.CASCADE;

@Entity
public class Objection {

    @NonNull
    @PrimaryKey(autoGenerate = true)
    private int id;

    @NonNull
    private String objection;

    @NonNull
    @ForeignKey(entity = Belief.class,parentColumns = "id",childColumns = "beliefId",onDelete = CASCADE)
    private int beliefId;

    public Objection(@NonNull String objection, @NonNull int beliefId) {
        this.objection = objection;
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
    public String getObjection() {
        return objection;
    }

    public void setObjection(@NonNull String objection) {
        this.objection = objection;
    }

    @NonNull
    public int getBeliefId() {
        return beliefId;
    }

    public void setBeliefId(@NonNull int beliefId) {
        this.beliefId = beliefId;
    }
}
