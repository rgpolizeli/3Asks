package com.rgp.asks.persistence.entity;

import androidx.annotation.NonNull;
import androidx.room.Entity;
import androidx.room.ForeignKey;

import static androidx.room.ForeignKey.CASCADE;

@Entity(
        primaryKeys = {
                "beliefId",
                "thinkingStyleId"
        },
        foreignKeys = {
                @ForeignKey(entity = Belief.class, parentColumns = "id", childColumns = "beliefId", onDelete = CASCADE),
                @ForeignKey(entity = ThinkingStyle.class, parentColumns = "thinkingStyle", childColumns = "thinkingStyleId", onDelete = CASCADE)
        }
)
public class BeliefThinkingStyle {

    private int beliefId;

    @NonNull
    private String thinkingStyleId;

    public BeliefThinkingStyle(int beliefId, @NonNull String thinkingStyleId) {
        this.beliefId = beliefId;
        this.thinkingStyleId = thinkingStyleId;
    }

    public int getBeliefId() {
        return beliefId;
    }

    public void setBeliefId(int beliefId) {
        this.beliefId = beliefId;
    }

    @NonNull
    public String getThinkingStyleId() {
        return thinkingStyleId;
    }
}
