package com.rgp.asks.persistence.entity;

import androidx.annotation.NonNull;
import androidx.room.Entity;
import androidx.room.ForeignKey;
import androidx.room.PrimaryKey;

import static androidx.room.ForeignKey.CASCADE;

@Entity(
        foreignKeys = {
                @ForeignKey(entity = Belief.class, parentColumns = "id", childColumns = "beliefId", onDelete = CASCADE)
        }
)
public class Argument {

    @PrimaryKey(autoGenerate = true)
    private int id;

    @NonNull
    private String argument;

    private int beliefId;

    public Argument(@NonNull String argument, int beliefId) {
        this.argument = argument;
        this.beliefId = beliefId;
    }

    public Argument(int argumentId, String argument, int beliefId) {
        this.id = argumentId;
        this.argument = argument;
        this.beliefId = beliefId;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    @NonNull
    public String getArgument() {
        return argument;
    }

    public void setArgument(@NonNull String argument) {
        this.argument = argument;
    }

    public int getBeliefId() {
        return beliefId;
    }

    public void setBeliefId(int beliefId) {
        this.beliefId = beliefId;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Argument argument1 = (Argument) o;

        if (getId() != argument1.getId()) return false;
        if (getBeliefId() != argument1.getBeliefId()) return false;
        return getArgument().equals(argument1.getArgument());
    }

    @Override
    public int hashCode() {
        int result = getId();
        result = 31 * result + getArgument().hashCode();
        result = 31 * result + getBeliefId();
        return result;
    }
}
