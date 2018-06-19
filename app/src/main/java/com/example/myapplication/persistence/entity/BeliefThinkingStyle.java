package com.example.myapplication.persistence.entity;

import android.arch.persistence.room.Entity;
import android.arch.persistence.room.ForeignKey;
import android.support.annotation.NonNull;

import static android.arch.persistence.room.ForeignKey.CASCADE;

@Entity(primaryKeys = {"beliefId","thinkingStyleId"})
public class BeliefThinkingStyle {

    @NonNull
    @ForeignKey(entity = Belief.class,parentColumns = "id",childColumns = "beliefId",onDelete = CASCADE)
    private int beliefId;

    @NonNull
    @ForeignKey(entity = ThinkingStyle.class,parentColumns = "thinkingStyle",childColumns = "thinkingStyleId",onDelete = CASCADE)
    private String thinkingStyleId;

    public BeliefThinkingStyle(@NonNull int beliefId, @NonNull String thinkingStyleId) {
        this.beliefId = beliefId;
        this.thinkingStyleId = thinkingStyleId;
    }

    @NonNull
    public int getBeliefId() {
        return beliefId;
    }

    public void setBeliefId(@NonNull int beliefId) {
        this.beliefId = beliefId;
    }

    @NonNull
    public String getThinkingStyleId() {
        return thinkingStyleId;
    }

    public void setThinkingStyleId(@NonNull String thinkingStyleId) {
        this.thinkingStyleId = thinkingStyleId;
    }
}
